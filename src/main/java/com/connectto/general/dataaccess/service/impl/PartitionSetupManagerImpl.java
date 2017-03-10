package com.connectto.general.dataaccess.service.impl;

import com.connectto.general.dataaccess.dao.IPartitionSetupDao;
import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;

/**
 * Created by htdev001 on 6/10/14.
 */
public class PartitionSetupManagerImpl implements IPartitionSetupManager {

    private IPartitionSetupDao partitionSetupDao;

    public void setPartitionSetupDao(IPartitionSetupDao partitionSetupDao) {
        this.partitionSetupDao = partitionSetupDao;
    }

    @Override
    public Partition getPartitionById(int id) throws InternalErrorException, EntityNotFoundException {
        try {
            return partitionSetupDao.getPartitionById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
