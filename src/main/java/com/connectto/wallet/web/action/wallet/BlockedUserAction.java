package com.connectto.wallet.web.action.wallet;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.wallet.BlockedUser;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by htdev001 on 9/2/14.
 */
public class BlockedUserAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(BlockedUserAction.class.getSimpleName());

    private ResponseDto responseDto;
    private IWalletManager walletManager;
    private IWalletLoggerManager walletLoggerManager;

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    private String title;
    private String content;

    private long blockedId;
    private BlockedUser blockedUser;

    public String viewBlock() {
        return  getIsMobile() ? "m-success" : SUCCESS;
    }

    public String blockUser() {

        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        if (user == null || blockedId < 1 || Utils.isEmpty(title) || Utils.isEmpty(content)) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        Long ownerId = user.getId();
        Date currentDate = new Date(System.currentTimeMillis());

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setBlockedAt(currentDate);
        blockedUser.setOwnerId(ownerId);
        blockedUser.setBlockedId(blockedId);
        blockedUser.setTitle(title);
        blockedUser.setContent(content);

        try {
            walletManager.blockUser(blockedUser);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String unblockUser() {

        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        if (user == null ||  blockedId < 1 )  {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        Long ownerId = user.getId();
        Date currentDate = new Date(System.currentTimeMillis());

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setUnblockedAt(currentDate);
        blockedUser.setOwnerId(ownerId);
        blockedUser.setBlockedId(blockedId);

        try {
            walletManager.unblockUser(blockedUser);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }
        return SUCCESS;
    }

    /*
     *##################################################################################################################
     *Getters
     *##################################################################################################################
     */

    public BlockedUser getBlockedUser() {
        return blockedUser;
    }

    /*
     *##################################################################################################################
     *Setters
     *##################################################################################################################
     */

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBlockedId(String blockedId) {
        try {
            this.blockedId = Long.parseLong(blockedId);
        } catch (Exception e) {
            this.blockedId = 0L;
            logger.error("Incorrect transactionId ,  blockedId=" + blockedId);
        }
    }
}
