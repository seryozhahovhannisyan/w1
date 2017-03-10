package com.connectto.wallet.dataaccess.service.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.IExchangeDao;
import com.connectto.wallet.dataaccess.service.IExchangeManager;
import com.connectto.wallet.model.wallet.Exchange;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
@Transactional(readOnly = true)
public class ExchangeManagerImpl implements IExchangeManager {

    private IExchangeDao exchangeDao;

    public void setExchangeDao(IExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(Exchange exchange) throws InternalErrorException {
        try {
            exchangeDao.add(exchange);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Exchange getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return exchangeDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<Exchange> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return exchangeDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return exchangeDao.getCountByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Exchange exchange) throws InternalErrorException, EntityNotFoundException {
        try {
            exchangeDao.update(exchange);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            exchangeDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
