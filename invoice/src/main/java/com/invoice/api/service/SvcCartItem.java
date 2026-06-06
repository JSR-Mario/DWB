package com.invoice.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.in.DtoCartItemIn;
import com.invoice.api.dto.out.DtoCartItemOut;

public interface SvcCartItem {

	ResponseEntity<ApiResponse> addItem(DtoCartItemIn in);
	ResponseEntity<List<DtoCartItemOut>> getItems();
	ResponseEntity<ApiResponse> deleteItem(Integer cartItemId);
	ResponseEntity<ApiResponse> clearCart();
}
