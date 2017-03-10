package com.connectto.wallet.web.action.wallet.transaction.util;

import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.encryption.WalletEncription;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransactionDecripter {


    public static String decript(String output) throws EncryptException {
        return WalletEncription.decrypt(output);
    }

}
