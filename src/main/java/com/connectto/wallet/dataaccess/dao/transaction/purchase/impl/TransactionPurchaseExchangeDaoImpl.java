package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.ITransactionPurchaseExchangeDao;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseExchange;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionPurchaseExchangeDaoImpl implements ITransactionPurchaseExchangeDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionPurchaseExchange data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionPurchaseExchange.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
