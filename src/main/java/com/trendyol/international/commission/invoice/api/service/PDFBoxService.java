package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.util.Font;
import com.trendyol.international.commission.invoice.api.util.*;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.vandeseer.easytable.structure.Row.RowBuilder;
import static org.vandeseer.easytable.structure.Row.builder;

@Service
public class PDFBoxService {
    private final Map<String, File> fontMap = new HashMap<>();
    private final PdfConfig pdfConfig;
    public PDDocument document;
    public PDPageContentStream contentStream;
    public Map<String,PDFModel> pdfModelMap;
    public Float pageHeight;

    public PDFBoxService(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
    }

    @PostConstruct
    public void init() throws FileNotFoundException {
        File RUBIK_MEDIUM_FONT_FILE = ResourceUtils.getFile("classpath:fonts/rubik-medium.ttf");
        File RUBIK_REGULAR_FONT_FILE = ResourceUtils.getFile("classpath:fonts/rubik-regular.ttf");
        File RUBIK_BOLD_FONT_FILE = ResourceUtils.getFile("classpath:fonts/rubik-bold.ttf");
        fontMap.put("rubik-bold", RUBIK_BOLD_FONT_FILE);
        fontMap.put("rubik-medium", RUBIK_MEDIUM_FONT_FILE);
        fontMap.put("rubik-regular", RUBIK_REGULAR_FONT_FILE);
    }

    public Color getColor(ColorValue colorValue) {
        if (Objects.nonNull(colorValue)) {
            return new Color(colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue());
        } else {
            return Color.BLACK;
        }
    }

    @SneakyThrows
    private PDFont getFont(Font font) {
        PDFont pdFont = PDType0Font.load(document, fontMap.get("rubik-regular"));
        if (Objects.nonNull(font)) {
            if (Objects.nonNull(font.getFontFamily())) {
                pdFont = PDType0Font.load(document, fontMap.get(font.getFontFamily()));
            }
        }
        return pdFont;
    }

    @SneakyThrows
    private Float getFontSize(Font font) {
        Float fontSize = 12F;
        if (Objects.nonNull(font)) {
            if (Objects.nonNull(font.getFontSize())) {
                fontSize = font.getFontSize();
            }
        }
        return fontSize;
    }

    @SneakyThrows
    private void processBeforeWrite(PDFModel pdfModel) {
        if(Objects.nonNull(pdfModel.getDependsOn())) {
            Float shiftY = pdfModelMap.get(pdfModel.getDependsOn()).getShiftValues().getOffSetY();
            Float shiftX = pdfModelMap.get(pdfModel.getDependsOn()).getShiftValues().getOffSetX();
            pdfModel.getCoordinates().setOffSetY(pdfModel.getCoordinates().getOffSetY() + shiftY );
            pdfModel.getCoordinates().setOffSetX(pdfModel.getCoordinates().getOffSetX() + shiftX );
        }
    }

    @SneakyThrows
    private void writeText(Float lineOffSetX, Float lineOffSetY, String message, Font font, ColorValue colorValue) {
        PDFont pdFont = getFont(font);
        Float fontSize = getFontSize(font);
        contentStream.beginText();
        contentStream.setFont(pdFont, fontSize);
        contentStream.setNonStrokingColor(getColor(colorValue));
        contentStream.newLineAtOffset(lineOffSetX, pageHeight - lineOffSetY - fontSize);
        contentStream.showText(message);
        contentStream.endText();
    }

    @SneakyThrows
    private void drawImage(Float lineOffSetX, Float lineOffSetY, String imageName, Integer width, Integer height) {
        File imageFile = ResourceUtils.getFile("classpath:" + imageName + ".png");
        PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imageFile, document);
        contentStream.drawImage(pdImage, lineOffSetX, pageHeight - lineOffSetY - height, width, height);
    }

    @SneakyThrows
    private void drawRect(Float lineOffSetX, Float lineOffSetY, ColorValue fillColor, Integer width, Integer height) {
        contentStream.setNonStrokingColor(getColor(fillColor));
        contentStream.addRect(lineOffSetX, pageHeight - lineOffSetY - height, width, height);
        contentStream.fill();
    }

    @SneakyThrows
    private void drawTable(Float lineOffSetX, Float lineOffSetY, Font font, TableInfo tableInfo, Coordinate shiftValues) {
        Color borderColor = getColor(tableInfo.getBorderColor());
        Color backgroundColor = getColor(tableInfo.getBackgroundColor());
        PDFont pdFont = getFont(font);
        Float fontSize = getFontSize(font);

        Table.TableBuilder tableBuilder = Table.builder();
        tableInfo.getColumnWidths().forEach((s, integer) -> tableBuilder.addColumnOfWidth(integer));
        tableBuilder.padding(1).fontSize(fontSize.intValue()).font(pdFont);
        //Header Row
        RowBuilder rowBuilder = builder().padding(5).backgroundColor(backgroundColor).borderColor(borderColor);
        tableInfo.getColumnNames().forEach((s, columnName) -> rowBuilder.add(TextCell.builder().text(columnName).borderWidth(1).build()));
        Table myTable = tableBuilder.addRow(rowBuilder.build()).build();
        TableDrawer tableDrawer = TableDrawer.builder()
                .contentStream(contentStream)
                .startX(lineOffSetX)
                .startY(pageHeight-lineOffSetY)
                .table(myTable)
                .build();

        tableDrawer.draw();
        shiftValues.setOffSetX(0f);
        shiftValues.setOffSetY(200f);
    }

    private void processComponent(Map.Entry<String, PDFModel> pdfModelEntry) {
        PDFModel pdfModel = pdfModelEntry.getValue();
        switch (pdfModel.getResourceType()) {
            case "text" -> {
                processBeforeWrite(pdfModel);
                writeText(pdfModel.getCoordinates().getOffSetX(),
                        pdfModel.getCoordinates().getOffSetY(),
                        pdfModel.getResourceValue(),
                        pdfModel.getFont(),
                        pdfModel.getColor());
                break;
            }
            case "image" -> drawImage(pdfModel.getCoordinates().getOffSetX(),
                    pdfModel.getCoordinates().getOffSetY(),
                    pdfModel.getResourceValue(),
                    pdfModel.getResourceWidth(),
                    pdfModel.getResourceHeight());
            case "table" -> drawTable(pdfModel.getCoordinates().getOffSetX(),
                    pdfModel.getCoordinates().getOffSetY(),
                    pdfModel.getFont(),
                    pdfModel.getTableInfo(),
                    pdfModel.getShiftValues());
            case "rect" -> drawRect(pdfModel.getCoordinates().getOffSetX(),
                    pdfModel.getCoordinates().getOffSetY(),
                    pdfModel.getColor(),
                    pdfModel.getResourceWidth(),
                    pdfModel.getResourceHeight()
            );
            default -> System.out.println("ERROR");
        }
    }

    @SneakyThrows
    public void createPDF() {
        document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        pageHeight = page.getMediaBox().getUpperRightY();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        pdfModelMap = pdfConfig.getComponents();
        // replace with REAL DATA
        pdfModelMap.entrySet().forEach(this::processComponent);
        contentStream.close();
        document.save(pdfConfig.getOutputFileName() + ".pdf");
    }
}

//
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
