package com.connectto.wallet.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.TransactionDispute;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDisputeManager {

    public void add(TransactionDispute transactionDispute) throws InternalErrorException;

    public TransactionDispute getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<TransactionDispute> getByParams(Map<String, Object> params) throws InternalErrorException;

    public Long getCountByParams(Map<String, Object> params) throws InternalErrorException;

    public void update(TransactionDispute transactionDispute) throws InternalErrorException, EntityNotFoundException;

    public void closeDispute(Long id) throws InternalErrorException, EntityNotFoundException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

}
