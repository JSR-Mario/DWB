package com.invoice.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class DtoShippingIn {

	@JsonProperty("street")
	@NotBlank(message = "La calle es obligatoria")
	private String street;

	@JsonProperty("city")
	@NotBlank(message = "La ciudad es obligatoria")
	private String city;

	@JsonProperty("state")
	@NotBlank(message = "El estado es obligatorio")
	private String state;

	@JsonProperty("zip_code")
	@NotBlank(message = "El codigo postal es obligatorio")
	private String zipCode;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
