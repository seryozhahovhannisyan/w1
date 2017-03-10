package com.connectto.wallet.dataaccess.service.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.model.wallet.CreditCard;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public interface ICreditCardManager {

    public void add(CreditCard creditCard) throws InternalErrorException;

    public CreditCard getById(Long id) throws InternalErrorException, EntityNotFoundException;

    public List<CreditCard> getByParams(Map<String, Object> params) throws InternalErrorException;

    public int getCountByParams(Map<String, Object> params) throws InternalErrorException;

    public void update(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException;

    public void makeDefaultCard(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException;
    public void reOrderPriorities(List<CreditCard> cards) throws InternalErrorException, EntityNotFoundException;

    public void enable(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException;
    public void disable(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException;
    public void delete(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException;

    public void forceDelete(Long id) throws InternalErrorException, EntityNotFoundException;

}
