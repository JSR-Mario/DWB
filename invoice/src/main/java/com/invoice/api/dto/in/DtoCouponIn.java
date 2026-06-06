package com.invoice.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DtoCouponIn {

	@JsonProperty("code")
	@NotBlank(message = "El codigo del cupon es obligatorio")
	private String code;

	@JsonProperty("discount_percentage")
	@NotNull(message = "El porcentaje de descuento es obligatorio")
	@Min(value = 1, message = "El descuento minimo es 1%")
	@Max(value = 100, message = "El descuento maximo es 100%")
	private Double discountPercentage;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
}
