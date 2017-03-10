package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchase;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseDao {

    public void add(TransactionPurchase data) throws DatabaseException;

    public  TransactionPurchase getById(Long id) throws DatabaseException, EntityNotFoundException;

    public TransactionPurchase getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException;

    public boolean isOrderCode(String orderCode) throws DatabaseException;

    public void update(TransactionPurchase data) throws DatabaseException;

}
