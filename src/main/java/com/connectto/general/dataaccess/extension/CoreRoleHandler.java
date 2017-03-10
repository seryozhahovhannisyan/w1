package com.connectto.general.dataaccess.extension;

import com.connectto.general.model.lcp.CoreRole;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class CoreRoleHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((CoreRole) o).getRoleId());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? null : CoreRole.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(CoreRole.class, s);
    }
}
