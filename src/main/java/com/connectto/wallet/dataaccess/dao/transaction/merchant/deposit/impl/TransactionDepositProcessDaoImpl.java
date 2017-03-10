package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.ITransactionDepositProcessDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositProcess;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDepositProcessDaoImpl implements ITransactionDepositProcessDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDepositProcess data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDepositProcess.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
