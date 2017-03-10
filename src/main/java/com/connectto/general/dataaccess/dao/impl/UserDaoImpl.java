package com.connectto.general.dataaccess.dao.impl;

import com.connectto.general.dataaccess.dao.IUserDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.Account;
import com.connectto.general.model.User;
import com.connectto.general.model.UserDto;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/20/14.
 */
public class UserDaoImpl implements IUserDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }


    @Override
    public User getUserByAccountSessionId(String sessionId) throws DatabaseException, EntityNotFoundException {
        try {
            User user = (User)sqlMapClient.queryForObject("nsUser.getUserByAccountSessionId", sessionId);
            if(user == null){
                throw new EntityNotFoundException("Could not found user by sessionId "+sessionId);
            }
            return user;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public UserDto getUserDtoByWalletId(Long walletId) throws DatabaseException,EntityNotFoundException {
        try {
            UserDto userDto = (UserDto)sqlMapClient.queryForObject("nsUser.getUserDtoByWalletId", walletId);
            if(userDto == null){
                throw new EntityNotFoundException("Could not found user by walletId "+walletId);
            }
            return userDto;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Long> getUserIdesByUNameOrSurname(Map<String,Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsUser.getUserIdesByUNameOrSurname", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<User> getByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsUser.getByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<User> getUserByParams(Map<String, Object> params) throws DatabaseException {
        try {
            return sqlMapClient.queryForList("nsUser.getUserByParams", params);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateLanguage(User user) throws DatabaseException, EntityNotFoundException {
        try {
            int rowCount = sqlMapClient.update("nsUser.updateLanguage", user);
            if(rowCount != 1){
                throw new EntityNotFoundException("Could not updateLanguage user by userId ="+user.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void logoutAccount(Account account) throws DatabaseException, EntityNotFoundException {
        try {
            int rowCount = sqlMapClient.update("nsUser.logoutAccount", account);
            if(rowCount != 1){
                throw new EntityNotFoundException("Could not logout Account id =" + account.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
