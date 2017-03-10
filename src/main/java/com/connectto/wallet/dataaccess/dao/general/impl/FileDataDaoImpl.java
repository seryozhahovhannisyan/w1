package com.connectto.wallet.dataaccess.dao.general.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.general.IFileDataDao;
import com.connectto.wallet.model.general.FileData;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 3/7/14.
 */
public class FileDataDaoImpl implements IFileDataDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }


    @Override
    public void addFileData(FileData fileData) throws DatabaseException {
        try {
            sqlMapClient.insert("nsWalletFileData.addFileData", fileData);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<FileData> getByWalletId(Long walletId) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWalletFileData.getByWalletId", walletId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<FileData> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsWalletFileData.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public FileData getById(long id) throws DatabaseException, EntityNotFoundException {
        try {
            FileData fileData = (FileData) sqlMapClient.queryForObject("nsWalletFileData.getById", id);
            if (fileData == null) {
                throw new EntityNotFoundException("Could not found FileData id=" + id);
            }
            return fileData;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateFileData(FileData fileData) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsWalletFileData.updateFileData", fileData);
            if (count != 1) {
                throw new EntityNotFoundException("Could not found updateFileData id=" + fileData.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteFileData(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsWalletFileData.deleteFileData", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not found deleteFileData id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
