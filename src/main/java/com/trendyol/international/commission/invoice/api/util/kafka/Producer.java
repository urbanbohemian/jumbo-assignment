package com.trendyol.international.commission.invoice.api.util.kafka;

import java.util.Map;

public class Producer {
    private Map<String, Object> props;

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }
}