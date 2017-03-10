package com.connectto.wallet.dataaccess.dao.transaction.transfer;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.transfer.TransferTransaction;

/**
 * Created by Serozh on 2/14/2017.
 */
public interface ITransferTransactionDao {

    public void add(TransferTransaction data) throws DatabaseException;

}
