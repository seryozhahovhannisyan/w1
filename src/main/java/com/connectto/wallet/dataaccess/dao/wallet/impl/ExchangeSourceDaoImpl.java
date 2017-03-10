package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.wallet.IExchangeSourceDao;
import com.connectto.wallet.model.wallet.ExchangeSource;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by htdev001 on 9/11/14.
 */
public class ExchangeSourceDaoImpl implements IExchangeSourceDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(ExchangeSource exchangeSource) throws DatabaseException {
        try {
            sqlMapClient.insert("nsExchangeSource.add", exchangeSource);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public ExchangeSource getById(int id) throws DatabaseException, EntityNotFoundException {
        try {
            ExchangeSource exchangeSource = (ExchangeSource) sqlMapClient.queryForObject("nsExchangeSource.getById", id);
            if (exchangeSource == null) {
                throw new EntityNotFoundException("Could not found ExchangeSource; id=" + id);
            }
            return exchangeSource;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ExchangeSource> getAll(int partitionId) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchangeSource.getAll", partitionId );
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ExchangeSource> getAllActive() throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchangeSource.getAllActive");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }@Override
    public List<ExchangeSource> getAllActive(int partitionId) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsExchangeSource.getAllActiveByPartition",partitionId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public ExchangeSource getDefault() throws DatabaseException, EntityNotFoundException {
        try {
            ExchangeSource exchangeSource = (ExchangeSource) sqlMapClient.queryForObject("nsExchangeSource.getDefault");
            if (exchangeSource == null) {
                throw new EntityNotFoundException("Could not found default ExchangeSource");
            }
            return exchangeSource;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(ExchangeSource exchangeSource) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsExchangeSource.update", exchangeSource);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update ExchangeSource; id=" + exchangeSource.getId());
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void activate(ExchangeSource exchangeSource) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsExchangeSource.activate", exchangeSource);
            if (count != 1) {
                throw new EntityNotFoundException("Could not activate ExchangeSource; id=" + exchangeSource.getId());
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void makeDefault(int id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsExchangeSource.makeDefault", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not makeDefault ExchangeSource; id=" + id);
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeDefault() throws DatabaseException {
        try {
            sqlMapClient.update("nsExchangeSource.removeDefault");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsExchangeSource.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete ExchangeSource; id=" + id);
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
