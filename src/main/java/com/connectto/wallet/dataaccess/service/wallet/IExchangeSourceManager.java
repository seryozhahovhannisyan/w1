package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.ExchangeSource;

import java.util.List;

/**
 * Created by htdev001 on 9/11/14.
 */
public interface IExchangeSourceManager {

    public ExchangeSource getById(int id) throws InternalErrorException, EntityNotFoundException;

    public List<ExchangeSource> getAll(int partitionId) throws InternalErrorException;

    public List<ExchangeSource> getAllActive() throws InternalErrorException;
    public List<ExchangeSource> getAllActive(int partitionId) throws InternalErrorException;
    public ExchangeSource getDefault() throws InternalErrorException, EntityNotFoundException;

}
