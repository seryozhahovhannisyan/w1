package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.dao.ITransactionDao;
import com.connectto.wallet.model.wallet.Transaction;
import com.connectto.wallet.model.wallet.TransactionNotifier;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDaoImpl implements ITransactionDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(Transaction transaction) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransaction.add", transaction);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isOrderCode(String orderCode) throws DatabaseException {
        try {
            return (Boolean) sqlMapClient.queryForObject("nsTransaction.isOrderCode", orderCode);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Transaction getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            Transaction transaction = (Transaction) sqlMapClient.queryForObject("nsTransaction.getById", id);
            if (transaction == null) {
                throw new EntityNotFoundException("Could not fount Transaction, id=" + id);
            }
            return transaction;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionDto getDtoById(Map<String, Object> params) throws DatabaseException, EntityNotFoundException {
        try {
            List<TransactionDto> transactionDtos = sqlMapClient.queryForList("nsTransaction.getDtosByParams", params);
            if (Utils.isEmpty(transactionDtos)) {
                throw new EntityNotFoundException("Could not fount TransactionDto, params=" + params);
            }
            return transactionDtos.get(0);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Transaction  getByOrderCode(String orderCode) throws DatabaseException, EntityNotFoundException {
        try {
            Transaction transaction = (Transaction) sqlMapClient.queryForObject("nsTransaction.getByOrderCode", orderCode);
            if (transaction == null) {
                throw new EntityNotFoundException("Could not fount Transaction, orderCode=" + orderCode);
            }
            return transaction;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public TransactionNotifier getTransactionNotifier(Map<String, Object> params) throws DatabaseException {
        try {
            return (TransactionNotifier) sqlMapClient.queryForObject("nsTransaction.getTransactionNotifier", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<TransactionDto> getDtosByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsTransaction.getDtosByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Transaction> getSampleTransactionsByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsTransaction.getSampleTransactionsByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(Transaction transaction) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsTransaction.update", transaction);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Transaction not exists id=" + transaction.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateState(Transaction transaction) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsTransaction.updateState", transaction);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Transaction not exists id=" + transaction.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsTransaction.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, Transaction not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
