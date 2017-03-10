package com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.IMerchantWithdrawDao;
import com.connectto.wallet.model.transaction.merchant.withdraw.MerchantWithdraw;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class MerchantWithdrawDaoImpl implements IMerchantWithdrawDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(MerchantWithdraw data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsMerchantWithdraw.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public MerchantWithdraw getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            MerchantWithdraw creditCard = (MerchantWithdraw) sqlMapClient.queryForObject("nsMerchantWithdraw.getById", id);
            if (creditCard == null) {
                throw new EntityNotFoundException("Could not found MerchantWithdraw, id=" + id);
            }
            return creditCard;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    } 

    @Override
    public List<MerchantWithdraw> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsMerchantWithdraw.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsMerchantWithdraw.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
