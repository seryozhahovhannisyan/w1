package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.wallet.lcp.DisputeState;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class DisputeStateHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((DisputeState) o).getId());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? DisputeState.getDefault() : DisputeState.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(DisputeState.class, s);
    }
}
