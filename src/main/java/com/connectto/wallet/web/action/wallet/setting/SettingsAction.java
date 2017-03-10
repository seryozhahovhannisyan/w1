package com.connectto.wallet.web.action.wallet.setting;

import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.general.util.encryption.SHAHashEnrypt;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;

/**
 * Created by Serozh on 1/29/16.
 */
public class SettingsAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(SettingsAction.class.getSimpleName());

    private IWalletManager walletManager;
    private IWalletLoggerManager walletLoggerManager;
    private ResponseDto responseDto;

    private String password;
    private String newPassword;

    public String changePassword () {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        String wPassword = wallet.getPassword();

        if (Utils.isEmpty(password) || Utils.isEmpty(newPassword)) {
            writeLog(SettingsAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, "password = " + password + " newPassword = " + newPassword);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {

            password = SHAHashEnrypt.get_MD5_SecurePassword(password);
            newPassword = SHAHashEnrypt.get_MD5_SecurePassword(newPassword);
            if(!wPassword.equalsIgnoreCase(password)){
                writeLog(SettingsAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, " Entried password = " + password + " wallet Password = " + wPassword);
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
                return SUCCESS;
            }

            Wallet walletReset = new Wallet();
            walletReset.setUserId(wallet.getUserId());
            walletReset.setPassword(newPassword);

            walletManager.resetPassword(walletReset);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            writeLog(SettingsAction.class.getSimpleName(), user, wallet, ex, LogLevel.ERROR, LogAction.READ, "");
        }

        return SUCCESS;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }
}
