package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.wallet.IExchangeRateDao;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 9/11/14.
 */
public class ExchangeRateDaoImpl implements IExchangeRateDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(ExchangeRate exchangeRate) throws DatabaseException {
        try {
            sqlMapClient.insert("nsExchangeRate.add", exchangeRate);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

   /* @Override
    public void add(List<ExchangeRate> exchangeRates) throws DatabaseException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("exchangeRates", exchangeRates);

        try {
            sqlMapClient.insert("nsExchangeRate.addAll", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }*/

    @Override
    public ExchangeRate getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            ExchangeRate exchangeRate = (ExchangeRate) sqlMapClient.queryForObject("nsExchangeRate.getById", id);
            if (exchangeRate == null) {
                throw new EntityNotFoundException("Could not found ExchangeRate; id=" + id);
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ExchangeRate> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchangeRate.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ExchangeRate> getLastUpdatesByCurrency(int sourceId) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchangeRate.getLastUpdatesByCurrency", sourceId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsExchangeRate.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete ExchangeRate; id=" + id);
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(List<Long> ides) throws DatabaseException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ides", ides);

        try {
            sqlMapClient.delete("nsExchangeRate.deleteAll", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
