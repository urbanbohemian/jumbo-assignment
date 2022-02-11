package com.trendyol.international.commission.invoice.api.model.document;

import java.util.Arrays;

public class PDFDocument {

    private String name;
    private byte[] content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PDFDocument{" +
                "name='" + name + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
