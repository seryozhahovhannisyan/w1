package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.wallet.IExchangeSourceDao;
import com.connectto.wallet.dataaccess.service.wallet.IExchangeSourceManager;
import com.connectto.wallet.model.wallet.ExchangeSource;

import java.util.List;

/**
 * Created by htdev001 on 12/24/14.
 */
public class ExchangeSourceManagerImpl implements IExchangeSourceManager {

    private IExchangeSourceDao exchangeSourceDao;

    public void setExchangeSourceDao(IExchangeSourceDao exchangeSourceDao) {
        this.exchangeSourceDao = exchangeSourceDao;
    }

    @Override
    public ExchangeSource getById(int id) throws InternalErrorException, EntityNotFoundException {
        try {
            return exchangeSourceDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<ExchangeSource> getAll(int partitionId) throws InternalErrorException {
        try {
            return exchangeSourceDao.getAll(partitionId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
    @Override
    public List<ExchangeSource> getAllActive() throws InternalErrorException {
        try {
            return exchangeSourceDao.getAllActive();
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    } @Override
    public List<ExchangeSource> getAllActive(int partitionId) throws InternalErrorException {
        try {
            return exchangeSourceDao.getAllActive(partitionId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
    @Override
    public ExchangeSource getDefault() throws InternalErrorException, EntityNotFoundException {
        try {
            return exchangeSourceDao.getDefault();
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }


}
