package com.connectto.general.dataaccess.dao.impl;

import com.connectto.general.dataaccess.dao.ITsmDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.model.TsmCompany;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Serozh on 2/14/2017.
 */
public class TsmDaoImpl implements ITsmDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Override
    public TsmCompany getByParams(Map<String, Object> params) throws DatabaseException, EntityNotFoundException {
        try {
            TsmCompany tsmCompany = (TsmCompany) sqlMapClient.queryForObject("nsTsm.getByParams", params);
            if (tsmCompany == null) {
                throw new EntityNotFoundException("Could not sign in tsm company by params " + params);
            }
            return tsmCompany;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
