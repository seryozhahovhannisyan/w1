package com.connectto.wallet.dataaccess.dao.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.IPurchaseTicketDao;
import com.connectto.wallet.model.transaction.purchase.PurchaseTicket;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class PurchaseTicketDaoImpl implements IPurchaseTicketDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(PurchaseTicket data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsPurchaseTicket.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public PurchaseTicket getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            PurchaseTicket creditCard = (PurchaseTicket) sqlMapClient.queryForObject("nsPurchaseTicket.getById", id);
            if (creditCard == null) {
                throw new EntityNotFoundException("Could not found PurchaseTicket, id=" + id);
            }
            return creditCard;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    } 

    @Override
    public List<PurchaseTicket> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsPurchaseTicket.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsPurchaseTicket.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    } 

    @Override
    public void forceDelete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsPurchaseTicket.forceDelete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, CreditCard not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
