package com.trendyol.international.commission.invoice.api.util.kafka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Map;
import java.util.Objects;

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
    private String dataClass;
//    @JsonIgnore
//    public String getDataClass() {
//        return Objects.nonNull(dataClass) ? dataClass : props.get("[json.value.type]").toString();
//    }
}