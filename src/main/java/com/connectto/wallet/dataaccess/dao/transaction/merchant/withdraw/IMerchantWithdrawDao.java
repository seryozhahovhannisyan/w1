package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.merchant.withdraw.MerchantWithdraw;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IMerchantWithdrawDao {

    public void add(MerchantWithdraw data) throws DatabaseException;

    public MerchantWithdraw getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<MerchantWithdraw> getByParams(Map<String, Object> params) throws DatabaseException;

    public int getCountByParams(Map<String, Object> params) throws DatabaseException;

}
