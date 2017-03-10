package com.connectto.general.dataaccess.dao;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.Account;
import com.connectto.general.model.User;
import com.connectto.general.model.UserDto;

import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
public interface IUserDao {

    public User getUserByAccountSessionId(String sessionId)  throws DatabaseException, EntityNotFoundException;
    public UserDto getUserDtoByWalletId(Long walletId)  throws DatabaseException, EntityNotFoundException;

    public List<Long> getUserIdesByUNameOrSurname(Map<String, Object> params) throws DatabaseException;
    public List<User> getByParams(Map<String, Object> params) throws DatabaseException;
    public List<User> getUserByParams(Map<String, Object> params) throws DatabaseException;


    public void updateLanguage(User user) throws DatabaseException, EntityNotFoundException;
    public void logoutAccount(Account account) throws DatabaseException, EntityNotFoundException;

}
