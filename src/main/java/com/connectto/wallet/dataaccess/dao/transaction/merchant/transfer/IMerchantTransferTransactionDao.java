package com.connectto.wallet.dataaccess.dao.transaction.merchant.transfer;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTransaction;

/**
 * Created by Serozh on 2/14/2017.
 */
public interface IMerchantTransferTransactionDao {

    public void add(MerchantTransferTransaction data) throws DatabaseException;

}
