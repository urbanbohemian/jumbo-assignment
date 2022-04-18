package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.model.KafkaConsumerException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KafkaConsumerExceptionRepository extends JpaRepository<KafkaConsumerException, String> {
    List<KafkaConsumerException> findFirst1000ByExceptionTypeInOrderByCreatedDateAsc(List<String> exceptionDetailList);
}
