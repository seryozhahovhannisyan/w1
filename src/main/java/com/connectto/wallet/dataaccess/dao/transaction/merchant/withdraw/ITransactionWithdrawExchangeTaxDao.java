package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawExchangeTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionWithdrawExchangeTaxDao {

    public void add(TransactionWithdrawExchangeTax data) throws DatabaseException;

}
