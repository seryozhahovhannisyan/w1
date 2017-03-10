package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.IWalletExchangeDao;
import com.connectto.wallet.model.wallet.WalletExchange;
import com.connectto.wallet.model.wallet.WalletExchangePending;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 4/25/16.
 */
public class WalletExchangeDaoImpl implements IWalletExchangeDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletExchange walletExchange) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletExchange.add", walletExchange);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void add(WalletExchangePending exchangePending) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletExchange.addWalletExchangePending", exchangePending);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public WalletExchange getById(Long walletExchangeId) throws DatabaseException, EntityNotFoundException {
        try {
            WalletExchange walletExchange = (WalletExchange)sqlMapClient.queryForObject("nsWalletExchange.getById", walletExchangeId);
            if(walletExchange == null){
                throw new EntityNotFoundException("Could not found WalletExchange by id="+walletExchangeId);
            }
            return walletExchange;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<WalletExchange> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWalletExchange.getByParams",params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
