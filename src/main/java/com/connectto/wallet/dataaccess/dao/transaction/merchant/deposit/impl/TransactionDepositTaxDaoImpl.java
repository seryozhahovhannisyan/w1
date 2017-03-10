package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.ITransactionDepositTaxDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDepositTaxDaoImpl implements ITransactionDepositTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDepositTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDepositTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void pay(TransactionDepositTax data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionDepositTax.pay", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
