package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by htdev001 on 12/23/14.
 */
@Transactional(readOnly = true)
public class WalletSetupManagerImpl implements IWalletSetupManager {

    private IWalletSetupDao walletSetupDao;

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(WalletSetup walletSetup) throws InternalErrorException {
        try {
            walletSetupDao.add(walletSetup);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public WalletSetup getById(Long id) throws InternalErrorException {
        try {
            return walletSetupDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public WalletSetup getByPartitionId(int id) throws InternalErrorException {
        try {
            return walletSetupDao.getByPartitionId(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletSetup> getAll() throws InternalErrorException {
        try {
            return walletSetupDao.getAll();
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException {
        try {
            walletSetupDao.update(walletSetup);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateAvailableRates(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException {
        try {
            walletSetupDao.updateAvailableRates(walletSetup);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateIncreaseBalance(  WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException {
        /*try {
            walletSetupDao.updateIncreaseBalance( walletSetup);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }*/
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateDecreaseBalance(  WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException {
       /* try {
            walletSetupDao.updateDecreaseBalance( walletSetup);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }*/
    }
}
