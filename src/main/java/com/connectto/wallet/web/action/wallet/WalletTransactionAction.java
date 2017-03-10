package com.connectto.wallet.web.action.wallet;

import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.ITransactionManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.*;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.util.TransactionShellAction;
import com.connectto.wallet.util.TransactionUtil;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;
import com.connectto.wallet.web.action.wallet.dto.TransactionReviewDto;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created by Serozh on 2/15/16.
 */
public class WalletTransactionAction extends TransactionShellAction {


    private static final Logger logger = Logger.getLogger(WalletTransactionAction.class.getSimpleName());
    private IUserManager userManager;
    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;
    private ITransactionManager transactionManager;

    private ResponseDto responseDto;
    private String paymentFeeMsg;
    //transaction payment details
    private String walletId;

    private String userId;
    private String amount;
    private String currencyType;
    private String message;
    //load
    private Integer currentPage;
    private Integer isLast;
    private String orderType;
    private List<TransactionDto> pendingTransactions;
    private List<TransactionDto> completedTransactions;
    private List<WalletExchange> walletExchanges;
    private WalletExchange previewExchange;
    //

    private Transaction preview;
    private TransactionReviewDto reviewDto;
    private TransactionNotifier transactionNotifier;
    //
    private String partitionId;
    private String orderCode;
    //
    private CurrencyType exchangeCurrencyType = null;
    private WalletExchange walletExchange;

    /*Send Money*/

    public String sendMoneyCheckTax() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {
            Wallet toWallet = walletManager.getByUserId(Long.parseLong(userId));
            preview = createTransaction(productAmount, productCurrencyType, myWallet, toWallet, walletSetup, TransactionState.SEND_MONEY);
            paymentFeeMsg = getText("wallet.send.money.check.tax.callback", new String[]{
                    preview.getFromTotalPrice().toString(), preview.getFromTotalPriceCurrencyType().getCode(),
                    preview.getToTotalPrice().toString(), preview.getToTotalPriceCurrencyType().getCode()});
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }


    @Deprecated
    public String sendMoneyPreview() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {
            Wallet toWallet = walletManager.getByUserId(Long.parseLong(userId));
            preview = createTransaction(productAmount, productCurrencyType, myWallet, toWallet, walletSetup, TransactionState.SEND_MONEY);
            reviewDto = TransactionUtil.convertToReviewDto(preview);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String sendMoneyMakePayment() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        responseDto.cleanMessages();
        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {

            Wallet toWallet = walletManager.getByUserId(Long.parseLong(userId));
            Transaction transaction = createTransaction(productAmount, productCurrencyType, myWallet, toWallet, walletSetup, TransactionState.SEND_MONEY);
            List<TransactionData> transactionDatas = (List<TransactionData>) session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);
            transaction.setTransactionDatas(transactionDatas);
            transaction.setMessage(message);

            transactionManager.add(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String sendMoneyApprove() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        try {

            Transaction transaction = transactionManager.getById(tId);
            if (TransactionState.PENDING.getId() != transaction.getState().getId()) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (TransactionState.SEND_MONEY.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getFromWalletId())) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            transactionManager.approve(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }

        return SUCCESS;
    }

    public String sendMoneyReject() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        try {

            Transaction transaction = transactionManager.getById(tId);
            if (TransactionState.PENDING.getId() != transaction.getState().getId()) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The reject action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionState state = null;
            if (TransactionState.SEND_MONEY.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getFromWalletId())) {
                state = TransactionState.CANCEL;
            } else if (TransactionState.REQUEST_TRANSACTION.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getToWalletId())) {
                state = TransactionState.CANCEL;
            } else {
                state = TransactionState.REJECTED;
            }

            transactionManager.rejectOrCancel(transaction, state);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }

        return SUCCESS;
    }

    /*Request Transaction*/

    public String requestTransactionCheckTax() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {
            Wallet toWallet = walletManager.getByUserId(Long.parseLong(userId));
            preview = createTransaction(productAmount, productCurrencyType, toWallet, myWallet, walletSetup, TransactionState.REQUEST_TRANSACTION);
            paymentFeeMsg = getText("wallet.action.check.payment.fee.unequal.currencyType", new String[]{
                    productCurrencyType.getName(), partition.getName(), walletSetup.getCurrencyType().getName(),
                    walletSetup.getTransferFeePercent().toString(), walletSetup.getTransferMinFee().toString()});
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    @Deprecated
    public String requestTransactionPreview() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {
            Wallet toWallet = walletManager.getByUserId(Long.parseLong(userId));
            Transaction transaction = createTransaction(productAmount, productCurrencyType, toWallet, myWallet, walletSetup, TransactionState.REQUEST_TRANSACTION);
            reviewDto = TransactionUtil.convertToReviewDto(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String requestTransactionMakePayment() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        responseDto.cleanMessages();
        boolean valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();


        try {
            Wallet fromWallet = walletManager.getByUserId(Long.parseLong(userId));
            Transaction transaction = createTransaction(productAmount, productCurrencyType, fromWallet, myWallet, walletSetup, TransactionState.REQUEST_TRANSACTION);
            List<TransactionData> transactionDatas = (List<TransactionData>) session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);
            transaction.setTransactionDatas(transactionDatas);
            transaction.setMessage(message);
            transactionManager.add(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String requestTransactionApprove() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        try {

            Transaction transaction = transactionManager.getById(tId);
            if (TransactionState.PENDING.getId() != transaction.getState().getId()) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (TransactionState.REQUEST_TRANSACTION.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getToWalletId())) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            transactionManager.approve(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }

        return SUCCESS;
    }

    public String requestTransactionReject() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        try {

            Transaction transaction = transactionManager.getById(tId);
            if (TransactionState.PENDING.getId() != transaction.getState().getId()) {
                writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "The reject action not allowed for current transaction transaction id = " + transaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionState state = null;
            if (TransactionState.SEND_MONEY.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getToWalletId())) {
                state = TransactionState.CANCEL;
            } else if (TransactionState.REQUEST_TRANSACTION.getId() == transaction.getActionState().getId() && myWallet.getId().equals(transaction.getFromWalletId())) {
                state = TransactionState.CANCEL;
            } else {
                state = TransactionState.REJECTED;
            }

            transactionManager.rejectOrCancel(transaction, state);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }

        return SUCCESS;
    }

    /*Transaction Notifier*/
    @Deprecated
    public String loadTransactionNotifier() {

        //Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        //User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);

        /*if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }*/
        Date currentDate = new Date(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<String, Object>();
        prepareNotifierCriteria(params);
        //must be exists
        params.put("currentDate", currentDate);
        params.put("walletId", walletId);

        try {
            transactionNotifier = transactionManager.getTransactionNotifier(params);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    /*load transactions*/

    public String loadPendingTransactions() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        int paginationCount = Initializer.getPaginationCount();
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (currentPage < 2) {
            currentPage = 1;
        }

        if(Utils.isEmpty(orderType)){
            orderType = "desc";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        prepareCriteria(params);

        params.put("limit", paginationCount);
        params.put("offset", (currentPage - 1) * paginationCount);
        params.put("walletId", myWallet.getId());
        params.put("orderType_" + orderType, orderType);

        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});

        try {
            pendingTransactions = transactionManager.getDtosByParams(params);
            if (Utils.isEmpty(pendingTransactions) || pendingTransactions.size() < paginationCount) {
                isLast = 1;
            } else {
                currentPage++;
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("wallet.data.not.found"));
            responseDto.setStatus(ResponseStatus.DATA_NOT_FOUND);
        }

        return SUCCESS;
    }

    public String loadCompletedTransactions() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        int paginationCount = Initializer.getPaginationCount();
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (currentPage < 2) {
            currentPage = 1;
        }

        if(Utils.isEmpty(orderType)){
            orderType = "desc";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        prepareCriteria(params);
        params.put("limit", paginationCount);
        params.put("offset", (currentPage - 1) * paginationCount);
        params.put("walletId", myWallet.getId());
        params.put("orderType_" + orderType, orderType);

        params.put("strictStates", new Integer[]{
                TransactionState.APPROVED.getId(),//13
                TransactionState.CANCEL.getId(),//5
                TransactionState.REJECTED.getId(),//14
                TransactionState.CHARGE_AMOUNT.getId()//10
        });


        try {

            completedTransactions = transactionManager.getDtosByParams(params);
            if (Utils.isEmpty(completedTransactions) || completedTransactions.size() < paginationCount) {
                isLast = 1;
            } else {
                currentPage++;
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("wallet.data.not.found"));
            responseDto.setStatus(ResponseStatus.DATA_NOT_FOUND);
        }

        return SUCCESS;
    }

    public String loadExchangedTransactions() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        int paginationCount = Initializer.getPaginationCount();
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (currentPage < 2) {
            currentPage = 1;
        }

        if(Utils.isEmpty(orderType)){
            orderType = "desc";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        prepareCriteria(params);
        params.put("limit", paginationCount);
        params.put("offset", (currentPage - 1) * paginationCount);
        params.put("walletId", myWallet.getId());
        params.put("orderType_" + orderType, orderType);

        try {

            walletExchanges = transactionManager.getWalletExchangesByParams(params);
            if (Utils.isEmpty(completedTransactions) || completedTransactions.size() < paginationCount) {
                isLast = 1;
            } else {
                currentPage++;
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String showPendingTransaction() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        Map<String, Object> params = new HashMap<String, Object>();
        prepareCriteria(params);

        params.put("walletId", myWallet.getId());
        params.put("transactionId"  , tId);

        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});

        try {
            preview = transactionManager.getById(tId);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String showCompletedTransaction() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        boolean valid = validateTransactionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long tId = Long.parseLong(transactionId);

        try {

            preview = transactionManager.getById(tId);
            List<TransactionData> transactionDatas = preview.getTransactionDatas();
            List<TransactionData> transactionDataExists = new ArrayList<TransactionData>();
            for(TransactionData data : transactionDatas){
                String dataFileName = Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + transactionId + ConstantGeneral.FILE_SEPARATOR + data.getFileName();
                dataFileName = dataFileName.replaceAll("//", "/");
                dataFileName = dataFileName.replaceAll("\\\\", "/");

                File file = new File(dataFileName);
                if(file.exists()){
                    transactionDataExists.add(data);
                }
                preview.setTransactionDatas(transactionDataExists);
            }

            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String showExchangedTransaction() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        boolean valid = validateWalletExchangedId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Long weId = Long.parseLong(walletExchangeId);

        try {

            previewExchange = transactionManager.getWalletExchangesById(weId);

            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String exchangeBalanceCheckTax() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null || sesUser == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (exchangeCurrencyType == null) {
            writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "Empty exchangeCurrencyType ");
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        WalletSetup walletSetup = partition.getWalletSetup();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("walletId", myWallet.getId());
        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});

        try {

            List<Transaction> transactions = transactionManager.getSampleTransactionsByParams(params);
            walletExchange = createWalletExchange(exchangeCurrencyType,  myWallet,  walletSetup,  transactions);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String exchangeBalance() {

        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet myWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (myWallet == null || sesUser == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (exchangeCurrencyType == null) {
            writeLog(sesUser, myWallet, null, LogLevel.ERROR, LogAction.READ, "Empty exchangeCurrencyType ");
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        WalletSetup walletSetup = partition.getWalletSetup();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("walletId", myWallet.getId());
        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});

        try {

            List<Transaction> transactions = transactionManager.getSampleTransactionsByParams(params);
            walletExchange = createWalletExchange(exchangeCurrencyType,  myWallet,  walletSetup,  transactions);

            transactionManager.exchangeWalletBalance(walletExchange);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, myWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    private synchronized boolean validateParams() {

        boolean valid = true;

        if (Utils.isEmpty(amount)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.amount");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(userId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.userId");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(currencyType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.currencyType");
            responseDto.addMessage(msg);
        }

        if (!valid) {
            return valid;
        }

        try {
            Double.parseDouble(amount);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.currencyType")+" ,"+getText("wallet.payment.label.Amount") +"="+ amount;
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(userId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.userId")+" ,"+getText("wallet.back.end.message.userId") +"="+ userId;
            userId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {
            int type = Integer.parseInt(currencyType);
            CurrencyType.valueOf(type);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType")+" ,"+getText("wallet.back.end.message.currencyType") +"="+ currencyType;
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);

        }
        return valid;
    }

    private synchronized boolean validateAmountAndCurrency() {

        boolean valid = true;

        if (Utils.isEmpty(amount)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.amount");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(currencyType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.currencyType");
            responseDto.addMessage(msg);
        }

        if (!valid) {
            return valid;
        }

        try {
            Double.parseDouble(amount);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.currencyType")+" ,"+getText("wallet.payment.label.Amount") +"="+ amount;
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        try {
            int type = Integer.parseInt(currencyType);
            CurrencyType.valueOf(type);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType")+" ,"+getText("wallet.back.end.message.currencyType") +"="+ currencyType;
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);

        }
        return valid;
    }

    private synchronized boolean validateTransactionId() {

        boolean valid = true;

        if (Utils.isEmpty(transactionId)) {
            valid = false;
            String msg = getText("wwallet.back.end.message.empty.transactionId");
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(transactionId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.transactionId")+" ,"+getText("wallet.back.end.message.transactionId") +"="+ transactionId;
            transactionId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    private synchronized boolean validateWalletExchangedId() {

        boolean valid = true;

        if (Utils.isEmpty(walletExchangeId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.walletExchangeId");
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(walletExchangeId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.walletExchangeId")+" ,"+getText("wallet.back.end.message.walletExchangeId") +"="+ walletExchangeId;
            walletExchangeId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    private synchronized boolean validateUserId() {

        boolean valid = true;

        if (Utils.isEmpty(userId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.userId");
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(userId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.transactionId")+" ,"+getText("wallet.back.end.message.userId") +"="+ userId;
            userId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    private synchronized boolean validatePartitionId() {

        boolean valid = true;

        if (Utils.isEmpty(partitionId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.partitionId");
            responseDto.addMessage(msg);
        }

        try {
            Integer.parseInt(partitionId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.partitionId")+" ,"+getText("wallet.back.end.message.userId") +"="+ partitionId;
            partitionId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public String getPaymentFeeMsg() {
        return paymentFeeMsg;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public String getOrderType() {
        return orderType;
    }

    public List<TransactionDto> getPendingTransactions() {
        return pendingTransactions;
    }

    public List<TransactionDto> getCompletedTransactions() {
        return completedTransactions;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public TransactionReviewDto getReviewDto() {
        return reviewDto;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurrentPage(String currentPage) {
        try {
            this.currentPage = Integer.parseInt(currentPage);
        } catch (Exception e) {
            this.currentPage = -1;
            logger.error(String.format("Incorrect current Page ,  page[%s]=", currentPage));
        }

    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public TransactionNotifier getTransactionNotifier() {
        return transactionNotifier;
    }

    public Transaction getPreview() {
        return preview;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public void setExchangeCurrencyType(String ct) {
        try {
            int cType = Integer.parseInt(ct);
            this.exchangeCurrencyType = CurrencyType.valueOf(cType);
        } catch (Exception e){

        }
    }

    public WalletExchange getWalletExchange() {
        return walletExchange;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WalletExchange> getWalletExchanges() {
        return walletExchanges;
    }

    public WalletExchange getPreviewExchange() {
        return previewExchange;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setWalletSetupManager(IWalletSetupManager walletSetupManager) {
        this.walletSetupManager = walletSetupManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setTransactionManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
