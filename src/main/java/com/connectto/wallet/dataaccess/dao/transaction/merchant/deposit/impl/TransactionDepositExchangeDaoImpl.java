package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.ITransactionDepositExchangeDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositExchange;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDepositExchangeDaoImpl implements ITransactionDepositExchangeDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDepositExchange data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDepositExchange.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
