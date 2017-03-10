package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.Exchange;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IExchangeDao {

    public void add(Exchange exchange) throws DatabaseException;

    public Exchange getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<Exchange> getByParams(Map<String, Object> params) throws DatabaseException;

    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void update(Exchange exchange) throws DatabaseException, EntityNotFoundException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
