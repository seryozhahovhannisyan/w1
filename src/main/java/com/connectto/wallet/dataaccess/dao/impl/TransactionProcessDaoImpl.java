package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.ITransactionProcessDao;
import com.connectto.wallet.model.wallet.TransactionProcess;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionProcessDaoImpl implements ITransactionProcessDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionProcess transactionProcess) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionProcess.add", transactionProcess);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
