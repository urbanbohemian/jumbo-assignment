package com.trendyol.international.commission.invoice.api.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "pdf-config")
public class PdfConfig {

    private Map<String,PDFModel> components;

    public Map<String, PDFModel> getComponents() {
        return components;
    }

    public void setComponents(Map<String, PDFModel> components) {
        this.components = components;
    }
}