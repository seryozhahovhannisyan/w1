package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.ITransactionDisputeDao;
import com.connectto.wallet.model.wallet.TransactionDispute;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDisputeDaoImpl implements ITransactionDisputeDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDispute transactionDispute) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDispute.add", transactionDispute);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionDispute getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            TransactionDispute transactionDispute = (TransactionDispute) sqlMapClient.queryForObject("nsTransactionDispute.getById", id);
            if (transactionDispute == null) {
                throw new EntityNotFoundException("Could not found TransactionDispute, id=" + id);
            }
            return transactionDispute;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<TransactionDispute> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsTransactionDispute.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Long getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Long) sqlMapClient.queryForObject("nsTransactionDispute.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(TransactionDispute transactionDispute) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsTransactionDispute.update", transactionDispute);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, TransactionDispute not exists id=" + transactionDispute.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsTransactionDispute.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, TransactionDispute not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
