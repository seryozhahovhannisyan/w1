package com.connectto.wallet.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.Exchange;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IExchangeManager {

    public void add(Exchange exchange) throws InternalErrorException;

    public Exchange getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<Exchange> getByParams(Map<String, Object> params) throws InternalErrorException;

    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException;

    public void update(Exchange exchange) throws InternalErrorException, EntityNotFoundException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

}
