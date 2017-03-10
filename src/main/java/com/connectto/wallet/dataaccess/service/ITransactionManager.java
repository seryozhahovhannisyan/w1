package com.connectto.wallet.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.wallet.model.wallet.Transaction;
import com.connectto.wallet.model.wallet.TransactionNotifier;
import com.connectto.wallet.model.wallet.WalletExchange;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionManager {

    public void exchangeWalletBalance(WalletExchange walletExchange) throws InternalErrorException;

    public void add(Transaction transaction) throws InternalErrorException;


    //will have same result as add and approve together
    public void chargeWalletImmediately(Transaction transaction) throws InternalErrorException;

    public void chargeFrozenTransaction(Transaction chargeTransaction) throws InternalErrorException, PermissionDeniedException;

    public Transaction getById(Long id)throws InternalErrorException, EntityNotFoundException;

    public TransactionDto getDtoById(Map<String, Object> params)throws InternalErrorException, EntityNotFoundException;

    public Transaction getByOrderCode(String orderCode)throws InternalErrorException, EntityNotFoundException;

    public TransactionNotifier getTransactionNotifier(Map<String, Object> params) throws InternalErrorException;

    public List<TransactionDto> getDtosByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException;

    public List<WalletExchange> getWalletExchangesByParams(Map<String, Object> params) throws InternalErrorException;
    public WalletExchange getWalletExchangesById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<Transaction> getSampleTransactionsByParams(Map<String, Object> params) throws InternalErrorException;

    public void update(Transaction transaction) throws InternalErrorException, EntityNotFoundException;

    public void approve(Transaction transaction) throws InternalErrorException, EntityNotFoundException;

    public void rejectOrCancel(Transaction transaction, TransactionState transactionState) throws InternalErrorException, EntityNotFoundException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

}
