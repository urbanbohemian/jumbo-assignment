package com.trendyol.international.commission.invoice.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class InternationalCommissionInvoiceApiApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void homeResponse() {
		String body = this.restTemplate.getForObject("/", String.class);
		assertThat(body).isEqualTo("Spring is here!");
	}
}
