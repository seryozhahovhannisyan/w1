package com.connectto.wallet.web.action.wallet.menu;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;

/**
 * Created by htdev001 on 9/3/14.
 */
public class WalletMenuAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(WalletMenuAction.class.getSimpleName());
    private IWalletManager walletManager;
    private ResponseDto responseDto;
    private String searchLike;
    private Wallet userWallet;

    private Long disputeId = 0L;
    private Long transactionId = 0L;

    public String execute() {
        setIsMobile();
        return getIsMobile() ? "m-success" : SUCCESS;
    }

    public String currentBalanceView() {
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (user == null) {
            logger.error("session time out");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            return SUCCESS;
        }
        try {
            userWallet = walletManager.getByUserId(user.getId());
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.DATA_NOT_FOUND);
        }

        return SUCCESS;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public Wallet getUserWallet() {
        return userWallet;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public String getSearchLike() {
        return searchLike;
    }

    public void setSearchLike(String searchLike) {
        this.searchLike = searchLike;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        try {
            this.disputeId = Long.parseLong(disputeId);
        } catch (Exception e) {
        }
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        try {
            this.transactionId = Long.parseLong(transactionId);
        } catch (Exception e) {
        }
    }
}
