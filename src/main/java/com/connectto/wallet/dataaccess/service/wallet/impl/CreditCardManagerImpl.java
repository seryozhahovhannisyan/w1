package com.connectto.wallet.dataaccess.service.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardDao;
import com.connectto.wallet.dataaccess.service.wallet.ICreditCardManager;
import com.connectto.wallet.model.wallet.CreditCard;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */

@Transactional(readOnly = true)
public class CreditCardManagerImpl implements ICreditCardManager {

    private ICreditCardDao creditCardDao;

    public void setCreditCardDao(ICreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(CreditCard creditCard) throws InternalErrorException {
        try {
            CreditCard max = creditCardDao.getMaxPriorityByWalletId(creditCard.getWalletId());
            int priority = 1;
            if(max !=  null){
                priority += max.getPriority();
            }
            creditCard.setPriority(priority);
            creditCardDao.add(creditCard);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public CreditCard getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return creditCardDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<CreditCard> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return creditCardDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public int getCountByParams(Map<String, Object> params)throws InternalErrorException{
        try {
            return creditCardDao.getCountByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException {
        try {
            creditCardDao.update(creditCard);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void makeDefaultCard(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException {

        boolean found = false;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("walletId", creditCard.getWalletId());

        try {
            List<CreditCard> creditCards = creditCardDao.getByParams(params);
            for(int i = 0; i < creditCards.size(); i++){
                CreditCard card = creditCards.get(i);
                if(card.getId().equals(creditCard.getId())){
                    found = true;
                    creditCards.remove(i);
                    break;
                }
            }

            if (!found) {
                throw new EntityNotFoundException("Could not found CreditCard, CreditCard not exists id=" + creditCard.getId());
            }

            creditCardDao.makeDefaultCard(creditCard);
            for(int i = 0; i < creditCards.size(); i++){
                CreditCard card = creditCards.get(i);
                card.setUpdatedAt(creditCard.getUpdatedAt());
                card.setPriority(2+i);
                card.setUpdatedDesc(";Credit card reordered at " + creditCard.getUpdatedAt().toString());
                creditCardDao.updatePriority(card);
            }

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void reOrderPriorities(List<CreditCard> cards) throws InternalErrorException, EntityNotFoundException {
        try {

            for(int i = 0; i < cards.size(); i++){
                CreditCard card = cards.get(i);
                creditCardDao.updatePriority(card);
            }

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void enable(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException {
        try {
            creditCardDao.enable(creditCard);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void disable(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException {
        try {
            creditCardDao.disable(creditCard);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(CreditCard creditCard) throws InternalErrorException, EntityNotFoundException {
        try {
            creditCardDao.delete(creditCard);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional( readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void forceDelete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            creditCardDao.forceDelete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
