package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.util.ColorValue;
import com.trendyol.international.commission.invoice.api.util.PDFModel;
import com.trendyol.international.commission.invoice.api.util.PdfConfig;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PDFBoxService {
    //    // LEFT,TOP,RIGHT,BOTTOM
//    private final List<Float> paddingList = Arrays.asList(16f, 16f, 16f, 16f);
//
//    private enum PADDING {
//        LEFT,
//        TOP,
//        RIGHT,
//        BOTTOM
//    }
//
//    public Float getPadding(PADDING padding) {
//        return switch (padding) {
//            case LEFT -> paddingList.get(0);
//            case TOP -> paddingList.get(1);
//            case RIGHT -> paddingList.get(2);
//            case BOTTOM -> paddingList.get(3);
//        };
//    }
//
//    private enum ALIGNMENT { LEFT, RIGHT, NONE }
//
//    private static float PAGE_WIDTH = new PDPage(PDRectangle.A4).getMediaBox().getWidth();
//    private static float PAGE_HEIGHT = new PDPage(PDRectangle.A4).getMediaBox().getHeight();
//    private static float regularFontSize = 10;
//    private static float mediumFontSize = 12;
//    private static float captionFontSize = 22;
//
//    private float lineOffSetY = 0;
//    private float lineOffSetX = 0;
//
//    private ColorValue divisionColor = new ColorValue(222,225,231);
    private Map<String, File> fontMap = new HashMap<>();
    private final PdfConfig pdfConfig;
    public PDDocument document;
    public PDPageContentStream contentStream;

    public PDFBoxService(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
    }

    @PostConstruct
    public void init() throws FileNotFoundException {
        File RUBIK_MEDIUM_FONT_FILE = ResourceUtils.getFile("classpath:rubik-medium.ttf");
        File RUBIK_REGULAR_FONT_FILE = ResourceUtils.getFile("classpath:rubik-regular.ttf");
        File RUBIK_BOLD_FONT_FILE = ResourceUtils.getFile("classpath:rubik-bold.ttf");
        fontMap.put("rubik-bold", RUBIK_BOLD_FONT_FILE);
        fontMap.put("rubik-medium", RUBIK_MEDIUM_FONT_FILE);
        fontMap.put("rubik-regular", RUBIK_REGULAR_FONT_FILE);
    }

    @SneakyThrows
    public void writeText(Float lineOffSetX, Float lineOffSetY, String message, String fontFamily, Float fontSize, ColorValue colorValue) {
        PDType0Font pdFont = PDType0Font.load(document, fontMap.get(fontFamily));
        contentStream.beginText();
        contentStream.setFont(pdFont, fontSize);
        contentStream.setStrokingColor(getColor(colorValue));
        contentStream.newLineAtOffset(lineOffSetX, lineOffSetY);
        contentStream.showText(message);
        contentStream.endText();
    }

    @SneakyThrows
    public Float calculateTextWidth(String message, PDFont font, Float fontSize) {
        return (font.getStringWidth(message) / 1000.0f) * fontSize;
    }

    public Color getColor(ColorValue colorValue) {
        if (Objects.nonNull(colorValue)) {
            return new Color(colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue());
        } else {
            return Color.BLACK;
        }
    }

    private void processComponent(Map.Entry<String, PDFModel> pdfModelEntry) {
        PDFModel pdfModel = pdfModelEntry.getValue();
        switch (pdfModel.getResourceType()) {
            case "text":
                writeText(pdfModel.getCoordinates().getOffSetX(), pdfModel.getCoordinates().getOffSetY(), pdfModel.getResourceValue(),
                        pdfModel.getFont().getFontFamily(), pdfModel.getFont().getFontSize(), pdfModel.getColor());
                break;
            case "image":
//                drawImage();
                break;
            case "table":
//                drawTable();
                break;
            default:
                System.out.println("ERROR");
        }
    }

    @SneakyThrows
    public void createPDF() {
        document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        pdfConfig.getComponents().entrySet().forEach(this::processComponent);
        document.save("example.pdf");
//                File trendyolPng = ResourceUtils.getFile("classpath:trendyol_logo.png");
//                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(trendyolPng, document);
//                contentStream.drawImage(pdImage, pageWidth-pdImageWidth-getPadding(PADDING.RIGHT), page.getMediaBox().getUpperRightY() - pdImageHeight - getPadding(PADDING.TOP),pdImageWidth,pdImageHeight);
//
//                this.lineOffSetY = page.getMediaBox().getUpperRightY() - pdImage.getHeight() - getPadding(PADDING.TOP);
//
//                this.lineOffSetY -= captionFontSize*2;
//
//                PDType0Font pdFontRubikBold = PDType0Font.load(document, RUBICK_BOLD_FONT_FILE);
//
//
//                contentStream.beginText();
//                contentStream.setFont(pdFontRubikBold, captionFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("Invoice");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*2;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("BittiBitiyor");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Invoice Number: TYF20212000000220",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Invoice Number: TYF20212000000220");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("060000 Üsküdar/istanbul");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize/2;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Invoice Number: TYF20212000000220",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Invoice Date : 12.05.2022");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*2;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("E-Posta : mehmetustundag@gmail.com");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Due Date : 29.05.2022",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Due Date : 29.05.2022");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*2;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("Telefon : 0 576 324 23 23");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("VAT Registration Date : TR98459357",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("VAT Registration Date : TR98459357");
//                contentStream.endText();
//
//
//                this.lineOffSetY -= regularFontSize*6;
//
//                contentStream.beginText();
//                contentStream.setNonStrokingColor(new ColorValue(255,215,100));
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*2;
//
//                // Build the table
//                Table myTable = Table.builder()
//                        .addColumnsOfWidth(75, 75, 75,75, 75, 75,75)
//                        .padding(1)
//                        .fontSize(12)
//                        .addRow(Row.builder()
//                                .padding(5)
//                                .backgroundColor(new ColorValue(247,246,251))
//                                .borderColor(divisionColor)
//                                .borderWidth(1)
//                                .borderStyle(BorderStyle.SOLID)
//                                .add(TextCell.builder().text("Aciklama").borderWidth(1).build())
//                                .add(TextCell.builder().text("Miktar").borderWidth(1).build())
//                                .add(TextCell.builder().text("Birim Fiyati").borderWidth(1).build())
//                                .add(TextCell.builder().text("Indirim").borderWidth(1).build())
//                                .add(TextCell.builder().text("KDV Orani").borderWidth(1).build())
//                                .add(TextCell.builder().text("Diger Vergiler").borderWidth(1).build())
//                                .add(TextCell.builder().text("Tutar").borderWidth(1).build())
//                                .build())
//
//                        .addRow(Row.builder()
//                                .padding(5)
//                                .add(TextCell.builder().text("Yagli Boya Tablo").borderWidth(1).build())
//                                .add(TextCell.builder().text("1 Adet").borderWidth(1).build())
//                                .add(TextCell.builder().text("80 TL").borderWidth(1).build())
//                                .add(TextCell.builder().text("20 TL").borderWidth(1).build())
//                                .add(TextCell.builder().text("%11").borderWidth(1).build())
//                                .add(TextCell.builder().text("%12").borderWidth(1).build())
//                                .add(TextCell.builder().text("500 TL").borderWidth(1).build())
//                                .build())
//                        .build();
////
////                // Set up the drawer
//                TableDrawer tableDrawer = TableDrawer.builder()
//                        .contentStream(contentStream)
//                        .startX(getPadding(PADDING.LEFT))
//                        .startY(this.lineOffSetY)
//                        .table(myTable)
//                        .build();
//
//                tableDrawer.draw();
//
//                this.lineOffSetY = tableDrawer.getFinalY() - regularFontSize*3;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Mal Hizmet Toplam Tutari    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Mal Hizmet Toplam Tutari    : 8989 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*1.5;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Toplam Iskonto    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Toplam Iskonto : 8989 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*1.5;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("KDV Matrahi    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("KDV Matrahi    : 8989 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*1.5;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Hesaplanan KDV (%8) :  102.00 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Hesaplanan KDV (%8) :  102.00 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*1.5;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Vergiler Dahl Toplam Tutar       :  66.12 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Vergiler Dahl Toplam Tutar       :  66.12 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*1.5;
//
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Ödenecek Tutar          :  234.12 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
//                        lineOffSetY);
//                contentStream.showText("Ödenecek Tutar          :  234.12 TL");
//                contentStream.endText();
//
//                this.lineOffSetY -= regularFontSize*2;
//
//                contentStream.beginText();
//                contentStream.setNonStrokingColor(ColorValue.ORANGE);
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, regularFontSize);
//                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
//                contentStream.showText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
//                contentStream.endText();
//
//                this.lineOffSetY = tableDrawer.getFinalY() - regularFontSize*2;
//
//                contentStream.setNonStrokingColor(new ColorValue(247,246,251));
//                contentStream.addRect(getPadding(PADDING.LEFT), getPadding(PADDING.BOTTOM) , pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT), 80);
//                contentStream.fill();
//
//
//                contentStream.setNonStrokingColor(divisionColor);
//                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4, getPadding(PADDING.BOTTOM) + 10,
//                        1, 60);
//                contentStream.fill();
//
//                contentStream.setNonStrokingColor(divisionColor);
//                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4*2, getPadding(PADDING.BOTTOM) + 10,
//                        1, 60);
//                contentStream.fill();
//
//                contentStream.setNonStrokingColor(divisionColor);
//                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4*3, getPadding(PADDING.BOTTOM) + 10,
//                        1, 60);
//                contentStream.fill();
//
//
//                contentStream.beginText();
//                contentStream.setNonStrokingColor(ColorValue.BLACK);
//                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
//                contentStream.newLineAtOffset( getPadding(PADDING.LEFT) + 12, getPadding(PADDING.BOTTOM) + 60);
//                contentStream.showText("TRENDYOL B.V");
//                contentStream.endText();
    }
}