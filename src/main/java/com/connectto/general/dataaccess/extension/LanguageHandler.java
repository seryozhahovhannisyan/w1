package com.connectto.general.dataaccess.extension;

import com.connectto.general.model.lcp.Language;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class LanguageHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((Language) o).getValue());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? Language.getDefault() : Language.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(Language.class, s);
    }
}
