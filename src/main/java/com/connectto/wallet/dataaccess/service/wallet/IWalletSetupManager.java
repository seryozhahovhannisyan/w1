package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.WalletSetup;

import java.util.List;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletSetupManager {

    public void add(WalletSetup walletSetup) throws InternalErrorException;

    public WalletSetup getById(Long id) throws InternalErrorException;
    public WalletSetup getByPartitionId(int id) throws InternalErrorException;
    public List<WalletSetup> getAll() throws InternalErrorException;

    public void update(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException;
    public void updateAvailableRates(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException;

    public void updateIncreaseBalance(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException;

    public void updateDecreaseBalance(WalletSetup walletSetup) throws InternalErrorException, EntityNotFoundException;

}
