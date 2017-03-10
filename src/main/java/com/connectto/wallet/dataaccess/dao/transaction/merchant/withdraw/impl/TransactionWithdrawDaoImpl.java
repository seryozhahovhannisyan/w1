package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.ITransactionWithdrawDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionWithdrawDaoImpl implements ITransactionWithdrawDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionWithdraw data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionWithdraw.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionWithdraw getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionWithdraw purchase = (TransactionWithdraw) sqlMapClient.queryForObject("nsTransactionWithdraw.getById", id);
            if (purchase == null) {
                throw new EntityNotFoundException(String.format("Could not find TransactionWithdraw Id=[%d]", id));
            }
            return purchase;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isOrderCode(String orderCode) throws DatabaseException {
        try {
            return (Boolean) sqlMapClient.queryForObject("nsTransactionWithdraw.isOrderCode", orderCode);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionWithdraw getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("orderCode", orderCode);
        map.put("walletId", walletId);
        map.put("setupId", setupId);
        try {
            TransactionWithdraw transactionWithdraw = (TransactionWithdraw) sqlMapClient.queryForObject("nsTransactionWithdraw.getBy", map);
            if (transactionWithdraw == null) {
                throw new EntityNotFoundException("Could not find TransactionWithdraw orderCode = [" + orderCode + "] walletId=[" + walletId + "] setupId=[" + setupId + "]");
            }
            return transactionWithdraw;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(TransactionWithdraw data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionWithdraw.update", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionWithdraw getUniqueByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionWithdraw transactionWithdraw = (TransactionWithdraw) sqlMapClient.queryForObject("nsTransactionWithdraw.getUniqueByParams", params);
            if (transactionWithdraw == null) {
                throw new EntityNotFoundException("Could not find TransactionWithdraw orderCode =" + params);
            }
            return transactionWithdraw;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
