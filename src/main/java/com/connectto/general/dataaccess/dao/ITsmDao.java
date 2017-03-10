package com.connectto.general.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.TsmCompany;

import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface ITsmDao {

    public TsmCompany getByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException;

}
