package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.merchant.deposit.MerchantDepositTax;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IMerchantDepositTaxDao {

    public void add(MerchantDepositTax data) throws DatabaseException;

}
