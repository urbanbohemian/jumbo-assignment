package com.trendyol.international.commission.invoice.api.util;

import java.util.Map;

public class Consumer {
    private String topic;
    private String errorTopic;
    private Map<String, Object> props;
    private int concurrency;
    private int retryCount;
    private int timeoutMillis;
    private int syncCommitTimeoutSecond;
    private boolean syncCommit;
    private long backoffIntervalMillis;
    private Boolean missingTopicAlertEnable;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getErrorTopic() {
        return errorTopic;
    }

    public void setErrorTopic(String errorTopic) {
        this.errorTopic = errorTopic;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getSyncCommitTimeoutSecond() {
        return syncCommitTimeoutSecond;
    }

    public void setSyncCommitTimeoutSecond(int syncCommitTimeoutSecond) {
        this.syncCommitTimeoutSecond = syncCommitTimeoutSecond;
    }

    public boolean isSyncCommit() {
        return syncCommit;
    }

    public void setSyncCommit(boolean syncCommit) {
        this.syncCommit = syncCommit;
    }

    public long getBackoffIntervalMillis() {
        return backoffIntervalMillis;
    }

    public void setBackoffIntervalMillis(long backoffIntervalMillis) {
        this.backoffIntervalMillis = backoffIntervalMillis;
    }

    public Boolean getMissingTopicAlertEnable() {
        return missingTopicAlertEnable;
    }

    public void setMissingTopicAlertEnable(Boolean missingTopicAlertEnable) {
        this.missingTopicAlertEnable = missingTopicAlertEnable;
    }
}