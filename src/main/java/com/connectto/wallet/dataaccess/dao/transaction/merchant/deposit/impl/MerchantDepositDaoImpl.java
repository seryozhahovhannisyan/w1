package com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.IMerchantDepositDao;
import com.connectto.wallet.model.transaction.merchant.deposit.MerchantDeposit;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class MerchantDepositDaoImpl implements IMerchantDepositDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(MerchantDeposit data) throws DatabaseException {
        try {
            sqlMapClient.insert("nsMerchantDeposit.add", data);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public MerchantDeposit getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            MerchantDeposit creditCard = (MerchantDeposit) sqlMapClient.queryForObject("nsMerchantDeposit.getById", id);
            if (creditCard == null) {
                throw new EntityNotFoundException("Could not found MerchantDeposit, id=" + id);
            }
            return creditCard;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    } 

    @Override
    public List<MerchantDeposit> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsMerchantDeposit.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsMerchantDeposit.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
