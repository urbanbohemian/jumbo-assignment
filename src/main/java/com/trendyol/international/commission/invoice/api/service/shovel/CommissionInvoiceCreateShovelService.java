package com.trendyol.international.commission.invoice.api.service.shovel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.model.KafkaConsumerException;
import com.trendyol.international.commission.invoice.api.repository.KafkaConsumerExceptionRepository;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.kafka.KafkaSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommissionInvoiceCreateShovelService implements JsonSupport {
    private final KafkaConsumerExceptionRepository kafkaConsumerExceptionRepository;
    private final KafkaSender kafkaSender;
    private final ObjectMapper objectMapper;
    @Value("${retryable-exceptions}")
    private List<String> retryableExceptions;
    @Value("${retry-max-attempt}")
    private Integer retryMaxAttempt;

    @Transactional
    public void shovelExceptions() throws ClassNotFoundException {
        log.info("Shovel job is started.");
        List<KafkaConsumerException> kafkaConsumerExceptionList;
        //TODO: BURASI DUSUNULMELI
//        do {
            kafkaConsumerExceptionList = kafkaConsumerExceptionRepository.findFirst1000ByExceptionTypeInAndRetryCountLessThanOrderByCreatedDateAsc(retryableExceptions,retryMaxAttempt);
            for(KafkaConsumerException kafkaConsumerException : kafkaConsumerExceptionList) {
                kafkaSender.send(kafkaConsumerException.getTopic(),
                        kafkaConsumerException.getKey(),
                        fromJson(objectMapper, Class.forName(kafkaConsumerException.getContentClassType()), kafkaConsumerException.getContent()));
            }
//        } while (!ObjectUtils.isEmpty(kafkaConsumerExceptionList));
        log.info("Shovel job is finished.");
    }
}