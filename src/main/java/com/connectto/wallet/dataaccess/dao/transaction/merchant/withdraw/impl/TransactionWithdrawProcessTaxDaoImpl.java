package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.ITransactionWithdrawProcessTaxDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawProcessTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransactionWithdrawProcessTaxDaoImpl implements ITransactionWithdrawProcessTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransactionWithdrawProcessTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransactionWithdrawProcessTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
