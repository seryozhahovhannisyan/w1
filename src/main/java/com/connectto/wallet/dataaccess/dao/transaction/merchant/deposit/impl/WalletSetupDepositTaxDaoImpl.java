package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.IWalletSetupDepositTaxDao;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDepositProcessTax;
import com.connectto.wallet.model.transaction.merchant.deposit.WalletSetupDepositTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class WalletSetupDepositTaxDaoImpl implements IWalletSetupDepositTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletSetupDepositTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletSetupDepositTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
