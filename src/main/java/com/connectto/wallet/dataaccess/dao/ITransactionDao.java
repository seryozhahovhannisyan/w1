package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.Transaction;
import com.connectto.wallet.model.wallet.TransactionNotifier;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDao {

    public void add(Transaction transaction) throws DatabaseException;

    public boolean isOrderCode(String orderCode) throws DatabaseException;

    public Transaction getById(Long id) throws DatabaseException, EntityNotFoundException;

    public TransactionDto getDtoById(Map<String, Object> params) throws DatabaseException, EntityNotFoundException;

    public Transaction  getByOrderCode(String orderCode) throws DatabaseException, EntityNotFoundException;

    public TransactionNotifier getTransactionNotifier(Map<String, Object> params) throws DatabaseException;

    public List<TransactionDto> getDtosByParams(Map<String, Object> params) throws DatabaseException;

    public List<Transaction> getSampleTransactionsByParams(Map<String, Object> params) throws DatabaseException;

    public void update(Transaction transaction) throws DatabaseException, EntityNotFoundException;

    public void updateState(Transaction transaction) throws DatabaseException, EntityNotFoundException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
