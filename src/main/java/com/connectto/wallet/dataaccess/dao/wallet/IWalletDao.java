package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.BlockedUser;
import com.connectto.wallet.model.wallet.Wallet;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletDao {

    public void add(Wallet wallet) throws DatabaseException;

    public void add(BlockedUser blockedUser) throws DatabaseException;

    public BlockedUser getBlockedUser(long ownerId, long blockedUserId) throws DatabaseException;

    public void delete(BlockedUser blockedUser) throws DatabaseException;

    public Wallet login(String encryptKey) throws DatabaseException, EntityNotFoundException;

    public Wallet login(String email, String password) throws DatabaseException, EntityNotFoundException;

    public Wallet getById(Long id) throws DatabaseException, EntityNotFoundException;

    public Wallet getByUserId(Long userId) throws DatabaseException, EntityNotFoundException;

    public List<Wallet> getByParams(Map<String, Object> params) throws DatabaseException;

    public List<Long> getBlockedUserIdes(Long userId) throws DatabaseException;

    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException;

    public Double checkUserBalance(Long walletId) throws DatabaseException;

    public void update(Wallet wallet) throws DatabaseException, EntityNotFoundException;

    public void resetPassword(Wallet wallet) throws DatabaseException, EntityNotFoundException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
