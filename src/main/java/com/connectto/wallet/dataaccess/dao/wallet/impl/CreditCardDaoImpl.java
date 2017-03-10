package com.connectto.wallet.dataaccess.dao.wallet.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardDao;
import com.connectto.wallet.model.wallet.CreditCard;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
public class CreditCardDaoImpl implements ICreditCardDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public void add(CreditCard creditCard) throws DatabaseException {
        try {
            sqlMapClient.insert("nsCreditCard.add", creditCard);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public CreditCard getById(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            CreditCard creditCard = (CreditCard) sqlMapClient.queryForObject("nsCreditCard.getById", id);
            if (creditCard == null) {
                throw new EntityNotFoundException("Could not found CreditCard, id=" + id);
            }
            return creditCard;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public CreditCard getMaxPriorityByWalletId(Long walletId) throws DatabaseException {
        try {
            return (CreditCard) sqlMapClient.queryForObject("nsCreditCard.getMaxPriorityByWalletId", walletId);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<CreditCard> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsCreditCard.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getCountByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return (Integer) sqlMapClient.queryForObject("nsCreditCard.getCountByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.update", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not update, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void enable(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.updateSwitches", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not updateSwitches, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void disable(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.updateSwitches", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not updateSwitches, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void blockCreditCard(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.blockCreditCard", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not blockCreditCard, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.updateSwitches", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not updateSwitches, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void makeDefaultCard(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.makeDefaultCard", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not makeDefaultCard, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

     @Override
    public void updatePriority(CreditCard creditCard) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.update("nsCreditCard.updatePriority", creditCard);
            if (count != 1) {
                throw new EntityNotFoundException("Could not updatePriority, CreditCard not exists id=" + creditCard.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void forceDelete(Long id) throws DatabaseException, EntityNotFoundException {
        try {
            int count = sqlMapClient.delete("nsCreditCard.forceDelete", id);
            if (count != 1) {
                throw new EntityNotFoundException("Could not delete, CreditCard not exists id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
