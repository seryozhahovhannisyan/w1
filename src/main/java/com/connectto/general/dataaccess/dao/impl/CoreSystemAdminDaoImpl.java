package com.connectto.general.dataaccess.dao.impl;

import com.connectto.general.dataaccess.dao.ICoreSystemAdminDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.CoreSystemAdmin;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Serozh on 2/14/2017.
 */
public class CoreSystemAdminDaoImpl implements ICoreSystemAdminDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public CoreSystemAdmin getByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException {
        try {
            CoreSystemAdmin userDto = (CoreSystemAdmin) sqlMapClient.queryForObject("nsCoreSystemAdmin.getByParams", params);
            if (userDto == null) {
                throw new EntityNotFoundException("Could not sign in system admin by params " + params);
            }
            return userDto;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
