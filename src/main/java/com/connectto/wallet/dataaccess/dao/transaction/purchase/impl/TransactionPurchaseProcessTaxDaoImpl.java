package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.ITransactionPurchaseProcessTaxDao;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseProcessTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionPurchaseProcessTaxDaoImpl implements ITransactionPurchaseProcessTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionPurchaseProcessTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionPurchaseProcessTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
