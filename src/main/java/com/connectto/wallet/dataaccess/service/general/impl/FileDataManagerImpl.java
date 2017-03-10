package com.connectto.wallet.dataaccess.service.general.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.general.IFileDataDao;
import com.connectto.wallet.dataaccess.service.general.IFileDataManager;
import com.connectto.wallet.model.general.FileData;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
@Transactional(readOnly = true)
public class FileDataManagerImpl implements IFileDataManager {

    private IFileDataDao fileDataDao;

    public void setFileDataDao(IFileDataDao fileDataDao) {
        this.fileDataDao = fileDataDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addFileData(FileData fileData) throws InternalErrorException {
        try {
            fileDataDao.addFileData(fileData);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<FileData> getByWalletId(Long id) throws InternalErrorException {
        try {
            return fileDataDao.getByWalletId(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<FileData> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return fileDataDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public FileData getById(long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return fileDataDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateFileData(FileData fileData) throws InternalErrorException, EntityNotFoundException {
        try {
            fileDataDao.updateFileData(fileData);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteFileData(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            fileDataDao.deleteFileData(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
