package com.product.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.in.DtoStockIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.ProductImage;
import com.product.api.service.SvcProduct;
import com.product.api.service.SvcProductImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
@Tag(name = "Product", description = "Administracion de productos")
public class CtrlProduct {

	@Autowired
	SvcProduct svc;

	@Autowired
	SvcProductImage svcImage;

	@GetMapping
	@Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados")
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		return svc.getProducts();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consultar producto por ID", description = "Obtiene el detalle de un producto por su ID")
	public ResponseEntity<DtoProductOut> getProduct(@PathVariable Integer id) {
		return svc.getProduct(id);
	}

	@GetMapping("/gtin/{gtin}")
	@Operation(summary = "Consultar producto por GTIN", description = "Obtiene el detalle de un producto por su GTIN")
	public ResponseEntity<DtoProductOut> getProductByGtin(@PathVariable String gtin) {
		return svc.getProductByGtin(gtin);
	}

	@PostMapping
	@Operation(summary = "Registrar producto", description = "Registra un nuevo producto en el catalogo")
	public ResponseEntity<String> createProduct(@Valid @RequestBody DtoProductIn in) {
		return svc.createProduct(in);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar producto", description = "Actualiza la informacion de un producto existente")
	public ResponseEntity<String> updateProduct(@PathVariable Integer id, @Valid @RequestBody DtoProductIn in) {
		return svc.updateProduct(id, in);
	}

	@PatchMapping("/{id}/enable")
	@Operation(summary = "Activar producto", description = "Activa un producto deshabilitado")
	public ResponseEntity<String> enableProduct(@PathVariable Integer id) {
		return svc.enableProduct(id);
	}

	@PatchMapping("/{id}/disable")
	@Operation(summary = "Desactivar producto", description = "Desactiva un producto")
	public ResponseEntity<String> disableProduct(@PathVariable Integer id) {
		return svc.disableProduct(id);
	}

	@PatchMapping("/gtin/{gtin}/stock")
	@Operation(summary = "Actualizar stock", description = "Suma o resta unidades al stock de un producto identificado por GTIN")
	public ResponseEntity<String> updateStock(@PathVariable String gtin, @Valid @RequestBody DtoStockIn in) {
		return svc.updateStock(gtin, in);
	}

	// ── Product Image ──────────────────────────────────────────────

	@GetMapping("/{id}/image")
	@Operation(summary = "Listar imagenes de producto", description = "Obtiene las imagenes de un producto")
	public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Integer id) {
		return svcImage.getProductImages(id);
	}

	@PostMapping("/{id}/image")
	@Operation(summary = "Registrar imagen de producto", description = "Registra una nueva imagen para un producto")
	public ResponseEntity<String> createProductImage(
			@PathVariable Integer id,
			@Valid @RequestBody DtoProductImageIn in) {
		return svcImage.createProductImage(id, in);
	}

	@DeleteMapping("/{id}/image/{product-image-id}")
	@Operation(summary = "Eliminar imagen de producto", description = "Elimina una imagen de un producto")
	public ResponseEntity<String> deleteProductImage(
			@PathVariable Integer id,
			@PathVariable("product-image-id") Integer productImageId) {
		return svcImage.deleteProductImage(id, productImageId);
	}
}
