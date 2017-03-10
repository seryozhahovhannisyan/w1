package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.creditCard.common.TransferSetting;

/**
 * Created by Serozh on 11/24/2016.
 */
public interface ITransferSettingDao {

    public TransferSetting getByPartitionId(int partitionId) throws DatabaseException, EntityNotFoundException;

}
