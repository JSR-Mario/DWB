package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.in.DtoCouponIn;
import com.invoice.api.entity.Coupon;
import com.invoice.api.service.SvcCoupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupon", description = "Administracion de cupones de descuento")
public class CtrlCoupon {

	@Autowired
	private SvcCoupon svc;

	@GetMapping
	@Operation(summary = "Listar cupones", description = "Obtiene todos los cupones registrados (solo admin)")
	public ResponseEntity<List<Coupon>> getAll() {
		return ResponseEntity.ok(svc.getAll());
	}

	@PostMapping
	@Operation(summary = "Crear cupon", description = "Registra un nuevo cupon de descuento (solo admin)")
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody DtoCouponIn dto) {
		return new ResponseEntity<>(svc.create(dto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Desactivar cupon", description = "Desactiva un cupon de descuento existente (solo admin)")
	public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
		return ResponseEntity.ok(svc.delete(id));
	}
}
