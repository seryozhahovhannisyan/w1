package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.ITransactionPurchaseDao;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchase;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionPurchaseDaoImpl implements ITransactionPurchaseDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionPurchase data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionPurchase.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionPurchase getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionPurchase purchase = (TransactionPurchase) sqlMapClient.queryForObject("nsTransactionPurchase.getById", id);
            if (purchase == null) {
                throw new EntityNotFoundException(String.format("Could not find TransactionPurchase Id=[%d]", id));
            }
            return purchase;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isOrderCode(String orderCode) throws DatabaseException {
        try {
            return (Boolean) sqlMapClient.queryForObject("nsTransactionPurchase.isOrderCode", orderCode);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionPurchase getBy(String orderCode, Long walletId, Long setupId) throws DatabaseException, EntityNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("orderCode", orderCode);
        map.put("walletId", walletId);
        map.put("setupId", setupId);
        try {
            TransactionPurchase transactionPurchase = (TransactionPurchase)sqlMapClient.queryForObject("nsTransactionPurchase.getBy", map);
            if(transactionPurchase == null){
                throw  new EntityNotFoundException("Could not find TransactionPurchase orderCode = [" +orderCode+ "] walletId=[" +walletId + "] setupId=["+ setupId+"]");
            }
            return transactionPurchase ;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(TransactionPurchase data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionPurchase.update", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
