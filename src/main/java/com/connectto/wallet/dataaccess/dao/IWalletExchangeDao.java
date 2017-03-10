package com.connectto.wallet.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.WalletExchange;
import com.connectto.wallet.model.wallet.WalletExchangePending;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IWalletExchangeDao {

    public void add(WalletExchange walletExchange) throws DatabaseException;

    public void add(WalletExchangePending exchangePending) throws DatabaseException;

    public WalletExchange getById(Long walletExchangeId) throws DatabaseException, EntityNotFoundException;

    public List<WalletExchange> getByParams(Map<String, Object> params) throws DatabaseException;

}
