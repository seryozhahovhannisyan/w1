package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositExchange;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDepositExchangeDao {

    public void add(TransactionDepositExchange data) throws DatabaseException;

}
