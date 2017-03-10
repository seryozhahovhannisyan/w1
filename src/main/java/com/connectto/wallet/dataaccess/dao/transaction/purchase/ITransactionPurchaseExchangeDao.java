package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseExchange;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseExchangeDao {

    public void add(TransactionPurchaseExchange data) throws DatabaseException;

}
