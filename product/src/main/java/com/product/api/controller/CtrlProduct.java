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
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.ProductImage;
import com.product.api.service.SvcProduct;
import com.product.api.service.SvcProductImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
@Tag(name = "Product", description = "Catálogo de productos")
public class CtrlProduct {

	@Autowired
	SvcProduct svc;

	@Autowired
	SvcProductImage svcImage;

	@Operation(summary = "Obtener todos los productos", description = "Devuelve una lista con la información resumida de todos los productos en el catálogo. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Prohibido - Requiere rol ADMIN", content = @Content)
	})
	@GetMapping
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		return svc.getProducts();
	}

	@Operation(summary = "Consultar detalle de producto", description = "Obtiene toda la información detallada de un producto específico, incluyendo su información de categoría e imagen decodificada en Base64. Accesible por ADMIN y CUSTOMER.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Detalle del producto obtenido exitosamente"),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@GetMapping("/{id}")
	public ResponseEntity<DtoProductOut> getProduct(@PathVariable Integer id) {
		return svc.getProduct(id);
	}

	@Operation(summary = "Registrar nuevo producto", description = "Añade un nuevo producto al catálogo validando que el GTIN y el nombre sean únicos. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Producto registrado exitosamente", content = @Content),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o duplicados", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Prohibido - Requiere rol ADMIN", content = @Content)
	})
	@PostMapping
	public ResponseEntity<String> createProduct(@Valid @RequestBody DtoProductIn in) {
		return svc.createProduct(in);
	}

	@Operation(summary = "Actualizar producto", description = "Modifica la información de un producto existente. Válida unicidad de datos. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o campos duplicados", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Integer id, @Valid @RequestBody DtoProductIn in) {
		return svc.updateProduct(id, in);
	}

	@Operation(summary = "Activar producto", description = "Cambia el estatus del producto a activo. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Producto activado exitosamente", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@PatchMapping("/{id}/enable")
	public ResponseEntity<String> enableProduct(@PathVariable Integer id) {
		return svc.enableProduct(id);
	}

	@Operation(summary = "Desactivar producto", description = "Cambia el estatus del producto a inactivo. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Producto desactivado exitosamente", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@PatchMapping("/{id}/disable")
	public ResponseEntity<String> disableProduct(@PathVariable Integer id) {
		return svc.disableProduct(id);
	}

	// ── Product Image ──────────────────────────────────────────────

	@Operation(summary = "Obtener imágenes del producto", description = "Devuelve la lista de metadatos de las imágenes registradas asociadas a un producto. Accesible por ADMIN y CUSTOMER.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de imágenes obtenida exitosamente"),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Integer id) {
		return svcImage.getProductImages(id);
	}

	@Operation(summary = "Subir imagen para producto", description = "Sube y registra una nueva imagen en formato Base64 para un producto, guardándola físicamente en el servidor. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content),
			@ApiResponse(responseCode = "400", description = "Petición inválida o formato incorrecto", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
	})
	@PostMapping("/{id}/image")
	public ResponseEntity<String> createProductImage(
			@PathVariable Integer id,
			@Valid @RequestBody DtoProductImageIn in) {
		return svcImage.createProductImage(id, in);
	}

	@Operation(summary = "Eliminar imagen de producto", description = "Borra el registro y el archivo físico de una imagen asociada a un producto. Requiere rol ADMIN.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagen eliminada exitosamente", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Imagen o Producto no encontrado", content = @Content)
	})
	@DeleteMapping("/{id}/image/{product-image-id}")
	public ResponseEntity<String> deleteProductImage(
			@PathVariable Integer id,
			@PathVariable("product-image-id") Integer productImageId) {
		return svcImage.deleteProductImage(id, productImageId);
	}
}
