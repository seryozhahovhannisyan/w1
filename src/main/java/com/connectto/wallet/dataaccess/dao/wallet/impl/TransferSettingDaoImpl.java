package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.creditCard.common.TransferSetting;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardDao;
import com.connectto.wallet.dataaccess.dao.wallet.ITransferSettingDao;
import com.connectto.wallet.model.wallet.CreditCard;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class TransferSettingDaoImpl implements ITransferSettingDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }



    @Override
    public TransferSetting getByPartitionId(int partitionId) throws DatabaseException, EntityNotFoundException {
        try {
            TransferSetting transferSetting = (TransferSetting) sqlMapClient.queryForObject("nsTransferSetting.getByPartitionId", partitionId);
            if (transferSetting == null) {
                throw new EntityNotFoundException("Could not found transferSetting, id=" + partitionId);
            }
            return transferSetting;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
