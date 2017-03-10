package com.connectto.general.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.CoreSystemAdmin;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface ICoreSystemAdminDao {

    public CoreSystemAdmin getByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException;

}
