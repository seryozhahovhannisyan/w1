package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletLocationDao;
import com.connectto.wallet.dataaccess.service.wallet.IWalletLocationManager;
import com.connectto.wallet.model.wallet.WalletLocation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
@Transactional(readOnly = true)
public class WalletLocationManagerImpl implements IWalletLocationManager {

    private IWalletLocationDao walletLocationDao;

    public void setWalletLocationDao(IWalletLocationDao walletLocationDao) {
        this.walletLocationDao = walletLocationDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(WalletLocation walletLocation) throws InternalErrorException {
        try {
            walletLocationDao.add(walletLocation);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public WalletLocation getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletLocationDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletLocation> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletLocationDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletLocationDao.getCountByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(WalletLocation walletLocation) throws InternalErrorException, EntityNotFoundException {
        try {
            walletLocationDao.update(walletLocation);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            walletLocationDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
