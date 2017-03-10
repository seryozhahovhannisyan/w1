package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDeposit;

import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDepositDao {

    public void add(TransactionDeposit data) throws DatabaseException;

    public  TransactionDeposit getById(Long id) throws DatabaseException, EntityNotFoundException;

    public TransactionDeposit getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException;

    public boolean isOrderCode(String orderCode) throws DatabaseException;

    public void update(TransactionDeposit data) throws DatabaseException;

    public TransactionDeposit getUniqueByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException;

}
