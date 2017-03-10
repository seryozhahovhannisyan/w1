package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.TransactionTax;

/**
 * Created by htdev001 on 11/25/14.
 */
public interface ITransactionTaxDao {

    public void add(TransactionTax transactionTax) throws DatabaseException;

    public void updatePayment(TransactionTax transactionTax) throws DatabaseException, EntityNotFoundException;

}
