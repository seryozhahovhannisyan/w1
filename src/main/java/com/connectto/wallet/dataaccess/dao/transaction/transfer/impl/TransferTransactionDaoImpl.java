package com.connectto.wallet.dataaccess.dao.transaction.transfer.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.transfer.ITransferTransactionDao;
import com.connectto.wallet.model.transaction.transfer.TransferTransaction;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by Serozh on 2/14/2017.
 */
public class TransferTransactionDaoImpl implements ITransferTransactionDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransferTransaction data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransferTransaction.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
