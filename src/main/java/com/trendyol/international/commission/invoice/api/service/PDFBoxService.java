package com.trendyol.international.commission.invoice.api.service;

import com.itextpdf.io.util.ResourceUtil;
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
import org.springframework.core.io.ClassPathResource;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static org.vandeseer.easytable.structure.Row.RowBuilder;
import static org.vandeseer.easytable.structure.Row.builder;

@Service
public class PDFBoxService {
    private final Map<String, InputStream> fontMap = new HashMap<>();
    private final PdfConfig pdfConfig;
    public PDDocument document;
    public PDPageContentStream contentStream;
    public Map<String, PdfComponent> pdfModelMap;
    public Float pageHeight;

    public PDFBoxService(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
    }

    @PostConstruct
    public void init() {
        InputStream RUBIK_MEDIUM_FONT_FILE = ResourceUtil.getResourceStream("classpath:fonts/rubik-medium.ttf");
        InputStream RUBIK_REGULAR_FONT_FILE = ResourceUtil.getResourceStream("classpath:fonts/rubik-regular.ttf");
        InputStream RUBIK_BOLD_FONT_FILE = ResourceUtil.getResourceStream("classpath:fonts/rubik-bold.ttf");

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
