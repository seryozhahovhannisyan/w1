package com.connectto.wallet.dataaccess.service.transaction.merchant;

import com.connectto.general.exception.*;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;

import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ITransactionWithdrawManager {

    public void start(TransactionWithdraw data) throws InternalErrorException, PermissionDeniedException;

    public TransactionWithdraw cancel(Map<String, Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException;

    public TransactionWithdraw approve(Map<String, Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException;

    public TransactionWithdraw getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public TransactionWithdraw getUniqueByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException;

}
