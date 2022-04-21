package com.trendyol.international.commission.invoice.api.util.kafka;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Consumer {
    private String topic;
    private String retryTopic;
    private String reproduceTopic;
    private String errorTopic;
    private Map<String, Object> props;
    private Integer concurrency;
    private Integer retryCount;
    private Long backoffIntervalMillis;
    private Double multiplier;
    private Long maxInterval;
    private Integer timeoutMillis;
    private Integer syncCommitTimeoutSecond;
    private Boolean syncCommit;
    private Boolean missingTopicAlertEnable;
    private Boolean autoStartup;
    private String failoverHandlerBeanName;
    private String factoryBeanName;
    private String dataClass;
}