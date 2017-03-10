package com.connectto.wallet.dataaccess.dao.transaction.transfer;

import com.connectto.general.exception.DatabaseException;
import com.connectto.wallet.model.transaction.transfer.TransferTicket;

/**
 * Created by Serozh on 2/14/2017.
 */
public interface ITransferTicketDao {

    public void add(TransferTicket data) throws DatabaseException;

}
