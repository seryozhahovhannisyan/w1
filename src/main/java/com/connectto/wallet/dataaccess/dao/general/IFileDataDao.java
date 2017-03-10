package com.connectto.wallet.dataaccess.dao.general;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.general.FileData;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface IFileDataDao {

    public void addFileData(FileData fileData) throws DatabaseException;

    public List<FileData> getByWalletId(Long walletId) throws DatabaseException;

    public List<FileData> getByParams(Map<String, Object> params) throws DatabaseException;

    public FileData getById(long id) throws DatabaseException, EntityNotFoundException;

    public void updateFileData(FileData fileData) throws DatabaseException, EntityNotFoundException;

    public void deleteFileData(Long id) throws DatabaseException, EntityNotFoundException;
}
