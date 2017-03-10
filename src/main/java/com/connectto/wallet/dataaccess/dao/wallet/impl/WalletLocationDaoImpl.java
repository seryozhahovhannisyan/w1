package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletLocationDao;
import com.connectto.wallet.model.wallet.WalletLocation;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class WalletLocationDaoImpl implements IWalletLocationDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletLocation walletLocation) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletLocation.add", walletLocation);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isEncryptKeyUsed(String encryptKey) throws DatabaseException {
        try {
            return (Boolean) sqlMapClient.queryForObject("nsWalletLocation.isEncryptKeyUsed", encryptKey);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public WalletLocation getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            WalletLocation walletLocation = (WalletLocation) sqlMapClient.queryForObject("nsWalletLocation.getById", id);
            if (walletLocation == null) {
                throw new EntityNotFoundException("Could not found WalletLocation, id=" + id);
            }
            return walletLocation;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<WalletLocation> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWalletLocation.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsWalletLocation.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(WalletLocation walletLocation) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWalletLocation.update", walletLocation);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, WalletLocation not exists id=" + walletLocation.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsWalletLocation.delete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, WalletLocation not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
