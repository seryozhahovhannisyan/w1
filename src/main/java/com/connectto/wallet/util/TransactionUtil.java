package com.connectto.wallet.util;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.Utils;
import com.connectto.wallet.creditCard.common.soap.encodedTypes.TransactionResult;
import com.connectto.wallet.model.wallet.*;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.connectto.wallet.model.wallet.lcp.TransactionType;
import com.connectto.wallet.web.action.wallet.dto.TransactionReviewDto;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 2/15/16.
 */
public class TransactionUtil { 
    @Deprecated
    public static TransactionReviewDto convertToReviewDto(Transaction transaction) {

        if (transaction == null) {
            return null;
        }

        TransactionProcess transactionAction = null;
        TransactionState transactionState = null;
        String priceCurrencyTypeName = null;
        if(transaction.getFromTransactionProcess() != null){
            transactionAction = transaction.getFromTransactionProcess();
        } else {
            return null;
        }

        transactionState = transaction.getActionState();
        if(TransactionState.SEND_MONEY.getId() == transactionState.getId()){
            transactionAction = transaction.getFromTransactionProcess();
            priceCurrencyTypeName = transaction.getFromWallet().getCurrencyType().getCode();
        } else if(TransactionState.REQUEST_TRANSACTION.getId() == transactionState.getId()){
            transactionAction = transaction.getToTransactionProcess();
            priceCurrencyTypeName = transaction.getToWallet().getCurrencyType().getCode();
        } else {
            return null;
        }

        TransactionTax tax = transactionAction.getTax();
        Exchange exchange = transactionAction.getTotalAmountExchange();

        String selectedExchangeRate = exchange == null ? null : " 1 " + priceCurrencyTypeName + " = " +  exchange.getRateAmount()  + " " + exchange.getRateCurrency().getCode();
        //$(".act-res").html( ' 1 ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.setupExchangeRate.buy + dto.setupExchangeRate.toCurrency);
        String setupExchangeRate = null;//todo
        //$(".cntr-res-n .trt-1").html(dto.amount + ' ' + dto.selectedExchangeRate.toCurrency + ' = ' + dto.transferExchangedAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
        String transferPrice = transaction.getFromTotalPrice() + " " + priceCurrencyTypeName;
        //$(".cntr-res .tot-tr").html(dto.totalAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
        String totalAmount = transaction.getFromTotalPrice() + " " + priceCurrencyTypeName;;
        //(".cntr-res .trans").html(dto.transferExchangedAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
        String transferExchangedAmount = null;
        //$(".cntr-res .trans_cost").html(dto.totalFeesAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
        String totalFeesAmount = tax.getTotalAmountTaxPrice().toString() + priceCurrencyTypeName;
        //$(".det-tem .trans_fee").html(dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency );
        String transferAmount = null;
        //$(".det-tem .trans-exchange").html(dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency );
        String exchangeTransferAmount = null;
        //$(".det-tem .exchange_fee").html(dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency);
        String exchangeSetupAmount = null;

        //$(".pdes .tfd").html(dto.transferFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency);
        String detailTransferFee = null;
        //$(".pdes .tefd").html(dto.exchangeTransferFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency);
        String detailExchangeTransferFee = null;
        //$(".pdes .efd").html(dto.exchangeSetupFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency);
        String detailSetupFee = null;
        //$(".pdes .tcd").html(dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' + ' + dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' + ' + dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.totalFeesAmount + ' ' + dto.setupExchangeRate.oneCurrency );
        String detailTotalAmount = null;

        TransactionReviewDto reviewDto = new TransactionReviewDto();
        reviewDto.setSelectedExchangeRate(selectedExchangeRate);
        reviewDto.setSetupExchangeRate(setupExchangeRate);
        reviewDto.setTransferPrice(transferPrice);
        reviewDto.setTotalAmount(totalAmount);
        reviewDto.setTransferExchangedAmount(transferExchangedAmount);
        reviewDto.setTotalFeesAmount(totalFeesAmount);
        reviewDto.setTransferAmount(transferAmount);
        reviewDto.setExchangeTransferAmount(exchangeTransferAmount);
        reviewDto.setExchangeSetupAmount(exchangeSetupAmount);
        reviewDto.setDetailTransferFee(detailTransferFee);
        reviewDto.setDetailExchangeTransferFee(detailExchangeTransferFee);
        reviewDto.setDetailSetupFee(detailSetupFee);
        reviewDto.setDetailTotalAmount(detailTotalAmount);
        return reviewDto;
    }

    public static CreditCardTransactionResult convertToCreditCardTransactionResult(TransactionResult transactionResult){
        CreditCardTransactionResult result = new CreditCardTransactionResult();
        result.setTransactionError(transactionResult.isTransaction_Error());
        result.setTransactionApproved(transactionResult.isTransaction_Approved());
        result.setEXactRespCode(transactionResult.getEXact_Resp_Code());
        result.setEXactMessage(transactionResult.getEXact_Message());
        result.setBankRespCode(transactionResult.getBank_Resp_Code());
        result.setBankMessage(transactionResult.getBank_Message());
        result.setBankRespCode2(transactionResult.getBank_Resp_Code_2());
        result.setSequenceNo(transactionResult.getSequenceNo());
        result.setAVS(transactionResult.getAVS());
        result.setCVV2(transactionResult.getCVV2());
        result.setRetrievalRefNo(transactionResult.getRetrieval_Ref_No());
        result.setCAVVResponse(transactionResult.getCAVV_Response());
        result.setAmountRequested(transactionResult.getAmountRequested());
        result.setMerchantName(transactionResult.getMerchantName());
        result.setMerchantAddress(transactionResult.getMerchantAddress());
        result.setMerchantCity(transactionResult.getMerchantCity());
        result.setMerchantProvince(transactionResult.getMerchantProvince());
        result.setMerchantCountry(transactionResult.getMerchantCountry());
        result.setMerchantPostal(transactionResult.getMerchantPostal());
        result.setMerchantURL(transactionResult.getMerchantURL());
        result.setCurrentBalance(transactionResult.getCurrentBalance());
        result.setPreviousBalance(transactionResult.getPreviousBalance());
        result.setCTR(transactionResult.getCTR());
        return result;
    } 

}
