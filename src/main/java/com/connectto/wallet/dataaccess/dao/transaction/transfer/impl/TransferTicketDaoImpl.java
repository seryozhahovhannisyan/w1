package com.connectto.wallet.dataaccess.dao.transaction.transfer.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.dataaccess.dao.transaction.transfer.ITransferTicketDao;
import com.connectto.wallet.model.transaction.transfer.TransferTicket;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

/**
 * Created by Serozh on 2/14/2017.
 */
public class TransferTicketDaoImpl implements ITransferTicketDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(TransferTicket data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsTransferTicket.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
