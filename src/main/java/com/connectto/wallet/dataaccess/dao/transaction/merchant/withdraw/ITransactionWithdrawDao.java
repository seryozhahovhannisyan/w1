package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;

import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionWithdrawDao {

    public void add(TransactionWithdraw data) throws DatabaseException;

    public  TransactionWithdraw getById(Long id) throws DatabaseException, EntityNotFoundException;

    public TransactionWithdraw getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException;

    public boolean isOrderCode(String orderCode) throws DatabaseException;

    public void update(TransactionWithdraw data) throws DatabaseException;

    public TransactionWithdraw getUniqueByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException;

}
