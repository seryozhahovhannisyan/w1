package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class TransactionStateHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((TransactionState) o).getId());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? TransactionState.getDefault() : TransactionState.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(TransactionState.class, s);
    }
}
