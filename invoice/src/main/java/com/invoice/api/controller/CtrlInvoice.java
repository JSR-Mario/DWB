package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.in.DtoCheckoutIn;
import com.invoice.api.entity.Invoice;
import com.invoice.api.service.SvcInvoice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice", description = "Facturacion y checkout")
public class CtrlInvoice {

	@Autowired
	SvcInvoice svc;

	@GetMapping
	@Operation(summary = "Consulta de facturas", description = "Administrador consulta todas las facturas. Cliente consulta sus facturas.")
	public ResponseEntity<List<DtoInvoiceList>> findAll() {	
		return ResponseEntity.ok(svc.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consulta de factura", description = "Consulta el detalle de una factura")
	public ResponseEntity<Invoice> findById(@PathVariable("id") Integer id) {		
		return ResponseEntity.ok(svc.findById(id));
	}
	
	@PostMapping
	@Operation(summary = "Finalizar compra",
			description = "Crea una factura a partir del carrito. Opcionalmente recibe direccion de envio, datos de pago y codigo de cupon.")
	public ResponseEntity<ApiResponse> create(@RequestBody(required = false) DtoCheckoutIn dto){
		return ResponseEntity.ok(svc.create(dto));
	}
	
}
