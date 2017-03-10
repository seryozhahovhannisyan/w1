package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.IWalletSetupWithdrawTaxDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdrawProcessTax;
import com.connectto.wallet.model.transaction.merchant.withdraw.WalletSetupWithdrawTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class WalletSetupWithdrawTaxDaoImpl implements IWalletSetupWithdrawTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletSetupWithdrawTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletSetupWithdrawTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
