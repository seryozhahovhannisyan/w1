package com.connectto.wallet.web.action.wallet.transaction.transfer;

import com.connectto.general.dataaccess.service.ICoreSystemAdminManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.CoreSystemAdmin;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.CoreRole;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.transaction.transfer.*;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.transaction.transfer.TransferTicket;
import com.connectto.wallet.model.transaction.transfer.TransferTransaction;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionBaseAction;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionDecripter;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransferTransactionAction extends TransactionBaseAction {

    private static final Logger logger = Logger.getLogger(TransferTransactionAction.class.getSimpleName());

    private ITransferTransactionManager transferTransactionManager;

    private ICoreSystemAdminManager coreSystemAdminManager;

    //TransferTicket
    private String itemId;
    private String name;
    private String description;
    //
    private String walletId;
    //
    private String systemAdminUsername;
    private String systemAdminPassword;
    private String systemAdminPartitionId;

    public String transferFromPartitionToUser() {

        responseDto.cleanMessages();
        boolean decripted = true;

        Map<String, Object> signInParams = new HashMap<String, Object>();
        signInParams.put("roles", new int[]{CoreRole.root.getRoleId(),
                CoreRole.System_Admin.getRoleId(),
                CoreRole.Manager.getRoleId(), CoreRole.User.getRoleId()});

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setActionDate(new Date(System.currentTimeMillis()));
        transferTransaction.setIsEncoded(decripted);

        try {

            if(!convertAmountAndCurrency(decripted)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            transferTransaction.setTransferAmount(productAmount);
            transferTransaction.setTransferAmountCurrencyType(productCurrencyType);

            TransferTicket ticket = initTransferTicket(decripted);
            signInParams.put("username", ticket.getSystemAdminUsername());
            signInParams.put("password", ticket.getSystemAdminPassword());
            signInParams.put("partitionId", ticket.getSystemAdminPartitionId());

            CoreSystemAdmin coreSystemAdmin = coreSystemAdminManager.signIn(signInParams);

            transferTransaction.setTransferTicket(ticket);
            transferTransaction.setCoreSystemAdminId(coreSystemAdmin.getId());
            transferTransaction.setWalletId(ticket.getWalletId());

            transferTransactionManager.add(transferTransaction);

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

    private synchronized TransferTicket initTransferTicket(boolean decripted) throws InvalidParameterException, EncryptException, NumberFormatException {

        if (Utils.isEmpty(itemId)) {
            throw new InvalidParameterException("TransferTicket itemId is empty");
        }

        if (Utils.isEmpty(name)) {
            throw new InvalidParameterException("TransferTicket name is empty");
        }

        if (Utils.isEmpty(description)) {
            throw new InvalidParameterException("TransferTicket description is empty");
        }

        if (Utils.isEmpty(walletId)) {
            throw new InvalidParameterException("TransferTicket walletId is empty");
        }

        if (Utils.isEmpty(systemAdminUsername)) {
            throw new InvalidParameterException("TransferTicket systemAdminUsername is empty");
        }

        if (Utils.isEmpty(systemAdminPassword)) {
            throw new InvalidParameterException("TransferTicket systemAdminPassword is empty");
        }

        if (decripted) {
            systemAdminPassword = TransactionDecripter.decript(systemAdminPassword);
        }

        systemAdminPassword = "" + systemAdminPassword.hashCode();

        if (Utils.isEmpty(systemAdminPartitionId)) {
            throw new InvalidParameterException("TransferTicket systemAdminPartitionId is empty");
        }


        TransferTicket ticket = new TransferTicket();
        ticket.setItemId(Long.parseLong(itemId));
        ticket.setName(name);
        ticket.setDescription(description);

        ticket.setSystemAdminUsername(systemAdminUsername);
        ticket.setSystemAdminPassword(systemAdminPassword);


        ticket.setSystemAdminPartitionId(Integer.parseInt(systemAdminPartitionId));
        ticket.setWalletId(Long.parseLong(walletId));

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

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public void setSystemAdminUsername(String systemAdminUsername) {
        this.systemAdminUsername = systemAdminUsername;
    }

    public void setSystemAdminPassword(String systemAdminPassword) {
        this.systemAdminPassword = systemAdminPassword;
    }

    public void setSystemAdminPartitionId(String systemAdminPartitionId) {
        this.systemAdminPartitionId = systemAdminPartitionId;
    }

    public void setCoreSystemAdminManager(ICoreSystemAdminManager coreSystemAdminManager) {
        this.coreSystemAdminManager = coreSystemAdminManager;
    }

    public void setTransferTransactionManager(ITransferTransactionManager transferTransactionManager) {
        this.transferTransactionManager = transferTransactionManager;
    }
}
