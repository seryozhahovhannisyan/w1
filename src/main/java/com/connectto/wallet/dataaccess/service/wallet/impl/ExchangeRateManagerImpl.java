package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.dao.wallet.IExchangeRateDao;
import com.connectto.wallet.dataaccess.service.wallet.IExchangeRateManager;
import com.connectto.wallet.model.wallet.ExchangeRate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 9/11/14.
 */

@Transactional(readOnly = true)
public class ExchangeRateManagerImpl implements IExchangeRateManager {

    private IExchangeRateDao exchangeRateDao;

    public void setExchangeRateDao(IExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(ExchangeRate exchangeRate) throws InternalErrorException {
        try {
            exchangeRateDao.add(exchangeRate);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(List<ExchangeRate> exchangeRates) throws InternalErrorException {

        if(Utils.isEmpty(exchangeRates)){
            return;
        }

        for(ExchangeRate exchangeRate : exchangeRates){
            try {
                exchangeRateDao.add(exchangeRate);
            } catch (DatabaseException e) {
                throw new InternalErrorException(e);
            }
        }

    }

    @Override
    public ExchangeRate getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return exchangeRateDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<ExchangeRate> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return exchangeRateDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<ExchangeRate> getLastUpdatesByCurrency(int sourceId) throws InternalErrorException {
        try {
            return exchangeRateDao.getLastUpdatesByCurrency(sourceId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            exchangeRateDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(List<Long> ides) throws InternalErrorException {
        try {
            exchangeRateDao.delete(ides);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
