package com.connectto.wallet.dataaccess.dao.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.IExchangeDao;
import com.connectto.wallet.model.wallet.Exchange;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class ExchangeDaoImpl implements IExchangeDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(Exchange exchange) throws DatabaseException {
        try {
            sqlMapClient.insert("nsExchange.add", exchange);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Exchange getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            Exchange exchange = (Exchange) sqlMapClient.queryForObject("nsExchange.getById", id);
            if (exchange == null) {
                throw new EntityNotFoundException("Could not found Exchange, id=" + id);
            }
            return exchange;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Exchange> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchange.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsExchange.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(Exchange exchange) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsExchange.update", exchange);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Exchange not exists id=" + exchange.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsExchange.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, Exchange not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
