package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.WalletLocation;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletLocationDao {

    public void add(WalletLocation walletLocation) throws DatabaseException;

    public boolean isEncryptKeyUsed(String encryptKey) throws DatabaseException;

    public WalletLocation getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<WalletLocation> getByParams(Map<String, Object> params) throws DatabaseException;

    public Integer getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void update(WalletLocation walletLocation) throws DatabaseException, EntityNotFoundException;

    public void delete(Long id) throws DatabaseException, EntityNotFoundException;

}
