package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositExchangeTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDepositExchangeTaxDao {

    public void add(TransactionDepositExchangeTax data) throws DatabaseException;

}
