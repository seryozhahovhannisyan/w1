package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.dataaccess.dao.IUserDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.User;
import com.connectto.general.util.Generator;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletLocationDao;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.wallet.BlockedUser;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.WalletLocation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
@Transactional(readOnly = true)
public class WalletManagerImpl implements IWalletManager {

    private IUserDao userDao;
    private IWalletDao walletDao;
    private IWalletLocationDao locationDao;

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setLocationDao(IWalletLocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(Wallet wallet) throws InternalErrorException {
        try {
            walletDao.add(wallet);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void blockUser(BlockedUser blockedUser) throws InternalErrorException {
        try {
            walletDao.delete(blockedUser);
            walletDao.add(blockedUser);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void unblockUser(BlockedUser blockedUser) throws InternalErrorException {
        try {
            walletDao.delete(blockedUser);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Wallet entry(Wallet wallet) throws InternalErrorException {

        WalletLocation walletLocation = wallet.getCurrentLocation();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lastUrl", wallet.getCurrentAccountLastUrl());
        params.put("id", wallet.getCurrentAccountId());

        String encryptKey = walletLocation.getEncryptKey();
        try {
            Wallet w = walletDao.getByUserId(wallet.getUserId());
            encryptKey = isEncryptKeyUsed(encryptKey);
            walletLocation.setEncryptKey(encryptKey);
            walletLocation.setWalletId(w.getId());
            locationDao.add(walletLocation);
            wallet.setId(w.getId());
            w.setId(w.getId());
            walletDao.update(wallet);
            return w;
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Override
    public Wallet getWalletByUserAndResetPasswordToken(String u, String t) throws InternalErrorException, EntityNotFoundException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", u);
        params.put("token", t);
        try {

            List<Wallet> wallets = walletDao.getByParams(params);
            if (wallets == null || wallets.size() != 1) {
                throw new EntityNotFoundException("Could not found wallet by user_id  " + u + " and token " + t);
            }

            return wallets.get(0);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void exit(Wallet wallet) throws InternalErrorException {

    }

    private synchronized String isEncryptKeyUsed(String encryptKey) throws DatabaseException {
        if (!locationDao.isEncryptKeyUsed(encryptKey)) {
            return encryptKey;
        } else {
            encryptKey = Generator.generateRandomString(256);
            return isEncryptKeyUsed(encryptKey);
        }
    }

    @Override
    public Wallet login(String encryptKey) throws InternalErrorException, EntityNotFoundException {
        try {

            Wallet wallet = walletDao.login(encryptKey);

            return wallet;
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Wallet login(String email, String password) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletDao.login(email, password);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Wallet getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Wallet getByUserId(Long userId) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletDao.getByUserId(userId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Wallet getByUserId(Long userId, String sessionId) throws InternalErrorException, EntityNotFoundException {
        try {
            Wallet wallet = walletDao.getByUserId(userId);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            params.put("sessionId", sessionId);

            List<User> users = userDao.getUserByParams(params);
            if (Utils.isEmpty(users)) {
                throw new EntityNotFoundException("Could not found user by userId = " + userId);
            }

            wallet.setUser(users.get(0));

            return wallet;
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<Wallet> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<Long> getBlockedUserIdes(Long userId) throws InternalErrorException {
        try {
            return walletDao.getBlockedUserIdes(userId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletDao.getCountByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Double checkUserBalance(Long walletId) throws InternalErrorException {
        try {
            return walletDao.checkUserBalance(walletId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Wallet wallet) throws InternalErrorException, EntityNotFoundException {
        try {
            walletDao.update(wallet);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void resetPassword(Wallet wallet) throws InternalErrorException, EntityNotFoundException {
        try {
            walletDao.resetPassword(wallet);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            walletDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
