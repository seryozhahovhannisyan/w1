package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.merchant.deposit.MerchantDeposit;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IMerchantDepositDao {

    public void add(MerchantDeposit data) throws DatabaseException;

    public MerchantDeposit getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<MerchantDeposit> getByParams(Map<String, Object> params) throws DatabaseException;

    public int getCountByParams(Map<String, Object> params) throws DatabaseException;

}
