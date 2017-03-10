package com.connectto.general.util.resourse;

import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.general.util.encryption.EncryptException;
import com.connectto.general.util.encryption.SHAHashEnrypt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by htdev001 on 10/21/14.
 */
public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        //cookie.setPath("/");
        cookie.setMaxAge(Utils.dayToSeconds(maxAge));
        response.addCookie(cookie);
    }

    public static void storeUserKeysAsCookie(HttpServletRequest request, HttpServletResponse response, String email, String key) {

        String keys = getCookieValue(request, ConstantGeneral.COOKIE_USER_LOGIN_KEYS + email);
        if (Utils.isEmpty(key)) {
            keys = key;
        } else {
            keys += "," + email;
        }

        removeCookie(response, ConstantGeneral.COOKIE_USER_LOGIN_KEYS);

        Cookie cookie = new Cookie(ConstantGeneral.COOKIE_USER_LOGIN_KEYS + email, keys);
        cookie.setPath("/");
        cookie.setMaxAge(ConstantGeneral.COOKIE_SAVED_ACCOUNT_EXPIRED_DAYS);
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String loginKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains(name)) {
                    addCookie(response, cookie.getName(), null, 0);
                } else if (cookie.getValue().equals(loginKey)) {
                    addCookie(response, cookie.getName(), null, 0);
                }
            }
        }
    }

    /*sign -in-out actions*/
    public static synchronized void storeAccountLoginKey(HttpServletResponse resp, String partitionDns, String profile, String email, String loginKey, boolean savePassword) {

        String encodeEmail;
        try {
            encodeEmail = SHAHashEnrypt.get_MD5_SecurePassword(email);
        } catch (EncryptException e) {
            encodeEmail = email;
        }

        String partitionProfileEmail = partitionDns + "_" + profile + "_" + encodeEmail;

        removeCookie(resp, partitionProfileEmail);
        addCookie(resp, partitionProfileEmail, loginKey, ConstantGeneral.COOKIE_SAVED_ACCOUNT_EXPIRED_DAYS);
        if (savePassword) {
            //for auto entry-login
            removeCookie(resp, partitionDns + "_" + ConstantGeneral.COOKIE_SAVED_ACCOUNT_LOGIN_KEY);
            addCookie(resp, String.format("%s_%s", partitionDns, ConstantGeneral.COOKIE_SAVED_ACCOUNT_LOGIN_KEY), loginKey, ConstantGeneral.COOKIE_SAVED_ACCOUNT_EXPIRED_DAYS);
        }
    }

    public static synchronized int getPartitionStoredAccountsCount(HttpServletRequest request, String partitionDns) {

        int count = 0;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains(partitionDns)) {
                    count++;
                }
            }
        }

        return count;

    }

    public static synchronized String getAccountLoginKey(HttpServletRequest req, String partitionDns, String profile, String email) {

        String encodeEmail;
        try {
            encodeEmail = SHAHashEnrypt.get_MD5_SecurePassword(email);
        } catch (EncryptException e) {
            encodeEmail = email;
        }

        String partitionProfileEmail = partitionDns + "_" + profile + "_" + encodeEmail;

        return getCookieValue(req, partitionProfileEmail);

    }


}
