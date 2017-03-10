package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.CreditCardTransfer;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardTransferDao;
import com.connectto.wallet.model.wallet.CreditCardTransactionResult;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by htdev001 on 8/26/14.
 */
public class CreditCardTransferDaoImpl implements ICreditCardTransferDao {

    private static final Logger logger = Logger.getLogger(CreditCardTransferDaoImpl.class.getSimpleName());

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }



    @Override
    public void add(CreditCardTransfer cardTransfer) throws DatabaseException {
        try {
            sqlMapClient.insert("nsCreditCardTransfer.add", cardTransfer);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void edit(CreditCardTransfer cardTransfer)  throws DatabaseException, EntityNotFoundException {
        try {
            int count =  sqlMapClient.update("nsCreditCardTransfer.edit", cardTransfer);
            if (count != 1) {
                throw new EntityNotFoundException("Could not edit, CreditCardTransfer not exists id=" + cardTransfer.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void add(CreditCardTransactionResult cardTransferResult)   throws DatabaseException {
        try {
            sqlMapClient.insert("nsCreditCardTransfer.addTransactionResult", cardTransferResult);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
