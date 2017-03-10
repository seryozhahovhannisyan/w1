package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.ExchangeRate;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IExchangeRateManager {

    public void add(ExchangeRate exchangeRate) throws InternalErrorException;

    public void add(List<ExchangeRate> exchangeRates) throws InternalErrorException;

    public ExchangeRate getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<ExchangeRate> getByParams(Map<String, Object> params) throws InternalErrorException;

    public List<ExchangeRate> getLastUpdatesByCurrency(int sourceId) throws InternalErrorException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

    public void delete(List<Long> ides) throws InternalErrorException;

}
