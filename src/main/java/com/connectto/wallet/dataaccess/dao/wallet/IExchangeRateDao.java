package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.ExchangeRate;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 9/11/14.
 */
public interface IExchangeRateDao {

    public void add(ExchangeRate exchangeRate) throws DatabaseException;

    //public void add(List<ExchangeRate> exchangeRates) throws DatabaseException;

    public ExchangeRate getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<ExchangeRate> getByParams(Map<String, Object> params) throws DatabaseException;

    public List<ExchangeRate> getLastUpdatesByCurrency(int sourceId) throws DatabaseException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

    public void delete(List<Long> ides) throws DatabaseException;
}
