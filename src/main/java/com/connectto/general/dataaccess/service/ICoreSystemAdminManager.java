package com.connectto.general.dataaccess.service;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.CoreSystemAdmin;

import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface ICoreSystemAdminManager {

    public CoreSystemAdmin signIn(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException;

}
