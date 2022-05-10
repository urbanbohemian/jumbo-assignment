package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.entity.KafkaConsumerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KafkaConsumerExceptionRepository extends JpaRepository<KafkaConsumerException, String> {

    Page<KafkaConsumerException> findAllByExceptionTypeInAndRetryCountLessThanOrderByCreatedDateAsc(List<String> exceptionDetailList, Integer maxAttempt, Pageable pageable);
}
