package com.trendyol.international.commission.invoice.api;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class InternationalCommissionInvoiceApiApplication {

	@GetMapping("/")
	String home() {
		return "Spring is here!";
	}

	public static void main(String[] args) {
		SpringApplication.run(InternationalCommissionInvoiceApiApplication.class, args);
	}
}