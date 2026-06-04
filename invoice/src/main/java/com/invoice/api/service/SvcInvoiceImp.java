package com.invoice.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.DtoProductResponse;
import com.invoice.api.entity.CartItem;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCartItem;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.commons.mapper.MapperInvoice;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

import jakarta.transaction.Transactional;

@Service
public class SvcInvoiceImp implements SvcInvoice {

	@Autowired
	private RepoInvoice repo;

	@Autowired
	private RepoCartItem repoCartItem;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Autowired
	MapperInvoice mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.service.url}")
	private String productServiceUrl;

	@Override
	public List<DtoInvoiceList> findAll() {
		try {
			if (jwtDecoder.isAdmin()) {
				return mapper.toDtoList(repo.findAll());
			} else {
				Integer user_id = jwtDecoder.getUserId();
				return mapper.toDtoList(repo.findAllByUserId(user_id));
			}
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public Invoice findById(Integer id) {
		try {
			Invoice invoice = repo.findById(id).get();
			if (!jwtDecoder.isAdmin()) {
				Integer user_id = jwtDecoder.getUserId();
				if (!invoice.getUser_id().equals(user_id)) {
					throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
				}
			}
			return invoice;
		} catch (DataAccessException e) {
			throw new DBAccessException();
		} catch (NoSuchElementException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe");
		}
	}

	@Override
	@Transactional
	public ApiResponse create() {
		try {
			Integer userId = jwtDecoder.getUserId();

			// 1. Obtener los articulos del carrito del cliente
			List<CartItem> cartItems = repoCartItem.findByUserIdAndStatus(userId, 1);

			if (cartItems.isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
			}

			// 2. Validar stock y construir los items de la factura
			List<InvoiceItem> invoiceItems = new ArrayList<>();
			double invoiceTotal = 0.0;
			double invoiceTaxes = 0.0;
			double invoiceSubtotal = 0.0;

			for (CartItem cartItem : cartItems) {
				// Consultar el producto en el product service
				DtoProductResponse product = getProductByGtin(cartItem.getGtin());

				// Validar stock suficiente
				if (product.getStock() < cartItem.getQuantity()) {
					throw new ApiException(HttpStatus.CONFLICT,
							"Stock insuficiente para " + product.getProduct()
									+ ". Disponible: " + product.getStock()
									+ ", solicitado: " + cartItem.getQuantity());
				}

				// 3. Calcular totales del item
				double itemTotal = cartItem.getQuantity() * product.getPrice();
				double itemTaxes = itemTotal * 0.16;
				double itemSubtotal = itemTotal - itemTaxes;

				InvoiceItem invoiceItem = new InvoiceItem();
				invoiceItem.setGtin(cartItem.getGtin());
				invoiceItem.setQuantity(cartItem.getQuantity());
				invoiceItem.setUnit_price(product.getPrice());
				invoiceItem.setTotal(itemTotal);
				invoiceItem.setTaxes(itemTaxes);
				invoiceItem.setSubtotal(itemSubtotal);
				invoiceItem.setStatus(1);

				invoiceItems.add(invoiceItem);

				// Acumular totales de la factura
				invoiceTotal += itemTotal;
				invoiceTaxes += itemTaxes;
				invoiceSubtotal += itemSubtotal;
			}

			// 4. Guardar la factura con sus items
			Invoice invoice = new Invoice();
			invoice.setUser_id(userId);
			invoice.setCreated_at(LocalDate.now());
			invoice.setTotal(invoiceTotal);
			invoice.setTaxes(invoiceTaxes);
			invoice.setSubtotal(invoiceSubtotal);
			invoice.setStatus(1);
			invoice.setItems(invoiceItems);

			repo.save(invoice);

			// 5. Decrementar stock de los productos comprados
			for (CartItem cartItem : cartItems) {
				updateProductStock(cartItem.getGtin(), -cartItem.getQuantity());
			}

			// 6. Vaciar el carrito del cliente
			for (CartItem cartItem : cartItems) {
				cartItem.setStatus(0);
			}
			repoCartItem.saveAll(cartItems);

			return new ApiResponse("La factura ha sido registrada");
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	/**
	 * Consulta el product service para obtener informacion de un producto por GTIN.
	 */
	private DtoProductResponse getProductByGtin(String gtin) {
		try {
			String url = productServiceUrl + "/product/gtin/" + gtin;
			return restTemplate.getForObject(url, DtoProductResponse.class);
		} catch (HttpClientErrorException.NotFound e) {
			throw new ApiException(HttpStatus.NOT_FOUND,
					"El producto con gtin " + gtin + " no existe");
		} catch (HttpClientErrorException e) {
			throw new ApiException(HttpStatus.BAD_REQUEST,
					"Error al consultar el producto: " + e.getMessage());
		} catch (Exception e) {
			throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
					"El servicio de productos no esta disponible");
		}
	}

	/**
	 * Llama al product service para actualizar el stock de un producto.
	 */
	private void updateProductStock(String gtin, Integer quantity) {
		try {
			String url = productServiceUrl + "/product/gtin/" + gtin + "/stock";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String body = "{\"quantity\":" + quantity + "}";
			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
		} catch (HttpClientErrorException e) {
			throw new ApiException(HttpStatus.CONFLICT,
					"Error al actualizar stock del producto " + gtin + ": " + e.getMessage());
		} catch (Exception e) {
			throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
					"El servicio de productos no esta disponible para actualizar stock");
		}
	}
}
