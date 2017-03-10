package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawExchange;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionWithdrawExchangeDao {

    public void add(TransactionWithdrawExchange data) throws DatabaseException;

}
