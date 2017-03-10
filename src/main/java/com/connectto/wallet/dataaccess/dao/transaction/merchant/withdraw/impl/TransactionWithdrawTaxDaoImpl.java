package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.ITransactionWithdrawTaxDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionWithdrawTaxDaoImpl implements ITransactionWithdrawTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionWithdrawTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionWithdrawTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void pay(TransactionWithdrawTax data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionWithdrawTax.pay", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
