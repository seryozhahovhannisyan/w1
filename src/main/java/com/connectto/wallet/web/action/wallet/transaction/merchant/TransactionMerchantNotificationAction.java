package com.connectto.wallet.web.action.wallet.transaction.merchant;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionDepositManager;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDeposit;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionMerchantAction;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serozh on 1/27/2017.
 */
public class TransactionMerchantNotificationAction extends TransactionMerchantAction {

    private static final Logger logger = Logger.getLogger(TransactionMerchantNotificationAction.class.getSimpleName());

    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;
    private ITransactionDepositManager transactionDepositManager;
    private ITransactionWithdrawManager transactionWithdrawManager;
    private ResponseDto responseDto;

    public String viewLocked() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return getIsMobile() ? "m-error" : ERROR;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("finalState", TransactionState.PENDING.getId());
        params.put("walletId", wallet.getId());

        try {
            TransactionWithdraw withdraw = transactionWithdrawManager.getUniqueByParams(params);
            if (withdraw == null) {
                TransactionDeposit deposit = transactionDepositManager.getUniqueByParams(params);
                if (deposit != null) {
                    responseDto.addResponse("data", deposit);
                }
            } else {
                responseDto.addResponse("data", withdraw);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setWalletSetupManager(IWalletSetupManager walletSetupManager) {
        this.walletSetupManager = walletSetupManager;
    }

    public void setTransactionDepositManager(ITransactionDepositManager transactionDepositManager) {
        this.transactionDepositManager = transactionDepositManager;
    }

    public void setTransactionWithdrawManager(ITransactionWithdrawManager transactionWithdrawManager) {
        this.transactionWithdrawManager = transactionWithdrawManager;
    }

    @Override
    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
