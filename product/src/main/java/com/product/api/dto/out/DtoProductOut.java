package com.product.api.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoProductOut {
	
	@JsonProperty("product_id")
	private Integer product_id;
	
	@JsonProperty("gtin")
	private String gtin;

	@JsonProperty("product")
	private String product;

	@JsonProperty("description")
	private String description;

	@JsonProperty("price")
	private Float price;

	@JsonProperty("stock")
	private Integer stock;

	@JsonProperty("status")
	private Integer status;

	@JsonProperty("category_id")
	private Integer category_id;

	@JsonProperty("category")
	private String category;

	@JsonProperty("image")
	private String image;

	public DtoProductOut() {
	}

	public DtoProductOut(Integer product_id, String gtin, String product, String description, Float price, Integer stock, Integer status, Integer category_id, String category) {
		this.product_id = product_id;
		this.gtin = gtin;
		this.product = product;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.status = status;
		this.category_id = category_id;
		this.category = category;
	}

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
