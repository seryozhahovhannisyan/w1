package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.wallet.lcp.WalletProfile;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class WalletProfileTypeHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        parameterSetter.setInt(((WalletProfile) o).getId());
    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? WalletProfile.getDefault() : WalletProfile.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(WalletProfile.class, s);
    }
}
