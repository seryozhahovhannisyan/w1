package com.connectto.general.dataaccess.extension;

import com.connectto.general.model.lcp.Status;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class StatusHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((Status) o).getKey());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? Status.getDefault() : Status.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(Status.class, s);
    }
}
