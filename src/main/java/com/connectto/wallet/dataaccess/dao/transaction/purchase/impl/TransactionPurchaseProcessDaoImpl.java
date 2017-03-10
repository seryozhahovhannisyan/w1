package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.ITransactionPurchaseProcessDao;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseProcess;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionPurchaseProcessDaoImpl implements ITransactionPurchaseProcessDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionPurchaseProcess data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionPurchaseProcess.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
