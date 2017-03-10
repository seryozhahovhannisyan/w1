package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseExchangeTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseExchangeTaxDao {

    public void add(TransactionPurchaseExchangeTax data) throws DatabaseException;

}
