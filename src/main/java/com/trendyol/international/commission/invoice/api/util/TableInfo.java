package com.trendyol.international.commission.invoice.api.util;

import java.util.Map;

public class TableInfo {
    private Map<String,String> columnNames;
    private Map<String,Integer> columnWidths;
    private ColorValue borderColor;
    private ColorValue backgroundColor;
    private Float borderWidth;

    public Map<String, String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Map<String, String> columnNames) {
        this.columnNames = columnNames;
    }

    public Map<String, Integer> getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(Map<String, Integer> columnWidths) {
        this.columnWidths = columnWidths;
    }

    public ColorValue getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(ColorValue borderColor) {
        this.borderColor = borderColor;
    }

    public ColorValue getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ColorValue backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Float borderWidth) {
        this.borderWidth = borderWidth;
    }
}
