package com.trendyol.international.commission.invoice.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.model.KafkaConsumerException;
import com.trendyol.international.commission.invoice.api.repository.KafkaConsumerExceptionRepository;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.kafka.KafkaSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShovelExceptionService implements JsonSupport {
    private final KafkaConsumerExceptionRepository kafkaConsumerExceptionRepository;
    private final KafkaSender kafkaSender;
    private final ObjectMapper objectMapper;

    @Transactional
    public void shovelAllExceptions(List<String> retryableExceptionList) {
        log.info("Shovel job is started.");
        List<KafkaConsumerException> kafkaConsumerExceptionList;
        do {
            kafkaConsumerExceptionList = kafkaConsumerExceptionRepository.findFirst1000ByExceptionDetailInOrderByCreatedDateAsc(retryableExceptionList);
            kafkaConsumerExceptionList.forEach(kafkaConsumerException -> {
                try {
                    kafkaSender.send(kafkaConsumerException.getTopic(),
                            kafkaConsumerException.getKey(),
                            fromJson(objectMapper, Class.forName(kafkaConsumerException.getDataClass()), kafkaConsumerException.getContent()));
                } catch (ClassNotFoundException exception) {
                    log.error("An error has occurred on casting from JSON ,{}",exception.getMessage());;
                }
                kafkaConsumerExceptionRepository.deleteById(kafkaConsumerException.getId());
            });
        } while (!ObjectUtils.isEmpty(kafkaConsumerExceptionList));
        log.info("Shovel job is finished.");
    }
}