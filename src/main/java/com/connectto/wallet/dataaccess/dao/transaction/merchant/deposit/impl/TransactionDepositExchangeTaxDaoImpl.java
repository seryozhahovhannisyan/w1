package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.ITransactionDepositExchangeTaxDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositExchangeTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionDepositExchangeTaxDaoImpl implements ITransactionDepositExchangeTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionDepositExchangeTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionDepositExchangeTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
