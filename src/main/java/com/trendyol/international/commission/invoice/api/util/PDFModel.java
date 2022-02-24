package com.trendyol.international.commission.invoice.api.util;

public class PDFModel {
    private String resourceType;
    private String resourceValue;
    private Integer resourceHeight;
    private Integer resourceWidth;
    private Coordinate coordinates;
    private String sourcePath;
    private ColorValue color;
    private Font font;
    private TableInfo tableInfo;
    private String dependsOn;
    private Coordinate shiftValues;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(String resourceValue) {
        this.resourceValue = resourceValue;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public ColorValue getColor() {
        return color;
    }

    public void setColor(ColorValue color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Integer getResourceHeight() {
        return resourceHeight;
    }

    public void setResourceHeight(Integer resourceHeight) {
        this.resourceHeight = resourceHeight;
    }

    public Integer getResourceWidth() {
        return resourceWidth;
    }

    public void setResourceWidth(Integer resourceWidth) {
        this.resourceWidth = resourceWidth;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public Coordinate getShiftValues() {
        return shiftValues;
    }

    public void setShiftValues(Coordinate shiftValues) {
        this.shiftValues = shiftValues;
    }
}
