package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.WalletLocation;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletLocationManager {

    public void add(WalletLocation walletLocation) throws InternalErrorException;

    public WalletLocation getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<WalletLocation> getByParams(Map<String, Object> params) throws InternalErrorException;

    public Integer getCountByParams(Map<String, Object> params) throws InternalErrorException;

    public void update(WalletLocation walletLocation) throws InternalErrorException, EntityNotFoundException;

    public void delete(Long id) throws InternalErrorException, EntityNotFoundException;

}
