package com.connectto.wallet.dataaccess.service.general.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.general.IWalletLoggerDao;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogLevel;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 14/11/14.
 */
public class WalletLoggerManagerImpl implements IWalletLoggerManager {

    private IWalletLoggerDao walletLoggerDao;

    public void setWalletLoggerDao(IWalletLoggerDao walletLoggerDao) {
        this.walletLoggerDao = walletLoggerDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(WalletLogger logger) throws InternalErrorException {
        try {
            walletLoggerDao.add(logger);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addAll(List<WalletLogger> loggers) throws InternalErrorException {
        try {
            for (WalletLogger sl : loggers) {
                walletLoggerDao.add(sl);
            }
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletLogger> getAll() throws InternalErrorException {
        try {
            return walletLoggerDao.getAll();
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletLogger> getByUserId(Long id) throws InternalErrorException {
        try {
            return walletLoggerDao.getByUserId(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletLogger> getByUserIdAndLevel(Long id, LogLevel logLevel) throws InternalErrorException {
        try {
            return walletLoggerDao.getByUserIdAndLevel(id, logLevel);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletLogger> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletLoggerDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public WalletLogger getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletLoggerDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
