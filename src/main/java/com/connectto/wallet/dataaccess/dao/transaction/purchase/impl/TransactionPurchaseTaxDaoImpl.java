package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.ITransactionPurchaseTaxDao;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionPurchaseTaxDaoImpl implements ITransactionPurchaseTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionPurchaseTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionPurchaseTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void pay(TransactionPurchaseTax data) throws DatabaseException {
        try {
            sqlMapClient.update("nsTransactionPurchaseTax.pay", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
