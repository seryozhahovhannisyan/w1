package com.connectto.wallet.dataaccess.dao.general.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.general.IWalletLoggerDao;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 11/14/14.
 */
public class WalletLoggerDaoImpl implements IWalletLoggerDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletLogger logger) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletLogger.add", logger);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addAll(List<WalletLogger> loggers) throws DatabaseException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loggers", loggers);
        try {
            sqlMapClient.insert("nsWalletLogger.addAll", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<WalletLogger> getAll() throws DatabaseException {
        return null;
    }

    @Override
    public List<WalletLogger> getByUserId(Long id) throws DatabaseException {
        return null;
    }

    @Override
    public List<WalletLogger> getByUserIdAndLevel(Long id, LogLevel logLevel) throws DatabaseException {
        return null;
    }

    @Override
    public List<WalletLogger> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWalletLogger.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public WalletLogger getById(Long id) throws DatabaseException, EntityNotFoundException {
        return null;
    }
}
