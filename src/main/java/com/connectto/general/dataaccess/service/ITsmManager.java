package com.connectto.general.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.TsmCompany;

import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface ITsmManager {

    public TsmCompany signIn(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException;

}
