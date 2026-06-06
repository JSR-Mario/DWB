package com.invoice.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoProductResponse;
import com.invoice.api.dto.in.DtoCartItemIn;
import com.invoice.api.dto.out.DtoCartItemOut;
import com.invoice.api.entity.CartItem;
import com.invoice.api.repository.RepoCartItem;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

@Service
public class SvcCartItemImp implements SvcCartItem {

	@Autowired
	private RepoCartItem repo;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.service.url}")
	private String productServiceUrl;

	@Override
	public ResponseEntity<ApiResponse> addItem(DtoCartItemIn in) {
		try {
			Integer userId = jwtDecoder.getUserId();

			// Validar que el producto existe y tiene stock consultando el product service
			DtoProductResponse product = getProductByGtin(in.getGtin());

			if (product.getStatus() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "El producto no esta activo");
			}

			if (product.getStock() < in.getQuantity()) {
				throw new ApiException(HttpStatus.CONFLICT,
						"Stock insuficiente. Disponible: " + product.getStock());
			}

			// Si el producto ya esta en el carrito, solo actualizar la cantidad
			Optional<CartItem> existing = repo.findByUserIdAndGtinAndStatus(userId, in.getGtin(), 1);

			if (existing.isPresent()) {
				CartItem cartItem = existing.get();
				int newQuantity = cartItem.getQuantity() + in.getQuantity();

				// Validar que la nueva cantidad no exceda el stock
				if (newQuantity > product.getStock()) {
					throw new ApiException(HttpStatus.CONFLICT,
							"Stock insuficiente. Disponible: " + product.getStock()
									+ ", en carrito: " + cartItem.getQuantity());
				}

				cartItem.setQuantity(newQuantity);
				repo.save(cartItem);
				return new ResponseEntity<>(
						new ApiResponse("Cantidad actualizada en el carrito"), HttpStatus.OK);
			}

			// Crear nuevo item en el carrito
			CartItem cartItem = new CartItem();
			cartItem.setUserId(userId);
			cartItem.setGtin(in.getGtin());
			cartItem.setQuantity(in.getQuantity());
			cartItem.setStatus(1);
			repo.save(cartItem);

			return new ResponseEntity<>(
					new ApiResponse("El articulo ha sido agregado al carrito"), HttpStatus.CREATED);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public ResponseEntity<List<DtoCartItemOut>> getItems() {
		try {
			Integer userId = jwtDecoder.getUserId();
			List<CartItem> items = repo.findByUserIdAndStatus(userId, 1);

			List<DtoCartItemOut> result = new ArrayList<>();
			for (CartItem item : items) {
				try {
					DtoProductResponse product = getProductByGtin(item.getGtin());
					result.add(new DtoCartItemOut(
							item.getCartItemId(),
							item.getGtin(),
							product.getProduct(),
							product.getPrice(),
							item.getQuantity()
					));
				} catch (ApiException e) {
					// Si el producto ya no existe, mostrar con datos parciales
					result.add(new DtoCartItemOut(
							item.getCartItemId(),
							item.getGtin(),
							"Producto no disponible",
							0.0,
							item.getQuantity()
					));
				}
			}

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public ResponseEntity<ApiResponse> deleteItem(Integer cartItemId) {
		try {
			Integer userId = jwtDecoder.getUserId();

			CartItem cartItem = repo.findById(cartItemId)
					.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
							"El articulo no existe en el carrito"));

			if (!cartItem.getUserId().equals(userId)) {
				throw new ApiException(HttpStatus.FORBIDDEN,
						"El articulo no pertenece al usuario");
			}

			if (cartItem.getStatus() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST,
						"El articulo ya fue eliminado del carrito");
			}

			cartItem.setStatus(0);
			repo.save(cartItem);

			return new ResponseEntity<>(
					new ApiResponse("El articulo ha sido eliminado del carrito"), HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public ResponseEntity<ApiResponse> clearCart() {
		try {
			Integer userId = jwtDecoder.getUserId();
			List<CartItem> items = repo.findByUserIdAndStatus(userId, 1);

			if (items.isEmpty()) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El carrito esta vacio");
			}

			for (CartItem item : items) {
				item.setStatus(0);
			}
			repo.saveAll(items);

			return new ResponseEntity<>(
					new ApiResponse("El carrito ha sido vaciado"), HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	/**
	 * Consulta el product service para obtener informacion de un producto por GTIN.
	 */
	DtoProductResponse getProductByGtin(String gtin) {
		try {
			String url = productServiceUrl + "/product/gtin/" + gtin;
			ResponseEntity<DtoProductResponse> response =
					restTemplate.getForEntity(url, DtoProductResponse.class);
			return response.getBody();
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
}
