package com.invoice.api.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoCartItemOut {

	@JsonProperty("cart_item_id")
	private Integer cartItemId;

	@JsonProperty("gtin")
	private String gtin;

	@JsonProperty("product")
	private String product;

	@JsonProperty("unit_price")
	private Double unitPrice;

	@JsonProperty("quantity")
	private Integer quantity;

	public DtoCartItemOut() {
	}

	public DtoCartItemOut(Integer cartItemId, String gtin, String product, Double unitPrice, Integer quantity) {
		this.cartItemId = cartItemId;
		this.gtin = gtin;
		this.product = product;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
