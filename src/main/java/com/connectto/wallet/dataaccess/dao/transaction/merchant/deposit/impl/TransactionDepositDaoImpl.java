package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.ITransactionDepositDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDeposit;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDepositDaoImpl implements ITransactionDepositDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDeposit data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDeposit.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionDeposit getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionDeposit purchase = (TransactionDeposit) sqlMapClient.queryForObject("nsTransactionDeposit.getById", id);
            if (purchase == null) {
                throw new EntityNotFoundException(String.format("Could not find TransactionDeposit Id=[%d]", id));
            }
            return purchase;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isOrderCode(String orderCode) throws DatabaseException {
        try {
            return (Boolean) sqlMapClient.queryForObject("nsTransactionDeposit.isOrderCode", orderCode);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionDeposit getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("orderCode", orderCode);
        map.put("walletId", walletId);
        map.put("setupId", setupId);
        try {
            TransactionDeposit transactionPurchase = (TransactionDeposit)sqlMapClient.queryForObject("nsTransactionDeposit.getBy", map);
            if(transactionPurchase == null){
                throw  new EntityNotFoundException("Could not find TransactionDeposit orderCode = [" +orderCode+ "] walletId=[" +walletId + "] setupId=["+ setupId+"]");
            }
            return transactionPurchase ;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(TransactionDeposit data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionDeposit.update", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionDeposit getUniqueByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionDeposit deposit = (TransactionDeposit) sqlMapClient.queryForObject("nsTransactionDeposit.getUniqueByParams", params);
            if (deposit == null) {
                throw new EntityNotFoundException("Could not find TransactionDeposit orderCode =" + params);
            }
            return deposit;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
