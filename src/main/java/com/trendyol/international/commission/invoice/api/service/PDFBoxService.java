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
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static org.vandeseer.easytable.structure.Row.RowBuilder;
import static org.vandeseer.easytable.structure.Row.builder;

@Service
public class PDFBoxService {
    private final Map<String, File> fontMap = new HashMap<>();
    private final PdfConfig pdfConfig;
    public PDDocument document;
    public PDPageContentStream contentStream;
    public Map<String, PdfComponent> pdfModelMap;
    public Float pageHeight;

    public PDFBoxService(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
    }

    @PostConstruct
    public void init() throws FileNotFoundException {
        File RUBIK_MEDIUM_FONT_FILE = ResourceUtils.getFile("classpath:ttf/rubik-medium.ttf");
        File RUBIK_REGULAR_FONT_FILE = ResourceUtils.getFile("classpath:ttf/rubik-regular.ttf");
        File RUBIK_BOLD_FONT_FILE = ResourceUtils.getFile("classpath:ttf/rubik-bold.ttf");
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
    private void processBeforeWrite(PdfComponent pdfComponent) {
        if(Objects.nonNull(pdfComponent.getDependsOn())) {
            Float shiftY = pdfModelMap.get(pdfComponent.getDependsOn()).getShiftValues().getOffSetY();
            Float shiftX = pdfModelMap.get(pdfComponent.getDependsOn()).getShiftValues().getOffSetX();
            pdfComponent.getCoordinates().setOffSetY(pdfComponent.getCoordinates().getOffSetY() + shiftY );
            pdfComponent.getCoordinates().setOffSetX(pdfComponent.getCoordinates().getOffSetX() + shiftX );
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
//        tableBuilder.fontSize(fontSize.intValue()).font(pdFont);
        List<Row> rows = new ArrayList<>();
        //Header Row
        RowBuilder headerRowBuilder =
                builder().settings(Settings.builder().paddingTop(11f).paddingBottom(11f).fontSize(fontSize.intValue()).font(pdFont).build()).backgroundColor(backgroundColor).borderColor(borderColor);
        tableInfo.getColumnNames().forEach((s, columnName) -> headerRowBuilder.add(TextCell.builder().paddingLeft(5).text(columnName).borderWidth(1).build()));
        rows.add(headerRowBuilder.build());

        //Dummy Data
        CommissionInvoiceLineItem tableLineItem1 = CommissionInvoiceLineItem.builder().description("Commission Fee").quantity(1).unit("Item").unitPrice(100.00f).vatRate(21f).amount(121.00f).build();
        CommissionInvoiceLineItem tableLineItem2 = CommissionInvoiceLineItem.builder().description("Commission Fee 2").quantity(3).unit("Item").unitPrice(123.00f).vatRate(30f).amount(300.00f).build();
        CommissionInvoiceLineItem tableLineItem3 = CommissionInvoiceLineItem.builder().description("Commission Fee 2").quantity(3).unit("Item").unitPrice(123.00f).vatRate(30f).amount(300.00f).build();
        CommissionInvoiceLineItem tableLineItem4 = CommissionInvoiceLineItem.builder().description("Commission Fee 2").quantity(3).unit("Item").unitPrice(123.00f).vatRate(30f).amount(300.00f).build();
        CommissionInvoiceLineItem tableLineItem5 = CommissionInvoiceLineItem.builder().description("Commission Fee 2").quantity(3).unit("Item").unitPrice(123.00f).vatRate(30f).amount(300.00f).build();
        List<CommissionInvoiceLineItem> tableLineItemList = List.of(tableLineItem1,tableLineItem2,tableLineItem3,tableLineItem4,tableLineItem5);

        Font genericRowFont = FontBuilder.aFont().fontFamily("rubik-regular").fontSize(10f).build();
        //
        //Row
        tableLineItemList.forEach(commissionInvoiceLineItem -> {
            RowBuilder genericRowBuilder =
                    builder().settings(Settings.builder().paddingTop(4f).paddingBottom(4f).fontSize(getFontSize(genericRowFont).intValue()).font(getFont(genericRowFont)).build()).borderColor(borderColor);
            commissionInvoiceLineItem.getCellValues().forEach(cellValue -> genericRowBuilder.add(TextCell.builder().paddingLeft(5).text(cellValue).borderWidth(1).build()));
            rows.add(genericRowBuilder.build());
        });

        rows.forEach(tableBuilder::addRow);
        TableDrawer tableDrawer = TableDrawer.builder()
                .contentStream(contentStream)
                .startX(lineOffSetX)
                .startY(pageHeight-lineOffSetY)
                .table(tableBuilder.build())
                .build();
        tableDrawer.draw();

        shiftValues.setOffSetX(0f);
        shiftValues.setOffSetY((tableLineItemList.size() - 1) * 10f);
    }

    private void processComponent(Map.Entry<String, PdfComponent> pdfModelEntry) {
        PdfComponent pdfComponent = pdfModelEntry.getValue();
        switch (pdfComponent.getResourceType()) {
            case "text" -> {
                processBeforeWrite(pdfComponent);
                writeText(pdfComponent.getCoordinates().getOffSetX(),
                        pdfComponent.getCoordinates().getOffSetY(),
                        pdfComponent.getResourceValue(),
                        pdfComponent.getFont(),
                        pdfComponent.getColor());
                break;
            }
            case "image" -> drawImage(pdfComponent.getCoordinates().getOffSetX(),
                    pdfComponent.getCoordinates().getOffSetY(),
                    pdfComponent.getResourceValue(),
                    pdfComponent.getResourceWidth(),
                    pdfComponent.getResourceHeight());
            case "table" -> drawTable(pdfComponent.getCoordinates().getOffSetX(),
                    pdfComponent.getCoordinates().getOffSetY(),
                    pdfComponent.getFont(),
                    pdfComponent.getTableInfo(),
                    pdfComponent.getShiftValues());
            case "rect" -> drawRect(pdfComponent.getCoordinates().getOffSetX(),
                    pdfComponent.getCoordinates().getOffSetY(),
                    pdfComponent.getColor(),
                    pdfComponent.getResourceWidth(),
                    pdfComponent.getResourceHeight()
            );
            default -> System.out.println("ERROR");
        }
    }

    @SneakyThrows
    public void createPDF(Map<String, PdfComponent> pdfModelMap) {
        document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        pageHeight = page.getMediaBox().getUpperRightY();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        this.pdfModelMap = pdfModelMap;
        this.pdfModelMap.entrySet().forEach(this::processComponent);
        contentStream.close();
        document.save(pdfConfig.getOutputFileName() + ".pdf");
        System.out.println("FINISHED");
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
