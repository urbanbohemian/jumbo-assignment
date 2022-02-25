package com.trendyol.international.commission.invoice.api.util;

public final class FontBuilder {
    private Float fontSize;
    private String fontFamily;

    private FontBuilder() {
    }

    public static FontBuilder aFont() {
        return new FontBuilder();
    }

    public FontBuilder fontSize(Float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public FontBuilder fontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    public Font build() {
        Font font = new Font();
        font.setFontSize(fontSize);
        font.setFontFamily(fontFamily);
        return font;
    }
}
