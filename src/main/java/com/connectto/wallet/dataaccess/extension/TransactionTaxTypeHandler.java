package com.connectto.wallet.dataaccess.extension;

import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Created by htdev001 on 2/24/14.
 */
public class TransactionTaxTypeHandler implements TypeHandlerCallback {
    @Override
    public void setParameter(ParameterSetter parameterSetter, Object o) throws SQLException {
        //int id =  o != null ?(( : 0;
        if(o != null){
            parameterSetter.setInt(((TransactionTaxType) o).getId());
        } else {
            parameterSetter.setObject(o);
        }

    }

    @Override
    public Object getResult(ResultGetter resultGetter) throws SQLException {
        int id = resultGetter.getInt();
        return resultGetter.wasNull() ? null : TransactionTaxType.valueOf(id);
    }

    @Override
    public Object valueOf(String s) {
        return Enum.valueOf(TransactionTaxType.class, s);
    }
}
