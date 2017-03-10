package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionWithdrawTaxDao {

    public void add(TransactionWithdrawTax data) throws DatabaseException;
    public void pay(TransactionWithdrawTax data) throws DatabaseException;

}
