package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.TransactionDispute;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDisputeDao {

    public void add(TransactionDispute transactionDispute) throws DatabaseException;

    public TransactionDispute getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<TransactionDispute> getByParams(Map<String, Object> params) throws DatabaseException;

    public Long getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void update(TransactionDispute transactionDispute) throws DatabaseException, EntityNotFoundException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
