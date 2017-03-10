package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.wallet.TransactionProcess;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionProcessDao {

    public void add(TransactionProcess transactionAction) throws DatabaseException;

}
