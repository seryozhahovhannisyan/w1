package com.connectto.wallet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.connectto.general.model.UserDto;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.Utils;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchase;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseTax;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

/**
 * Created by Seryozha on 11/15/2016.
 */
public class TransactionExporter {

    public static synchronized void export(TransactionPurchase transaction,
                                           File file) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        convertTransactionToPDF(transaction,  document, pdfWriter );
        // step 5
        document.close();
    }

    private static synchronized void convertTransactionToPDF(TransactionPurchase transaction, Document document, PdfWriter pdfWriter)
            throws DocumentException, IOException {

        String logoPath = TransactionExporterHelper.logoPath ;

        BaseColor colorTitle= new BaseColor(14, 168, 109);
        Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, colorTitle);

        BaseColor colorPersonTitle= new BaseColor(92, 92, 92);
        Font fontPersonTitle = new Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, colorPersonTitle);

        BaseColor colorCurrency= new BaseColor(10, 10, 10);
        Font fontCurrency = new Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, colorCurrency);

        BaseColor colorMsg= new BaseColor(48, 48, 48);
        Font fontMsg = new Font(Font.FontFamily.HELVETICA, 12f, Font.ITALIC, colorMsg);

        BaseColor colorAnchor= new BaseColor(14, 87, 172);
        Font fontAnchor = new Font(Font.FontFamily.HELVETICA, 12f, Font.ITALIC, colorAnchor);

        UserDto user = TransactionExporterHelper.getUserDto();
        String nameSurname = user != null ? user.getName() + " " + user.getSurname() : "Hollor";
        String priceAndCurrency = Utils.convertDoubleToViewString(0d) + " " + CurrencyType.AMD.getCode();

        String openedAt = Utils.toViewDate(transaction.getOpenedAt());
        String closedAt = Utils.toViewDate(transaction.getClosedAt());
        String now = Utils.toViewDate(new Date(System.currentTimeMillis()));

        String profileAnchor = TransactionExporterHelper.profileAnchor;

        String tState = "State";
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(tState, fontTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(20);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);

        Image logo = Image.getInstance(logoPath);
        logo.setAbsolutePosition(0, 0);
        logo.scaleAbsolute(150, 25);

        Chunk logoChunk = new Chunk(logo, 0, 0, true);
        logoChunk.setAnchor(PartitionLCP.getHost(1));

        PdfPTable logoTable = new PdfPTable(1);

        logoTable.setWidthPercentage(100);
        logoTable.setWidths(new int[]{1});
        logoTable.setSpacingBefore(20);
        logoTable.addCell(getCell(logoChunk, Element.ALIGN_LEFT, false));
        document.add(logoTable);

        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setGap(4);//7
        separator.setLineWidth(2);//3
        separator.setAlignment(Element.ALIGN_CENTER);
        Chunk linebreak = new Chunk(separator);

        document.add(linebreak);

        Image profile = Image.getInstance(profileAnchor);
        profile.scaleAbsolute(64, 64);

        PdfPTable nsPdfPTable = new PdfPTable(1);

        nsPdfPTable.setWidthPercentage(40);
        nsPdfPTable.setWidths(new int[]{2});

        nsPdfPTable.addCell(getCell(profile, Element.ALIGN_CENTER, false));
        nsPdfPTable.addCell(getCell(new Paragraph(""), Element.ALIGN_RIGHT, false));

        nsPdfPTable.addCell(getCell(new Paragraph(nameSurname, fontPersonTitle), Element.ALIGN_CENTER, false));
        nsPdfPTable.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.pdf.started.at") + "-" + openedAt, fontPersonTitle), Element.ALIGN_CENTER, false));
        if(closedAt != null){
            nsPdfPTable.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.pdf.closed.at") + "-" + closedAt, fontPersonTitle), Element.ALIGN_CENTER, false));
        }
        nsPdfPTable.addCell(getCell(new Paragraph(priceAndCurrency, fontCurrency), Element.ALIGN_CENTER, false));

        document.add(nsPdfPTable);
        document.add(linebreak);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
//        table.setWidths(new int[]{3, 1});
        table.setSpacingBefore(20);

        String fromTotalPrice = transaction.getWalletTotalPrice() != null ? Utils.convertDoubleToViewString(transaction.getWalletTotalPrice()) : "-";
        String fromTotalPriceCurrencyType = transaction.getWalletTotalPrice() != null ? transaction.getWalletTotalPriceCurrencyType().getCode() : "-";
        String productAmount = transaction.getWalletTotalPrice() != null ? Utils.convertDoubleToViewString(transaction.getWalletTotalPrice()) : "-";
        String productCurrencyType = transaction.getWalletTotalPrice() != null ? transaction.getWalletTotalPriceCurrencyType().getCode() : "-";

        TransactionPurchaseTax tax = null;//transaction.getFromTransactionProcess().getTax();

        String totalAmountTaxPrice = null;//tax.getTotalAmountTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTotalAmountTaxPrice()) : "-";
        String transferTaxPrice =null;// tax.getTransferTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTransferTaxPrice()) : "-";
        String transferExchangeTaxPrice = null;//tax.getTransferExchangeTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTransferExchangeTaxPrice()) : "-";

        table.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.exchange.label.Transfer.Total.debit")  + "      "), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));
        table.addCell(getCell(new Paragraph(fromTotalPrice + " " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));

        table.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_LEFT, false));
        table.addCell(getCell(new Paragraph(productAmount + " " + productCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.exchange.label.Exchange.fee")  + "      "), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));
        table.addCell(getCell(new Paragraph(totalAmountTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));

        table.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_LEFT, false));
        table.addCell(getCell(new Paragraph(transferTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(TransactionExporterHelper.getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));
        table.addCell(getCell(new Paragraph(transferExchangeTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false,new BaseColor(14, 168, 109)));

        document.add(table);

        document.add(linebreak);



        Paragraph msgParagraph = new Paragraph(transaction.getMessage(), fontMsg);
        msgParagraph.setAlignment(Element.ALIGN_LEFT);
        msgParagraph.setSpacingAfter(40);
        document.add(msgParagraph);

        createFooter(pdfWriter, document, now, transaction.getOrderCode());
    }

    private static synchronized PdfPCell getCell(Paragraph p, int horizontalAlignment, boolean hasBorder,BaseColor backgroundColor) {
        p.setAlignment(horizontalAlignment);
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.addElement(p);
        if(!hasBorder){
            cell.setBorder(Rectangle.NO_BORDER);
        }
        if(backgroundColor != null){
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    private static synchronized PdfPCell getCell(Paragraph p, int horizontalAlignment, boolean hasBorder ) {
        return getCell(p,horizontalAlignment,hasBorder, null);
    }

    private static synchronized PdfPCell getCell(Chunk p, int verticalAlignment, boolean hasBorder) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(verticalAlignment);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.addElement(p);
        if(!hasBorder){
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    private static synchronized PdfPCell getCell(Image p, int horizontalAlignment,boolean hasBorder) {
        p.setAlignment(horizontalAlignment);
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.addElement(p);
        if(!hasBorder){
            cell.setBorder(Rectangle.NO_BORDER);
        }

        return cell;
    }

    private static synchronized void createFooter(PdfWriter writer, Document document, String now, String orderCode) {
        BaseColor colorPersonTitle= new BaseColor(15, 15, 15);
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD, colorPersonTitle);
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(TransactionExporterHelper.getText("hollor.footer.Copyright"), ffont);
        Phrase nowPhrase = new Phrase(now, ffont);
        Phrase orderCodePhrase = new Phrase(orderCode, ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                orderCodePhrase,
                document.right() + document.rightMargin() - 50,
                document.bottom() + 50, 0);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                nowPhrase,
                document.right() + document.rightMargin()-50,
                document.bottom() + 30 , 0);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    private synchronized Chunk createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
        BarcodeEAN barcode = new BarcodeEAN();
        barcode.setCodeType(Barcode.EAN8);
        barcode.setCode(code);

        Chunk chunk = new Chunk(barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), 0, 0, true);
        return chunk;
    }
}
