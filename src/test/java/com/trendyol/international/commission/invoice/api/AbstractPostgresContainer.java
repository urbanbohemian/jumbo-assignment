package com.trendyol.international.commission.invoice.api;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = AbstractPostgresContainer.Initializer.class)
@Testcontainers
public abstract class AbstractPostgresContainer {

    static final PostgreSQLContainer POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER =  new PostgreSQLContainer<>(DockerImageName
                .parse("registry.trendyol.com/platform/base/image/postgres:11.6")
                .asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("int_seller_commission_invoice")
                .withUsername("int_seller_commission_invoice_appuser")
                .withPassword("1234567890");

        POSTGRES_CONTAINER.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}