package com.connectto.general.dataaccess.service;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.HttpConnectionDeniedException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface IUserManager {
    //##################################################################################################################
    //USER  Contact
    //##################################################################################################################
    public User getUserByAccountSessionId(String sessionId) throws InternalErrorException, EntityNotFoundException;
    public List<User> getByParams(Map<String, Object> params) throws InternalErrorException;

    public void updateLanguage(User user) throws InternalErrorException, EntityNotFoundException;
    public String logout(User user) throws InternalErrorException, EntityNotFoundException, HttpConnectionDeniedException;
}
