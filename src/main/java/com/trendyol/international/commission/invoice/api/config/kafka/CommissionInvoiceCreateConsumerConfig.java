//package com.trendyol.international.commission.invoice.api.config.kafka;
//
//import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
//import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
//import com.trendyol.international.commission.invoice.api.util.kafka.KafkaConsumerUtil;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.KafkaOperations;
//
//@Configuration
//public class CommissionInvoiceCreateConsumerConfig {
//    private Consumer consumer;
//    private final KafkaConsumerUtil kafkaConsumerUtil;
//
//    public CommissionInvoiceCreateConsumerConfig(KafkaConsumerUtil kafkaConsumerUtil, KafkaProducerConsumerProps kafkaProducerConsumerProps) {
//        this.kafkaConsumerUtil = kafkaConsumerUtil;
//        consumer = kafkaProducerConsumerProps.getConsumers().get("commission-invoice-create-consumer");
//    }
//
//    @Bean
//    public ConsumerFactory<String, CommissionInvoiceCreateEvent> commissionInvoiceCreateConsumerFactory() {
//        return kafkaConsumerUtil.createConsumerFactory(consumer, CommissionInvoiceCreateEvent.class);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, CommissionInvoiceCreateEvent> commissionInvoiceCreateKafkaListenerContainerFactory(KafkaOperations<String, Object> kafkaOperations) {
//        return kafkaConsumerUtil.createSingleKafkaListenerContainerFactory(
//                kafkaOperations,
//                commissionInvoiceCreateConsumerFactory(),
//                consumer
//        );
//    }
//}
