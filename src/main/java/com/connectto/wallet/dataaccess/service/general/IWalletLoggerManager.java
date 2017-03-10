package com.connectto.wallet.dataaccess.service.general;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogLevel;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 14/11/14.
 */
public interface IWalletLoggerManager {

    public void add(WalletLogger logger) throws InternalErrorException;

    public void addAll(List<WalletLogger> loggers) throws InternalErrorException;

    public List<WalletLogger> getAll() throws InternalErrorException;

    public List<WalletLogger> getByUserId(Long id) throws InternalErrorException;

    public List<WalletLogger> getByUserIdAndLevel(Long id, LogLevel logLevel) throws InternalErrorException;

    public List<WalletLogger> getByParams(Map<String, Object> params) throws InternalErrorException;

    public WalletLogger getById(Long id) throws InternalErrorException, EntityNotFoundException;

}
