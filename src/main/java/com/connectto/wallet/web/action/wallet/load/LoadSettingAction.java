package com.connectto.wallet.web.action.wallet.load;

import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.web.action.wallet.dto.CurrencyTypeDto;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serozh on 2/21/16.
 */
public class LoadSettingAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(LoadSettingAction.class.getSimpleName());
    private IUserManager userManager;
    private IWalletManager walletManager;
    private ResponseDto responseDto;
    private String userId ;
    //getters
    private User user;
    private CurrencyTypeDto defaultCurrencyType;
    private List<CurrencyTypeDto> currencyTypes;


    public String loadAvailableCurrencies() {
        session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);
        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();
        if (wallet == null) {
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            Long  uId = Long.parseLong(this.userId);
            if (uId == null || uId == -1l) {
                logger.error( "invalid user id userId="+userId);
                responseDto.addMessage( "invalid user id userId="+userId);
                responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error("invalid user id userId=" + userId);
            responseDto.addMessage( "invalid user id userId="+userId);
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            initCurrencyTypes();
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }

        return SUCCESS;
    }

    private synchronized void initCurrencyTypes() throws EntityNotFoundException, InternalErrorException {
        User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        WalletSetup walletSetup = partition.getWalletSetup();
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        defaultCurrencyType = new CurrencyTypeDto(walletSetup.getCurrencyType());
        Long  uId = Long.parseLong(this.userId);
        user = null;//todo userManager.getById(uId);
        Wallet uWallet = walletManager.getByUserId(uId);
        currencyTypes = new ArrayList<CurrencyTypeDto>();

        currencyTypes.add(defaultCurrencyType);

        if (uWallet.getCurrencyType().getId() != defaultCurrencyType.getId()) {
            currencyTypes.add(new CurrencyTypeDto(uWallet.getCurrencyType()));
        }

        if (wallet.getCurrencyType().getId() != defaultCurrencyType.getId() &&
                wallet.getCurrencyType().getId() != uWallet.getCurrencyType().getId()) {
            currencyTypes.add(new CurrencyTypeDto(wallet.getCurrencyType()));
        }
    }

    public User getUser() {
        return user;
    }

    public CurrencyTypeDto getDefaultCurrencyType() {
        return defaultCurrencyType;
    }

    public List<CurrencyTypeDto> getCurrencyTypes() {
        return currencyTypes;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
