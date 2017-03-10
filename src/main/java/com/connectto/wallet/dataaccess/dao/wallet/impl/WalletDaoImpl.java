package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.model.wallet.BlockedUser;
import com.connectto.wallet.model.wallet.Wallet;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class WalletDaoImpl implements IWalletDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(Wallet wallet) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWallet.add", wallet);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void add(BlockedUser blockedUser) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWallet.replaceBlockedUser", blockedUser);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BlockedUser getBlockedUser(long ownerId, long blockedUserId) throws DatabaseException {

        Map<String , Object> params = new HashMap<String, Object>();
        params.put("ownerId", ownerId);
        params.put("blockedId", blockedUserId);

        try {
            return  (BlockedUser)sqlMapClient.queryForObject("nsWallet.getBlockedUser", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(BlockedUser blockedUser) throws DatabaseException {
        try {
            sqlMapClient.delete("nsWallet.deleteBlockedUser", blockedUser);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Wallet login(String encryptKey) throws DatabaseException, EntityNotFoundException {
        try {
            Wallet wallet = (Wallet) sqlMapClient.queryForObject("nsWallet.getByEncryptKey", encryptKey);
            if (wallet == null) {
                throw new EntityNotFoundException("Could not found Wallet, encryptKey=" + encryptKey);
            }
            return wallet;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Wallet login(String email, String password) throws DatabaseException, EntityNotFoundException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        params.put("password", password);
        try {
            Wallet wallet = (Wallet) sqlMapClient.queryForObject("nsWallet.login", params);
            if (wallet == null) {
                throw new EntityNotFoundException("Could not found Wallet, email=" + email + ",password=" + password);
            }
            return wallet;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Wallet getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            Wallet wallet = (Wallet) sqlMapClient.queryForObject("nsWallet.getById", id);
            if (wallet == null) {
                throw new EntityNotFoundException("Could not found Wallet, id=" + id);
            }
            return wallet;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Wallet getByUserId(Long userId) throws DatabaseException, EntityNotFoundException {
        try {
            Wallet wallet = (Wallet) sqlMapClient.queryForObject("nsWallet.getByUserId", userId);
            if (wallet == null) {
                throw new EntityNotFoundException("Could not found Wallet, userId=" + userId);
            }
            return wallet;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Wallet> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWallet.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Long> getBlockedUserIdes(Long userId) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWallet.getBlockedUserIdes", userId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsWallet.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Double checkUserBalance(Long walletId) throws DatabaseException {
        try {
            return (Double) sqlMapClient.queryForObject("nsWallet.checkUserBalance", walletId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(Wallet wallet) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWallet.update", wallet);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Wallet not exists id=" + wallet.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }

    @Override
    public void resetPassword(Wallet wallet) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWallet.resetPassword", wallet);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, Wallet not exists wallet.getUserId()=" + wallet.getUserId() );
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsWallet.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, Wallet not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
