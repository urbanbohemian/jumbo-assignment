package com.trendyol.international.commission.invoice.api.service;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PDFBoxService {
    // LEFT,TOP,RIGHT,BOTTOM
    private final List<Float> paddingList = Arrays.asList(16.0f, 25.0f, 16.0f, 25.0f);

    private enum PADDING {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    public Float getPadding(PADDING padding) {
        return switch (padding) {
            case LEFT -> paddingList.get(0);
            case TOP -> paddingList.get(1);
            case RIGHT -> paddingList.get(2);
            case BOTTOM -> paddingList.get(3);
        };
    }

    private enum ALIGNMENT { LEFT, RIGHT, NONE }

    private static float PAGE_WIDTH = new PDPage(PDRectangle.A4).getMediaBox().getWidth();
    private static float PAGE_HEIGHT = new PDPage(PDRectangle.A4).getMediaBox().getHeight();
    private static float regularFontSize = 12;
    private static float captionFontSize = 26;

    private float lineOffSetY = 0;
    private float lineOffSetX = 0;

    private Color divisionColor = new Color(222,225,231);

    @SneakyThrows
    public Float calculateTextWidth(String message,PDType1Font font,Float fontSize) {
        return (font.getStringWidth(message) / 1000.0f) * fontSize;
    }

    @SneakyThrows
    public void writeText(PDPageContentStream contentStream,String message,ALIGNMENT alignment,PDType1Font fontType,Float fontSize,Color textColor) {
        float offSetX = this.lineOffSetX;
        float offSetY = this.lineOffSetY;
        if(ALIGNMENT.LEFT.compareTo(alignment) == 0) {
            offSetX = getPadding(PADDING.LEFT);
        } else if(ALIGNMENT.RIGHT.compareTo(alignment) == 0){
            offSetX = PAGE_WIDTH - calculateTextWidth(message,fontType,fontSize) - getPadding(PADDING.RIGHT);
        }


        contentStream.beginText();
        contentStream.setFont(fontType, fontSize);
        contentStream.setStrokingColor(Objects.nonNull(textColor) ? textColor : Color.BLACK );
        contentStream.newLineAtOffset(offSetX, PAGE_HEIGHT - getPadding(PADDING.TOP));
        contentStream.showText(message);
        contentStream.endText();
    }


    @PostConstruct
    public void createPDF() throws IOException {
        File RUBICK_MEDIUM_FONT_FILE = ResourceUtils.getFile("classpath:rubik-medium.ttf");
        File RUBICK_REGULAR_FONT_FILE = ResourceUtils.getFile("classpath:rubik-regular.ttf");
        File RUBICK_BOLD_FONT_FILE = ResourceUtils.getFile("classpath:rubik-bold.ttf");


        try (PDDocument document = new PDDocument()) {
            final PDPage page = new PDPage(PDRectangle.A4);
            System.out.println(PAGE_WIDTH);
            System.out.println(PAGE_HEIGHT);

            document.addPage(page);
            String message = "Invoice #: BV1";
            float fontSize = 24;
            float pageWidth = page.getMediaBox().getWidth();
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                writeText(contentStream,"Invoice #: BV1",ALIGNMENT.RIGHT,PDType1Font.HELVETICA,20f,null);
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, fontSize);
//                contentStream.setStrokingColor(Color.BLACK);
//                contentStream.newLineAtOffset(pageWidth - calculateTextWidth("Invoice #: BV1",PDType1Font.HELVETICA) - 10, page.getMediaBox().getHeight() - getPadding(PADDING.TOP));
//                contentStream.showText("Invoice #: BV1");
//                contentStream.endText();


                File trendyolPng = ResourceUtils.getFile("classpath:Logo.png");
                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(trendyolPng, document);
//                pdImage.setWidth(144);
//                pdImage.setHeight(48);
                contentStream.drawImage(pdImage, pageWidth-pdImage.getWidth()-getPadding(PADDING.RIGHT), page.getMediaBox().getUpperRightY() - pdImage.getHeight() - getPadding(PADDING.TOP));

                this.lineOffSetY = page.getMediaBox().getUpperRightY() - pdImage.getHeight() - getPadding(PADDING.TOP);

                this.lineOffSetY -= captionFontSize*2;

                PDType0Font pdFontRubickBold = PDType0Font.load(document, RUBICK_BOLD_FONT_FILE);


                contentStream.beginText();
                contentStream.setFont(pdFontRubickBold, captionFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("Invoice");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*2;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("BittiBitiyor");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Invoice Number: TYF20212000000220",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Invoice Number: TYF20212000000220");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("060000 Üsküdar/istanbul");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize/2;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Invoice Number: TYF20212000000220",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Invoice Date : 12.05.2022");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*2;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("E-Posta : mehmetustundag@gmail.com");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Due Date : 29.05.2022",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Due Date : 29.05.2022");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*2;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("Telefon : 0 576 324 23 23");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("VAT Registration Date : TR98459357",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("VAT Registration Date : TR98459357");
                contentStream.endText();


                this.lineOffSetY -= regularFontSize*6;

                contentStream.beginText();
                contentStream.setNonStrokingColor(new Color(255,215,100));
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*2;

                // Build the table
                Table myTable = Table.builder()
                        .addColumnsOfWidth(75, 75, 75,75, 75, 75,75)
                        .padding(1)
                        .fontSize(12)
                        .addRow(Row.builder()
                                .padding(5)
                                .backgroundColor(new Color(247,246,251))
                                .borderColor(divisionColor)
                                .borderWidth(1)
                                .borderStyle(BorderStyle.SOLID)
                                .add(TextCell.builder().text("Aciklama").borderWidth(1).build())
                                .add(TextCell.builder().text("Miktar").borderWidth(1).build())
                                .add(TextCell.builder().text("Birim Fiyati").borderWidth(1).build())
                                .add(TextCell.builder().text("Indirim").borderWidth(1).build())
                                .add(TextCell.builder().text("KDV Orani").borderWidth(1).build())
                                .add(TextCell.builder().text("Diger Vergiler").borderWidth(1).build())
                                .add(TextCell.builder().text("Tutar").borderWidth(1).build())
                                .build())

                        .addRow(Row.builder()
                                .padding(5)
                                .add(TextCell.builder().text("Yagli Boya Tablo").borderWidth(1).build())
                                .add(TextCell.builder().text("1 Adet").borderWidth(1).build())
                                .add(TextCell.builder().text("80 TL").borderWidth(1).build())
                                .add(TextCell.builder().text("20 TL").borderWidth(1).build())
                                .add(TextCell.builder().text("%11").borderWidth(1).build())
                                .add(TextCell.builder().text("%12").borderWidth(1).build())
                                .add(TextCell.builder().text("500 TL").borderWidth(1).build())
                                .build())
                        .build();
//
//                // Set up the drawer
                TableDrawer tableDrawer = TableDrawer.builder()
                        .contentStream(contentStream)
                        .startX(getPadding(PADDING.LEFT))
                        .startY(this.lineOffSetY)
                        .table(myTable)
                        .build();

                tableDrawer.draw();

                this.lineOffSetY = tableDrawer.getFinalY() - regularFontSize*3;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Mal Hizmet Toplam Tutari    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Mal Hizmet Toplam Tutari    : 8989 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*1.5;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Toplam Iskonto    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Toplam Iskonto : 8989 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*1.5;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("KDV Matrahi    : 8989 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("KDV Matrahi    : 8989 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*1.5;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Hesaplanan KDV (%8) :  102.00 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Hesaplanan KDV (%8) :  102.00 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*1.5;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Vergiler Dahl Toplam Tutar       :  66.12 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Vergiler Dahl Toplam Tutar       :  66.12 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*1.5;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( PAGE_WIDTH - calculateTextWidth("Ödenecek Tutar          :  234.12 TL",PDType1Font.HELVETICA,regularFontSize) - getPadding(PADDING.RIGHT),
                        lineOffSetY);
                contentStream.showText("Ödenecek Tutar          :  234.12 TL");
                contentStream.endText();

                this.lineOffSetY -= regularFontSize*2;

                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.ORANGE);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, regularFontSize);
                contentStream.newLineAtOffset(getPadding(PADDING.LEFT), lineOffSetY);
                contentStream.showText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                contentStream.endText();

                this.lineOffSetY = tableDrawer.getFinalY() - regularFontSize*2;

                contentStream.setNonStrokingColor(new Color(247,246,251));
                contentStream.addRect(getPadding(PADDING.LEFT), getPadding(PADDING.BOTTOM) , pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT), 80);
                contentStream.fill();


                contentStream.setNonStrokingColor(divisionColor);
                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4, getPadding(PADDING.BOTTOM) + 10,
                        1, 60);
                contentStream.fill();

                contentStream.setNonStrokingColor(divisionColor);
                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4*2, getPadding(PADDING.BOTTOM) + 10,
                        1, 60);
                contentStream.fill();

                contentStream.setNonStrokingColor(divisionColor);
                contentStream.addRect(getPadding(PADDING.LEFT)+(pageWidth-getPadding(PADDING.LEFT)-getPadding(PADDING.RIGHT))/4*3, getPadding(PADDING.BOTTOM) + 10,
                        1, 60);
                contentStream.fill();


                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.HELVETICA, regularFontSize);
                contentStream.newLineAtOffset( getPadding(PADDING.LEFT) + 12, getPadding(PADDING.BOTTOM) + 60);
                contentStream.showText("TRENDYOL B.V");
                contentStream.endText();

            }

            document.save("example.pdf");
        }
    }


}
