package com.connectto.wallet.dataaccess.service.transaction.purchase;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchase;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseManager {

    public void freezeDriverBalance(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException;

    public void freeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException;

    public void cancelFreeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException;

    public void approveFreeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException, CreditCardException;

    public void directChargeFromWallet(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException, CreditCardException;

    public TransactionPurchase getById(Long id) throws InternalErrorException, EntityNotFoundException;

}
