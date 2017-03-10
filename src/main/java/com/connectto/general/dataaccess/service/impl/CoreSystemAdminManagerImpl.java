package com.connectto.general.dataaccess.service.impl;

import com.connectto.general.dataaccess.dao.ICoreSystemAdminDao;
import com.connectto.general.dataaccess.service.ICoreSystemAdminManager;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.CoreSystemAdmin;

import java.util.Map;

/**
 * Created by Serozh on 2/14/2017.
 */
public class CoreSystemAdminManagerImpl implements ICoreSystemAdminManager {

    private ICoreSystemAdminDao coreSystemAdminDao;

    public void setCoreSystemAdminDao(ICoreSystemAdminDao coreSystemAdminDao) {
        this.coreSystemAdminDao = coreSystemAdminDao;
    }

    @Override
    public CoreSystemAdmin signIn(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {
            return coreSystemAdminDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
