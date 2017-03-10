package com.connectto.wallet.dataaccess.service.general;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.general.FileData;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface IFileDataManager {

    public void addFileData(FileData fileData) throws InternalErrorException;

    public List<FileData> getByWalletId(Long id) throws InternalErrorException;

    public List<FileData> getByParams(Map<String, Object> params) throws InternalErrorException;

    public FileData getById(long id) throws InternalErrorException, EntityNotFoundException;

    public void updateFileData(FileData fileData) throws InternalErrorException, EntityNotFoundException;

    public void deleteFileData(Long id) throws InternalErrorException, EntityNotFoundException;
}
