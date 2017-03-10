package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.ITransactionDataDao;
import com.connectto.wallet.model.wallet.TransactionData;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 11/17/14.
 */
public class TransactionDataDaoImpl implements ITransactionDataDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionData transactionData) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionData.add", transactionData);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


    @Override
    public TransactionData getById(Long id) throws DatabaseException, EntityNotFoundException {

        try {
            TransactionData data = (TransactionData) sqlMapClient.queryForObject("nsTransactionData.getById", id);
            if (data == null) {
                throw new EntityNotFoundException("Could not fount TransactionData, id =" + id);
            }
            return data;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<TransactionData> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsTransactionData.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsTransactionData.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsTransactionData.delete", id);
            if (count < 1) {
                throw new EntityNotFoundException("Could not delete TransactionData, id =" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
