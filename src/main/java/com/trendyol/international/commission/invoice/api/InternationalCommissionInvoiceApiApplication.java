package com.trendyol.international.commission.invoice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaAuditing
public class InternationalCommissionInvoiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternationalCommissionInvoiceApiApplication.class, args);
	}

}