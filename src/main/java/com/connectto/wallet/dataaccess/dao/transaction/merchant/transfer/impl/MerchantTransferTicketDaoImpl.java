package com.connectto.wallet.dataaccess.dao.transaction.merchant.transfer.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.transfer.IMerchantTransferTicketDao;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTicket;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by Serozh on 2/14/2017.
 */
public class MerchantTransferTicketDaoImpl implements IMerchantTransferTicketDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(MerchantTransferTicket data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsMerchantTransferTicket.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
