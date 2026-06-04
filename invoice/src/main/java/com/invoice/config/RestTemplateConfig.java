package com.invoice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuracion de RestTemplate para llamadas inter-servicio.
 * Agrega automaticamente el token JWT del usuario actual a cada peticion saliente.
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		// Interceptor que reenvía el JWT del usuario al servicio destino
		restTemplate.getInterceptors().add((request, body, execution) -> {
			ServletRequestAttributes attrs =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				String authHeader = attrs.getRequest().getHeader("Authorization");
				if (authHeader != null) {
					request.getHeaders().set("Authorization", authHeader);
				}
			}
			return execution.execute(request, body);
		});

		return restTemplate;
	}
}
