package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.Product;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.common.mapper.MapperProduct;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcProductImp implements SvcProduct {

	@Autowired
	RepoProduct repo;

	@Autowired
	RepoProductImage repoProductImage;

	@Autowired
	MapperProduct mapper;

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Value("${app.upload.images}")
	private String uploadImages;

	@Override
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		try {
			List<Product> products = repo.findAll();
			return new ResponseEntity<>(mapper.fromProductList(products), HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<DtoProductOut> getProduct(Integer id) {
		try {
			DtoProductOut product = repo.getProduct(id);
			if (product == null) {
				throw new ApiException("El id del producto no existe", HttpStatus.NOT_FOUND);
			}

			String image = readProductImageFile(id);
			product.setImage(image);

			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> createProduct(DtoProductIn in) {
		try {
			Product product = mapper.fromDto(in);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido registrado", HttpStatus.CREATED);
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException("El gtin del producto ya está registrado", HttpStatus.CONFLICT);
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException("El nombre del producto ya está registrado", HttpStatus.CONFLICT);
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException("El id de categoría no existe", HttpStatus.NOT_FOUND);

			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> updateProduct(Integer id, DtoProductIn in) {
		try {
			validateProductId(id);
			Product product = mapper.fromDto(id, in);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido actualizado", HttpStatus.OK);
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException("El gtin del producto ya está registrado", HttpStatus.CONFLICT);
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException("El nombre del producto ya está registrado", HttpStatus.CONFLICT);
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException("El id de categoría no existe", HttpStatus.NOT_FOUND);

			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> enableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(1);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido activado", HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> disableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(0);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido desactivado", HttpStatus.OK);
		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	private void validateProductId(Integer id) {
		try {
			if (repo.findById(id).isEmpty()) {
				throw new ApiException("El id del producto no existe", HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	private String readProductImageFile(Integer productId) {
		try {
			List<ProductImage> images = repoProductImage.findByProductId(productId);
			if (images.isEmpty()) {
				return "";
			}

			ProductImage productImage = images.get(0);
			String imageUrl = productImage.getImage();

			if (imageUrl != null && imageUrl.startsWith("/")) {
				imageUrl = imageUrl.substring(1);
			}

			Path imagePath = Paths.get(uploadDir, uploadImages, imageUrl);

			if (!Files.exists(imagePath)) {
				return "";
			}

			byte[] imageBytes = Files.readAllBytes(imagePath);
			return Base64.getEncoder().encodeToString(imageBytes);

		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		} catch (IOException e) {
			throw new ApiException("Error al leer el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
