package com.connectto.general.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.PartitionDto;

import java.util.List;

/**
 * Created by htdev001 on 6/10/14.
 */
public interface IPartitionSetupDao {

    public Partition getPartitionById(int partitionId) throws DatabaseException, EntityNotFoundException;
    public PartitionDto getPartitionDtoByWalletSetupId(int walletSetupId)throws DatabaseException, EntityNotFoundException;
    public List<Partition> getAvailablePartition(int partitionId) throws DatabaseException;

}
