package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.IMerchantWithdrawTaxDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.MerchantWithdrawTax;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class MerchantWithdrawTaxDaoImpl implements IMerchantWithdrawTaxDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(MerchantWithdrawTax data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsMerchantWithdrawTax.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


}
