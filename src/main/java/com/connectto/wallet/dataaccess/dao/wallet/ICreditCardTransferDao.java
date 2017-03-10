package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.CreditCardTransactionResult;
import com.connectto.wallet.model.wallet.CreditCardTransfer;

/**
 * Created by Serozh on 11/24/2016.
 */
public interface ICreditCardTransferDao {

    public void add(CreditCardTransfer cardTransfer) throws DatabaseException;

    public void edit(CreditCardTransfer cardTransfer) throws DatabaseException, EntityNotFoundException;

    public void add(CreditCardTransactionResult cardTransferResult) throws DatabaseException;


}
