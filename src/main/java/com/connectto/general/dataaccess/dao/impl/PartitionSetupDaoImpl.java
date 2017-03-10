package com.connectto.general.dataaccess.dao.impl;

import com.connectto.general.dataaccess.dao.IPartitionSetupDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.PartitionDto;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by htdev001 on 6/10/14.
 */

public class PartitionSetupDaoImpl implements IPartitionSetupDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public Partition getPartitionById(int partitionId) throws DatabaseException, EntityNotFoundException {
        try {
            Partition partition = (Partition) sqlMapClient.queryForObject("nsPartitionSetup.getPartitionById", partitionId);

            if (partition == null) {
                throw new EntityNotFoundException(String.format("Could not find Partition id=[%d]", partitionId));
            }

            return partition;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public PartitionDto getPartitionDtoByWalletSetupId(int walletSetupId) throws DatabaseException, EntityNotFoundException {
        try {
            PartitionDto partitionDto = (PartitionDto) sqlMapClient.queryForObject("nsPartitionSetup.getPartitionDtoByWalletSetupId", walletSetupId);

            if (partitionDto == null) {
                throw new EntityNotFoundException(String.format("Could not find PartitionDto walletSettupId=[%d]", walletSetupId));
            }

            return partitionDto;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Partition> getAvailablePartition(int partitionId) throws DatabaseException {
        try {
            return   sqlMapClient.queryForList("nsPartitionSetup.getAvailablePartition", partitionId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
