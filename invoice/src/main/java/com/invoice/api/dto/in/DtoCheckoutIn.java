package com.invoice.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoCheckoutIn {

	@JsonProperty("shipping")
	private DtoShippingIn shipping;

	@JsonProperty("payment")
	private DtoPaymentIn payment;

	@JsonProperty("coupon_code")
	private String couponCode;

	public DtoShippingIn getShipping() {
		return shipping;
	}

	public void setShipping(DtoShippingIn shipping) {
		this.shipping = shipping;
	}

	public DtoPaymentIn getPayment() {
		return payment;
	}

	public void setPayment(DtoPaymentIn payment) {
		this.payment = payment;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
}
