package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.ExchangeSource;

import java.util.List;

/**
 * Created by htdev001 on 9/11/14.
 */
public interface IExchangeSourceDao {

    public void add(ExchangeSource exchangeSource) throws DatabaseException;

    public ExchangeSource getById(int id) throws DatabaseException, EntityNotFoundException;

    public List<ExchangeSource> getAll(int partitionId) throws DatabaseException;

    public List<ExchangeSource> getAllActive() throws DatabaseException;
     public List<ExchangeSource> getAllActive(int partitionId) throws DatabaseException;
    public ExchangeSource getDefault() throws DatabaseException, EntityNotFoundException;

    public void update(ExchangeSource exchangeSource) throws DatabaseException, EntityNotFoundException;

    public void activate(ExchangeSource exchangeSource) throws DatabaseException, EntityNotFoundException;

    public void makeDefault(int id) throws DatabaseException, EntityNotFoundException;

    public void removeDefault() throws DatabaseException;

    public void delete(int id) throws DatabaseException, EntityNotFoundException;
}
