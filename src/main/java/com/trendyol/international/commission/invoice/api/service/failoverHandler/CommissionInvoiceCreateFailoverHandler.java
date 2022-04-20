package com.trendyol.international.commission.invoice.api.service.failoverHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.model.KafkaConsumerException;
import com.trendyol.international.commission.invoice.api.repository.KafkaConsumerExceptionRepository;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service(value = "CommissionInvoiceCreateFailoverHandler")
public class CommissionInvoiceCreateFailoverHandler extends FailoverHandler {
    private final KafkaConsumerExceptionRepository kafkaConsumerExceptionRepository;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String getHashId(ConsumerRecord consumerRecord) {
        CommissionInvoiceCreateEvent commissionInvoiceCreateEvent = fromJson(objectMapper, CommissionInvoiceCreateEvent.class, consumerRecord.value());
        return Hashing.md5(Optional.ofNullable(commissionInvoiceCreateEvent.getSellerId()).orElse(0L).toString()
                .concat(Optional.ofNullable(commissionInvoiceCreateEvent.getCountry()).orElse(""))
                .concat(Optional.ofNullable(commissionInvoiceCreateEvent.getCurrency()).orElse(""))
                .concat(Optional.ofNullable(commissionInvoiceCreateEvent.getAutomaticInvoiceStartDate()).orElse(new Date(1L)).toString())
                .concat(Optional.ofNullable(commissionInvoiceCreateEvent.getEndDate()).orElse(new Date(1L)).toString()));
    }


    @Override
    public void logException(Exception e) {
        log.error("CommissionInvoiceCreateFailoverHandler has an error while handling retry failover. Message: {}", e.getMessage());
    }

    @Override
    public void persistException(Consumer consumer, ConsumerRecord consumerRecord, Exception exception, String hashId) {
        Optional<KafkaConsumerException> optionalKafkaConsumerException = kafkaConsumerExceptionRepository.findById(hashId);
        if(optionalKafkaConsumerException.isPresent()) {
            KafkaConsumerException kafkaConsumerException = optionalKafkaConsumerException.get();
            kafkaConsumerException.setRetryCount(kafkaConsumerException.getRetryCount() + 1);
            kafkaConsumerExceptionRepository.save(kafkaConsumerException);
        } else {
            KafkaConsumerException kafkaConsumerException = KafkaConsumerException.builder()
                    .id(hashId)
                    .topic(consumer.getReproduceTopic())
                    .key(consumerRecord.key().toString())
                    .content(consumerRecord.value())
                    .exceptionType(Optional.ofNullable(exception.getCause())
                            .map(cause -> cause.getClass().getName().substring(cause.getClass().getName().lastIndexOf(".") + 1))
                            .orElseGet(() -> exception.getClass().getName().substring(exception.getClass().getName().lastIndexOf(".") + 1)))
                    .contentClassType(CommissionInvoiceCreateEvent.class.getName())
                    .retryCount(0)
                    .build();
            kafkaConsumerExceptionRepository.save(kafkaConsumerException);
        }
    }
}