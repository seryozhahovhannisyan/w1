package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class CurrencyTypeHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        int id =  o != null ?((CurrencyType) o).getId() : 0;
        parameterSetter.setInt(id);
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? CurrencyType.getDefault() : CurrencyType.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(CurrencyType.class, s);
    }
}
