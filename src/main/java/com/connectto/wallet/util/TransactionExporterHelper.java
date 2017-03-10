package com.connectto.wallet.util;

import com.connectto.general.model.UserDto;

/**
 * Created by Seryozha on 11/15/2016.
 */
public class TransactionExporterHelper {

    public static final String logoPath = "C:/Users/Serozh/Desktop/comunicator.png";
    public static final String profileAnchor = "C:/Users/Serozh/Desktop/comunicator.png";


    public static UserDto getUserDto( ) {
        UserDto userDto = new UserDto();
        return userDto;
    }

    public static String getText(String t) {
        String[] a = t.split("\\.");
        int l = a.length;
        return a[l - 1] + " " + a[l - 2];
    }
}
