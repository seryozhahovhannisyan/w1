package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseTaxDao {

    public void add(TransactionPurchaseTax data) throws DatabaseException;
    public void pay(TransactionPurchaseTax data) throws DatabaseException;

}
