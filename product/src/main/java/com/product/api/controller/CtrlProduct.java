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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class CtrlProduct {

	@Autowired
	SvcProduct svc;

	@Autowired
	SvcProductImage svcImage;

	@GetMapping
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		return svc.getProducts();
	}

	@GetMapping("/{id}")
	public ResponseEntity<DtoProductOut> getProduct(@PathVariable Integer id) {
		return svc.getProduct(id);
	}

	@PostMapping
	public ResponseEntity<String> createProduct(@Valid @RequestBody DtoProductIn in) {
		return svc.createProduct(in);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Integer id, @Valid @RequestBody DtoProductIn in) {
		return svc.updateProduct(id, in);
	}

	@PatchMapping("/{id}/enable")
	public ResponseEntity<String> enableProduct(@PathVariable Integer id) {
		return svc.enableProduct(id);
	}

	@PatchMapping("/{id}/disable")
	public ResponseEntity<String> disableProduct(@PathVariable Integer id) {
		return svc.disableProduct(id);
	}

	// ── Product Image ──────────────────────────────────────────────

	@GetMapping("/{id}/image")
	public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Integer id) {
		return svcImage.getProductImages(id);
	}

	@PostMapping("/{id}/image")
	public ResponseEntity<String> createProductImage(
			@PathVariable Integer id,
			@Valid @RequestBody DtoProductImageIn in) {
		return svcImage.createProductImage(id, in);
	}

	@DeleteMapping("/{id}/image/{product-image-id}")
	public ResponseEntity<String> deleteProductImage(
			@PathVariable Integer id,
			@PathVariable("product-image-id") Integer productImageId) {
		return svcImage.deleteProductImage(id, productImageId);
	}
}
