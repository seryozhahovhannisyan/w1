package com.connectto.wallet.dataaccess.dao.general;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogLevel;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 14/11/14.
 */
public interface IWalletLoggerDao {
    public void add(WalletLogger logger) throws DatabaseException;

    public void addAll(List<WalletLogger> logger) throws DatabaseException;

    public List<WalletLogger> getAll() throws DatabaseException;

    public List<WalletLogger> getByUserId(Long id) throws DatabaseException;

    public List<WalletLogger> getByUserIdAndLevel(Long id, LogLevel logLevel) throws DatabaseException;

    public List<WalletLogger> getByParams(Map<String, Object> params) throws DatabaseException;

    public WalletLogger getById(Long id) throws DatabaseException, EntityNotFoundException;
}
