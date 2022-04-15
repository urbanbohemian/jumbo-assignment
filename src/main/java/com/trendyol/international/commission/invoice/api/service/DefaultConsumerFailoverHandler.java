package com.trendyol.international.commission.invoice.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.model.CustomerInvoiceError;
import com.trendyol.international.commission.invoice.api.repository.CustomerInvoiceErrorRepository;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.kafka.FailoverHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultConsumerFailoverHandler implements FailoverHandler, JsonSupport {
    private final ObjectMapper objectMapper;
    private final CustomerInvoiceErrorRepository customerInvoiceErrorRepository;

    @Override
    public void handle(ConsumerRecord record, Throwable throwable) {
        try {
            String contentAsJson = asJson(objectMapper, record.value());
            String contentMd5 = Hashing.md5(contentAsJson);
            CustomerInvoiceError.CustomerInvoiceErrorBuilder builder = CustomerInvoiceError.builder()
                    .id(contentMd5)
                    .topic(record.topic())
                    .key(record.key().toString())
                    .content(contentAsJson);

            Optional.ofNullable(throwable.getCause())
                    .ifPresentOrElse(
                            cause -> builder.exceptionDetail(cause.getClass().getName().substring(cause.getClass().getName().lastIndexOf(".") + 1)),
                            () -> builder.exceptionDetail(throwable.getClass().getName().substring(throwable.getClass().getName().lastIndexOf(".") + 1)));

            CustomerInvoiceError customerInvoiceError = builder.build();
            customerInvoiceErrorRepository.save(customerInvoiceError);

            log.error("Default Consumer Failover Handler Success. Handled Error: {}", customerInvoiceError, throwable);
        } catch (Exception e) {
            log.error("Default Consumer Failover Handler has an error while handling retry failover. Message: {}", e.getMessage(), e);
        }
    }
}