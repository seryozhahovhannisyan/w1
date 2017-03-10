package com.connectto.wallet.web.action.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.UserDto;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.notification.MailContent;
import com.connectto.notification.MailException;
import com.connectto.notification.MailSender;
import com.connectto.wallet.dataaccess.service.ITransactionManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Transaction;
import com.connectto.wallet.model.wallet.TransactionData;
import com.connectto.wallet.model.wallet.TransactionTax;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 5/10/16.
 */

public class TransactionInfoAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(TransactionInfoAction.class.getSimpleName());

    private ITransactionManager transactionManager;
    private ResponseDto responseDto;

    private InputStream inputStream;
    private Long transactionId;
    private Long walletExchangeId;
    private String email;
    private String dataFileName;

    public String transactionInfoEmail() {
        responseDto.cleanMessages();
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        if(wallet == null || user == null ){
            writeLog(DisputeAction.class.getName(), null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            return SUCCESS;
        }

        if (transactionId == null || transactionId < 1) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.READ, "transactionId is empty");
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            responseDto.addMessage(getText("errors.internal.server"));
            return SUCCESS;
        }

        if (Utils.isEmpty(email)) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.READ, "email is empty");
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            responseDto.addMessage(getText("errors.required", new String[]{getText("label.user.email")}));
            return SUCCESS;
        }

        try {

            Transaction transaction = transactionManager.getById(transactionId);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("walletId", wallet.getId());
            params.put("id", transactionId);
            params.put("sendMoneyId", TransactionState.SEND_MONEY.getId());//11
            params.put("requestTransactionId", TransactionState.REQUEST_TRANSACTION.getId());//12
            int transactionType = 0;
            switch (transactionType) {
                case 1:
                    params.put("ttr", transactionType);
                    break;
                case 2:
                    params.put("tts", transactionType);
                    break;
                default:
                    params.put("tt", transactionType);
            }

            TransactionDto transactionDto =  transactionManager.getDtoById(params);

            String partitionDns = PartitionLCP.getDNS(user.getPartitionId());
            Partition partition = (Partition)session.get(ConstantGeneral.SESSION_PARTITION);
            String fromEmail = partition.getPartitionEmail();
            String fromEmailPassword = partition.getPartitionEmailPassword();
            String nameSurname =  String.format("%s %s", user.getName(), user.getSurname());

            StringWriter writer = new StringWriter();
            Template template = Velocity.getTemplate("transaction.vm", "UTF-8");
            VelocityContext context = new VelocityContext();

            context.put("Greeting", "Dear");
            context.put("Name", nameSurname);
            template.merge(context, writer);

            MailContent mailContent = new MailContent();
            mailContent.setEmailsTo(new String[]{email});
            mailContent.setSubject("Transaction Info::");
            mailContent.setContent(writer.toString());
            mailContent.setRecipientTypeTo();

            MailSender mailNotification = new MailSender();
            mailNotification.sendEmailFromConnectTo(mailContent, fromEmail, fromEmailPassword);

            responseDto.setStatus(ResponseStatus.SUCCESS);
            responseDto.addMessage( getText("wallet.transaction.info.email.success"));
        } catch (InternalErrorException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.READ, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            responseDto.addMessage(getText("errors.internal.server"));
            return SUCCESS;
        } catch (EntityNotFoundException e) {
            writeLog(DisputeAction.class.getName(), user, wallet, e, LogLevel.ERROR, LogAction.READ, "");
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
            responseDto.addMessage(getText("wallet.data.not.found"));
            return SUCCESS;
        } catch (MailException e) {
            writeLog(DisputeAction.class.getName(), user, wallet, e, LogLevel.ERROR, LogAction.READ, "");
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
            responseDto.addMessage(getText("wallet.data.not.found"));
            return SUCCESS;
        }

        return SUCCESS;
    }

    public String transactionInfoDownload() {

        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);

        if (user == null || wallet == null) {
            logger.error("session time expired");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            return "start";
        }

        dataFileName = "transaction.pdf";

        try {

            Transaction transaction = transactionManager.getById(transactionId);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("walletId", wallet.getId());
            params.put("id", transactionId);
            //params.put("disputeStateNY", -1);
            params.put("sendMoneyId", TransactionState.SEND_MONEY.getId());//11
            params.put("requestTransactionId", TransactionState.REQUEST_TRANSACTION.getId());//12
            int transactionType = 0;
            switch (transactionType) {
                case 1:
                    params.put("ttr", transactionType);
                    break;
                case 2:
                    params.put("tts", transactionType);
                    break;
                default:
                    params.put("tt", transactionType);
            }

            TransactionDto transactionDto =  transactionManager.getDtoById(params);

            File tempDir = ImageIO.getCacheDirectory();
            File file = File.createTempFile("tmp", dataFileName, tempDir);
            inputStream = new FileInputStream(file);

            // step 1
            Document document = new Document();
            // step 2
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            // step 3
            document.open();
            convertTransactionToPDF(partition, transaction, transactionDto, document, pdfWriter );
            // step 5
            document.close();
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (DocumentException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }

        return SUCCESS;
    }

    private  synchronized void convertTransactionToPDF(Partition partition, Transaction transaction, TransactionDto transactionDto, Document document, PdfWriter pdfWriter) throws DocumentException, IOException {

        int partitionId = partition.getId();
        long transactionId = transaction.getId();
        String contentPath = "";
        try {
            contentPath = getHttpServletRequest() != null ? Initializer.context.getRealPath("") : "https://www.hollor.com/wallet";
        } catch (NullPointerException e){
            contentPath = "https://www.hollor.com/wallet";
        }

        String logoPath = contentPath + "/img/wallet/logos/connecttotv_wallet.png";

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

        UserDto user = transactionDto.getUserDto();
        Integer walletSetupId = transactionDto.getWalletSetupId();
        String nameSurname = user != null ? user.getName() + " " + user.getSurname() : PartitionLCP.getDNS(partitionId);
        String priceAndCurrency = Utils.convertDoubleToViewString(transactionDto.getPrice()) + " " + transactionDto.getPriceCurrency().getCode();

        String openedAt = Utils.toViewDate(transaction.getOpenedAt());
        String closedAt = Utils.toViewDate(transaction.getClosedAt());
        String now = Utils.toViewDate(new Date(System.currentTimeMillis()));

        String profileAnchor =  partition.getPartitionServerUrl() + partition.getPartitionLogoDirectory() + partition.getLogoPath();
        if (user != null) {
            profileAnchor = contentPath + "/img/general/avatar/"+PartitionLCP.getDNS(partitionId)+"/1.png";
        }

        String tState = TransactionState.PENDING.getId() == transaction.getState().getId() ? getText("wallet.login.pending.transaction") : getText("wallet.login.complited.transaction");
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
        logoChunk.setAnchor(PartitionLCP.getHost(partitionId));

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
        nsPdfPTable.addCell(getCell(new Paragraph(getText("wallet.pdf.started.at") + "-" + openedAt, fontPersonTitle), Element.ALIGN_CENTER, false));
        if(closedAt != null){
            nsPdfPTable.addCell(getCell(new Paragraph(getText("wallet.pdf.closed.at") + "-" + closedAt, fontPersonTitle), Element.ALIGN_CENTER, false));
        }
        nsPdfPTable.addCell(getCell(new Paragraph(priceAndCurrency, fontCurrency), Element.ALIGN_CENTER, false));

        document.add(nsPdfPTable);
        document.add(linebreak);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setWidths(new int[]{3, 1});
        table.setSpacingBefore(20);

        String fromTotalPrice = transaction.getFromTotalPrice() != null ? Utils.convertDoubleToViewString(transaction.getFromTotalPrice()) : "-";
        String fromTotalPriceCurrencyType = transaction.getFromTotalPriceCurrencyType() != null ? transaction.getFromTotalPriceCurrencyType().getCode() : "-";
        String productAmount = transaction.getProductAmount() != null ? Utils.convertDoubleToViewString(transaction.getProductAmount()) : "-";
        String productCurrencyType = transaction.getProductCurrencyType() != null ? transaction.getProductCurrencyType().getCode() : "-";

        TransactionTax tax = transaction.getFromTransactionProcess().getTax();

        String totalAmountTaxPrice = tax.getTotalAmountTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTotalAmountTaxPrice()) : "-";
        String transferTaxPrice = tax.getTransferTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTransferTaxPrice()) : "-";
        String transferExchangeTaxPrice = tax.getTransferExchangeTaxPrice() != null ?  Utils.convertDoubleToViewString(tax.getTransferExchangeTaxPrice()) : "-";

        table.addCell(getCell(new Paragraph(getText("wallet.exchange.label.Transfer.Total.debit")  + "      "), Element.ALIGN_RIGHT, false));
        table.addCell(getCell(new Paragraph(fromTotalPrice + " " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_RIGHT, false));
        table.addCell(getCell(new Paragraph(productAmount + " " + productCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(getText("wallet.exchange.label.Exchange.fee")  + "      "), Element.ALIGN_RIGHT, false));
        table.addCell(getCell(new Paragraph(totalAmountTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_RIGHT, false));
        table.addCell(getCell(new Paragraph(transferTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false));

        table.addCell(getCell(new Paragraph(getText("wallet.exchange.label.Transfer.exchange.fee")  + "      "), Element.ALIGN_RIGHT, false));
        table.addCell(getCell(new Paragraph(transferExchangeTaxPrice +" " + fromTotalPriceCurrencyType), Element.ALIGN_LEFT, false));

        document.add(table);

        document.add(linebreak);

        List<TransactionData> transactionDatas = transaction.getTransactionDatas();
        if(!Utils.isEmpty(transactionDatas)){
            Paragraph attachmentParagraph = new Paragraph(getText("wallet.payment.label.Attach"), fontPersonTitle);
            attachmentParagraph.setAlignment(Element.ALIGN_CENTER);
            attachmentParagraph.setSpacingBefore(20);
            attachmentParagraph.setSpacingAfter(20);
            document.add(attachmentParagraph);

            PdfPTable dataPdfPTable = new PdfPTable(1);
            dataPdfPTable.setWidthPercentage(30);
            dataPdfPTable.setWidths(new int[]{1});

            for(TransactionData data : transactionDatas){

                String dataFileName = Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + transactionId + ConstantGeneral.FILE_SEPARATOR + data.getFileName();
                dataFileName = dataFileName.replaceAll("//", "/");
                dataFileName = dataFileName.replaceAll("\\\\", "/");

                File file = new File(dataFileName);
                if(file.exists()){
                    Paragraph dataParagraph = new Paragraph();
                    dataParagraph.setAlignment(Element.ALIGN_CENTER);
                    Chunk dataChunk = new Chunk(data.getFileName(), fontAnchor);
                    dataParagraph.add(dataChunk);
                    dataChunk.setAnchor("https://www.hollor.com/wallet/transaction_data_download.htm?dataFileName=" + data.getFileName() +"&transactionId=" + transactionId);
                    dataPdfPTable.addCell(getCell(dataChunk, Element.ALIGN_CENTER, false));
                }

            }
            document.add(dataPdfPTable);
            document.add(linebreak);
        }

        Paragraph msgParagraph = new Paragraph(transaction.getMessage(), fontMsg);
        msgParagraph.setAlignment(Element.ALIGN_LEFT);
        msgParagraph.setSpacingAfter(40);
        document.add(msgParagraph);

        createFooter(pdfWriter, document, now, transaction.getOrderCode());
    }

    private synchronized PdfPCell getCell(Paragraph p, int horizontalAlignment, boolean hasBorder) {
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

    private synchronized PdfPCell getCell(Chunk p, int verticalAlignment, boolean hasBorder) {
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

    private synchronized PdfPCell getCell(Image p, int horizontalAlignment,boolean hasBorder) {
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

    private synchronized void createFooter(PdfWriter writer, Document document, String now, String orderCode) {
        BaseColor colorPersonTitle= new BaseColor(15, 15, 15);
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD, colorPersonTitle);
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(getText("hollor.footer.Copyright"), ffont);
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

    public void setWalletExchangeId(String walletExchangeId) {
        try {
            this.walletExchangeId = Long.parseLong(walletExchangeId);
        } catch (Exception e) {
            this.walletExchangeId = -1L;
            logger.error("Incorrect walletExchange id ,  walletExchangeId=" + walletExchangeId);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        try {
            this.transactionId = Long.parseLong(transactionId);
        } catch (Exception e) {
            this.transactionId = -1L;
            logger.error("Incorrect transaction id ,  transactionId=" + transactionId);
        }
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTransactionManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
