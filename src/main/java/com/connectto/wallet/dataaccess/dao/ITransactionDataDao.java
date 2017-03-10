package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.TransactionData;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDataDao {

    public void add(TransactionData transactionData) throws DatabaseException;

    public TransactionData getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<TransactionData> getByParams(Map<String, Object> params) throws DatabaseException;

    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
