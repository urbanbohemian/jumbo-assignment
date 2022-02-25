package com.trendyol.international.commission.invoice.api.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "pdf-config")
public class PdfConfig {
    private String outputFileName;
    private Map<String, PdfComponent> components;

    public Map<String, PdfComponent> getComponents() {
        return components;
    }

    public void setComponents(Map<String, PdfComponent> components) {
        this.components = components;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}