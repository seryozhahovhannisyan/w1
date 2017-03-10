package com.connectto.general.dataaccess.service.impl;

import com.connectto.general.dataaccess.dao.ITsmDao;
import com.connectto.general.dataaccess.service.ITsmManager;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.TsmCompany;

import java.util.Map;

/**
 * Created by Serozh on 2/14/2017.
 */
public class TsmManagerImpl implements ITsmManager {

    private ITsmDao tsmDao;

    public void setTsmDao(ITsmDao tsmDao) {
        this.tsmDao = tsmDao;
    }

    @Override
    public TsmCompany signIn(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {
            return tsmDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
