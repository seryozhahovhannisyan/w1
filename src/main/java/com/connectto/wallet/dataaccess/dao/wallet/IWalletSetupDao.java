package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.WalletSetup;

import java.util.List;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletSetupDao {

    public void add(WalletSetup walletSetup) throws DatabaseException;

    public WalletSetup getById(Long id) throws DatabaseException;
    public WalletSetup getByPartitionId(int id) throws DatabaseException;
    public List<WalletSetup> getAll() throws DatabaseException;

    public void update(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException;

    public void updateNotNull(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException;

     public void updateAvailableRates(WalletSetup walletSetup) throws DatabaseException, EntityNotFoundException;

}
