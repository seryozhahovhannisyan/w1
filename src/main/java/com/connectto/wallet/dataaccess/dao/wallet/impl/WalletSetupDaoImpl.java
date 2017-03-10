package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by htdev001 on 11/25/14.
 */
public class WalletSetupDaoImpl implements IWalletSetupDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(WalletSetup walletSetup) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletSetup.add", walletSetup);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public WalletSetup getById(Long id) throws DatabaseException {
        try {
            return (WalletSetup) sqlMapClient.queryForObject("nsWalletSetup.getById", id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public WalletSetup getByPartitionId(int id) throws DatabaseException {
        try {
            return (WalletSetup) sqlMapClient.queryForObject("nsWalletSetup.getByPartitionId", id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<WalletSetup> getAll() throws DatabaseException {
        try {
            return  sqlMapClient.queryForList("nsWalletSetup.getAll");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWalletSetup.update", walletSetup);
            if (count != 1) {
                throw new EntityNotFoundException("The walletSetup not exists (id=" + walletSetup.getId() + ") partitionId = " + walletSetup.getPartitionId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateNotNull(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWalletSetup.updateNotNull", walletSetup);
            if (count != 1) {
                throw new EntityNotFoundException("The walletSetup not exists (id=" + walletSetup.getId() + ") partitionId = " + walletSetup.getPartitionId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateAvailableRates(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWalletSetup.updateAvailableRates", walletSetup);
            if (count != 1) {
                throw new EntityNotFoundException("The walletSetup not exists (id=" + walletSetup.getId() + ") ");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
