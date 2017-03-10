package com.connectto.wallet.dataaccess.service.transaction.merchant;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTransaction;

/**
 * Created by Serozh on 2/14/2017.
 */
public interface IMerchantTransferTransactionManager {

    public void add(MerchantTransferTransaction data) throws InternalErrorException, EntityNotFoundException, PermissionDeniedException;

}
