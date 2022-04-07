package com.trendyol.international.commission.invoice.api;

import com.trendyol.mpc.consul.configuration.processor.EnableConsulProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableConsulProcessor({"/configs/config.json", "/configs/toggle.json", "/configs/secret.json"})
@SpringBootApplication
public class InternationalCommissionInvoiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternationalCommissionInvoiceApiApplication.class, args);
    }
}