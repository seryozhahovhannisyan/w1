package com.connectto.wallet.dataaccess.service.transaction.transfer;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.wallet.model.transaction.transfer.TransferTransaction;

/**
 * Created by Serozh on 2/14/2017.
 */
public interface ITransferTransactionManager {

    public void add(TransferTransaction data) throws InternalErrorException, EntityNotFoundException, PermissionDeniedException;

}
