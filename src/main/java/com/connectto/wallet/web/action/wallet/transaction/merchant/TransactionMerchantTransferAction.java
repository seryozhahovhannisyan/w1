package com.connectto.wallet.web.action.wallet.transaction.merchant;
 
import com.connectto.general.dataaccess.service.ITsmManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.TsmCompany;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.transaction.merchant.IMerchantTransferTransactionManager;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.transaction.merchant.MerchantTransactionReviewDto;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTicket;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTransaction;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionBaseAction;
import com.connectto.wallet.web.dto.DataConverter;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransactionMerchantTransferAction extends TransactionBaseAction {

    private static final Logger logger = Logger.getLogger(TransactionMerchantTransferAction.class.getSimpleName());

    private IMerchantTransferTransactionManager merchantTransferTransactionManager;

    private ITsmManager tsmManager;

    private MerchantTransactionReviewDto transactionReviewDto;

    //TransferTicket
    private String merchantCompanyId;
    private String merchantTransactionId;
    private String name;
    private String description;
    //
    private String walletId;
    //
    private String secretKey;
    private String clientKey;

    public String transferFromMerchantToUser() {

        responseDto.cleanMessages();
        boolean decripted = true;

        Map<String, Object> signInParams = new HashMap<String, Object>();

        MerchantTransferTransaction transferTransaction = new MerchantTransferTransaction();
        transferTransaction.setActionDate(new Date(System.currentTimeMillis()));
        transferTransaction.setIsEncoded(decripted);

        try {

            if(!convertAmountAndCurrency(decripted)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            transferTransaction.setTransferAmount(productAmount);
            transferTransaction.setTransferAmountCurrencyType(productCurrencyType);

            MerchantTransferTicket ticket = initTransferTicket(decripted);
            signInParams.put("secretKey", ticket.getSecretKey());
            signInParams.put("clientKey", ticket.getClientKey());

            TsmCompany tsmCompany = tsmManager.signIn(signInParams);

            transferTransaction.setTransferTicket(ticket);
            transferTransaction.setWalletId(ticket.getWalletId());
            transferTransaction.setTsmCompanyId(tsmCompany.getId());
            transferTransaction.setTsmCompany(tsmCompany);

            merchantTransferTransactionManager.add(transferTransaction);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transferTransaction, true);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        } catch (EncryptException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.DATA_NOT_FOUND);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        }

        return SUCCESS;
    }

    private synchronized MerchantTransferTicket initTransferTicket(boolean decripted) throws InvalidParameterException, EncryptException, NumberFormatException {

        if (Utils.isEmpty(merchantCompanyId)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  merchantCompanyId is empty");
        }

        if (Utils.isEmpty(merchantTransactionId)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  merchantTransactionId is empty");
        }

        if (Utils.isEmpty(name)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  name is empty");
        }

        if (Utils.isEmpty(description)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  description is empty");
        }

        if (Utils.isEmpty(walletId)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  walletId is empty");
        }

        if (Utils.isEmpty(secretKey)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  secretKey is empty");
        }

        if (Utils.isEmpty(clientKey)) {
            throw new InvalidParameterException("Merchant Transaction Transfer  clientKey is empty");
        }

        MerchantTransferTicket ticket = new MerchantTransferTicket();
        ticket.setMerchantCompanyId(Long.parseLong(merchantCompanyId));
        ticket.setMerchantTransactionId(Long.parseLong(merchantTransactionId));
        ticket.setWalletId(Long.parseLong(walletId));
        ticket.setName(name);
        ticket.setDescription(description);
        ticket.setSecretKey(secretKey);
        ticket.setClientKey(clientKey);


        return ticket;
    }

    /*
     * #################################################################################################################
     * ########################################        GETTER & SETTER       ###########################################
     * #################################################################################################################
     */

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public MerchantTransactionReviewDto getTransactionReviewDto() {
        return transactionReviewDto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }    

    public void setMerchantCompanyId(String merchantCompanyId) {
        this.merchantCompanyId = merchantCompanyId;
    }

    public void setMerchantTransactionId(String merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public void setMerchantTransferTransactionManager(IMerchantTransferTransactionManager merchantTransferTransactionManager) {
        this.merchantTransferTransactionManager = merchantTransferTransactionManager;
    }

    public void setTsmManager(ITsmManager tsmManager) {
        this.tsmManager = tsmManager;
    }
}
