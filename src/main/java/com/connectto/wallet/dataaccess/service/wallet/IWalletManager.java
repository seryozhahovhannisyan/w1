package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.BlockedUser;
import com.connectto.wallet.model.wallet.Wallet;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletManager {

    public void add(Wallet wallet) throws InternalErrorException;

    public void blockUser(BlockedUser blockedUser) throws InternalErrorException;

    public void unblockUser(BlockedUser blockedUser) throws InternalErrorException;

    public Wallet entry(Wallet wallet) throws InternalErrorException;

    public Wallet getWalletByUserAndResetPasswordToken(String u, String t) throws InternalErrorException, EntityNotFoundException;

    public void exit(Wallet wallet) throws InternalErrorException;

    public Wallet login(String encryptKey) throws InternalErrorException, EntityNotFoundException;

    public Wallet login(String email, String password) throws InternalErrorException, EntityNotFoundException;

    public Wallet getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public Wallet getByUserId(Long userId) throws InternalErrorException, EntityNotFoundException;

    public Wallet getByUserId(Long userId, String sessionId) throws InternalErrorException, EntityNotFoundException;

    public List<Wallet> getByParams(Map<String, Object> params) throws InternalErrorException;

    public List<Long> getBlockedUserIdes(Long userId) throws InternalErrorException;

    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException;

    public Double checkUserBalance(Long walletId) throws InternalErrorException;

    public void update(Wallet wallet) throws InternalErrorException, EntityNotFoundException;

    public void resetPassword(Wallet wallet) throws InternalErrorException, EntityNotFoundException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

}
