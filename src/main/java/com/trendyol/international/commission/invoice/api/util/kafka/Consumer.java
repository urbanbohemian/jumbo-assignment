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
    private String errorTopic;
    private Map<String, Object> props;
    private int concurrency;
    private int retryCount;
    private long backoffIntervalMillis;
    private int timeoutMillis;
    private int syncCommitTimeoutSecond;
    private boolean syncCommit;
    private Boolean missingTopicAlertEnable;
    private Boolean autoStartup;
    private String failoverHandlerBeanName;
    private String factoryBeanName;
}