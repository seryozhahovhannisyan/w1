package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.general.lcp.LogLevel;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class LogLevelTypeHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((LogLevel) o).getKey());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? LogLevel.getDefault() : LogLevel.valueOfKey(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(LogLevel.class, s);
    }
}
