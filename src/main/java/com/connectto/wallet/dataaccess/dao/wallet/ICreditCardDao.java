package com.connectto.wallet.dataaccess.dao.wallet;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.model.wallet.CreditCard;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ICreditCardDao {

    public void add(CreditCard creditCard) throws DatabaseException;

    public CreditCard getById(Long id) throws DatabaseException, EntityNotFoundException;

    public CreditCard getMaxPriorityByWalletId(Long walletId) throws DatabaseException;

    public List<CreditCard> getByParams(Map<String, Object> params) throws DatabaseException;

    public int getCountByParams(Map<String, Object> params) throws DatabaseException;

    public void update(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;

    public void enable(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;
    public void disable(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;
    public void blockCreditCard(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;
    public void delete(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;


    public void makeDefaultCard(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;
    public void updatePriority(CreditCard creditCard) throws DatabaseException, EntityNotFoundException;

    public void forceDelete(Long id) throws DatabaseException, EntityNotFoundException;

}
