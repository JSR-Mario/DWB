package com.invoice.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class DtoPaymentIn {

	@JsonProperty("method")
	@NotBlank(message = "El metodo de pago es obligatorio")
	private String method;

	@JsonProperty("card_last_four")
	@NotBlank(message = "Los ultimos 4 digitos son obligatorios")
	private String cardLastFour;

	@JsonProperty("card_holder")
	@NotBlank(message = "El nombre del titular es obligatorio")
	private String cardHolder;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCardLastFour() {
		return cardLastFour;
	}

	public void setCardLastFour(String cardLastFour) {
		this.cardLastFour = cardLastFour;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
}
