package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseProcess;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionPurchaseProcessDao {

    public void add(TransactionPurchaseProcess data) throws DatabaseException;

}
