package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDepositTaxDao {

    public void add(TransactionDepositTax data) throws DatabaseException;
    public void pay(TransactionDepositTax data) throws DatabaseException;

}
