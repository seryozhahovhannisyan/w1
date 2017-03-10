package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.ITransactionTaxDao;
import com.connectto.wallet.model.wallet.TransactionTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 11/25/14.
 */
public class TransactionTaxDaoImpl implements ITransactionTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionTax transactionTax) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionTax.add", transactionTax);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updatePayment(TransactionTax transactionTax) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsTransactionTax.updatePayment", transactionTax);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Transaction Tax not exists id=" + transactionTax.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
