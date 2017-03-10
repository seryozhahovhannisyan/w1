package com.connectto.wallet.dataaccess.dao.transaction.purchase;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.transaction.purchase.PurchaseTicket;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface IPurchaseTicketDao {

    public void add(PurchaseTicket data) throws DatabaseException;

    public PurchaseTicket getById(Long id) throws DatabaseException, EntityNotFoundException;

    public List<PurchaseTicket> getByParams(Map<String, Object> params) throws DatabaseException;

    public int getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void forceDelete(Long id) throws DatabaseException, EntityNotFoundException;

}
