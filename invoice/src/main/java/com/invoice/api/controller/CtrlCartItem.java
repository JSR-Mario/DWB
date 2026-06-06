package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.in.DtoCartItemIn;
import com.invoice.api.dto.out.DtoCartItemOut;
import com.invoice.api.service.SvcCartItem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart-item")
@Tag(name = "Cart", description = "Administracion del carrito de compras")
public class CtrlCartItem {

	@Autowired
	SvcCartItem svc;

	@PostMapping
	@Operation(summary = "Agregar al carrito",
			description = "Agrega un producto al carrito indicando GTIN y cantidad. Si ya existe, suma la cantidad.")
	public ResponseEntity<ApiResponse> addItem(@Valid @RequestBody DtoCartItemIn in) {
		return svc.addItem(in);
	}

	@GetMapping
	@Operation(summary = "Consultar carrito",
			description = "Obtiene los productos en el carrito con nombre, precio unitario y cantidad")
	public ResponseEntity<List<DtoCartItemOut>> getItems() {
		return svc.getItems();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar articulo del carrito",
			description = "Elimina un articulo especifico del carrito de compras")
	public ResponseEntity<ApiResponse> deleteItem(@PathVariable Integer id) {
		return svc.deleteItem(id);
	}

	@DeleteMapping
	@Operation(summary = "Vaciar carrito",
			description = "Elimina todos los articulos del carrito de compras del cliente")
	public ResponseEntity<ApiResponse> clearCart() {
		return svc.clearCart();
	}
}
