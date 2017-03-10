package com.connectto.general.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;

/**
 * Created by htdev001 on 6/10/14.
 */
public interface IPartitionSetupManager {

    public Partition getPartitionById(int id) throws InternalErrorException, EntityNotFoundException;

}
