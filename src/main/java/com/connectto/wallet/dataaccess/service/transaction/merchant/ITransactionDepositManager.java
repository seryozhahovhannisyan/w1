package com.connectto.wallet.dataaccess.service.transaction.merchant;

import com.connectto.general.exception.*;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDeposit;

import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionDepositManager {

    public void start(TransactionDeposit data) throws InternalErrorException, PermissionDeniedException;

    public TransactionDeposit cancel(Map<String, Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException;

    public TransactionDeposit approve(Map<String, Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException;

    public TransactionDeposit getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public TransactionDeposit getUniqueByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException;

}
