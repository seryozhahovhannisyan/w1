package com.connectto.general.util;

/**
 * User: Seryozha Hovhannisyan
 * Date: 6/6/13
 * Time: 3:36 PM
 */
public class ConstantGeneral {

    //DB_STATICS
    public static final int DB_STATICS_FALSE = 0;
    public static final int DB_STATICS_TRUE = 1;
    //Languages
    public static final String LANGUAGE_BUNDLE_PATH = "com/connectto/general/config/web/resource/message";
    public static final String REQUEST_LANGUAGE = "lang";
    public static final String LANGUAGE = "language";
    public static final String SESSION_LANGUAGE = "WW_TRANS_I18N_LOCALE";
    public static final String REQUEST_LOCALE = "request_locale";   // for session
    public static final String REQUEST_ONLY_LOCALE = "request_only_locale"; // for the current request only (ignores struts solution for 'request_only_locale')

    public static final int TOKEN_LENGTH = 15;
    public static final int TOKEN_WALLET_LENGTH = 256;

    public static final int COOKIE_SAVED_ACCOUNT_EXPIRED_DAYS = 30;
    public static final String COOKIE_SAVED_ACCOUNT_LOGIN_KEY = "cookieSavedAccountKey";
    public static final String COOKIE_USER_LOGIN_KEYS = "cookieUserLoginKeys";
    //Files
    public static final String MAIN_PATH = "/data";
    public static final String PACKAGE_TEMP = "tmp";
    public static final String FILE_SEPARATOR = "/";

    public static final String WALLET_TRANSACTION_FILE_DATAS = "walletTransactionFileDatas";
    public static final String WALLET_FILE_DATAS = "walletTransactionFileDatas";

    //Messages
    public static final String INFO = "info";
    public static final String ERR_MESSAGE = "err_message";
    public static final String ERR_CODE = "err_code";


    //The keywords for session,request and interceptor parameters
    public static final String SESSION_USER = "session_user";
    public static final String SESSION_MERCHANT_TRANSACTION = "session_merchant_transaction";


    public static final String SESSION_REDIRECT_ACTION_URL = "session_action_url";
    public static final String SESSION_REDIRECT_ACTION_LABEL = "redirect-action-";

    public static final String SESSION_URL_PARTITION = "session_url_partition";
    //public static final String SESSION_URL_PARTITION_DNS = "session_url_partition_dns";

    public static final String SESSION_PARTITION = "session_url_partition";
    //public static final String SESSION_PARTITION_DNS = "session_url_partition_dns";

    public static final String RESET_SESSION_WALLET= "reset_session_wallet";

    public static final String SESSION_URL_TSM_TYPE_KEY = "session_url_tsm_type_key";

    public static final String TRANSPORTATION_LOGIN_INVALID_STATUS = "Check user status, please.";//pending
    public static final String TRANSPORTATION__NO_DATA_FOR_USER = "No data for this user.";
    public static final String TRANSPORTATION__OK = "OK";
    public static final String TRANSPORTATION__ERROR = "error";
    public static final int COOKIE_TIMEOUT_DAYS = 365;


    //
    //WALLET

    //The keywords for session,request and interceptor parameters
    public static final String SESSION_WALLET = "session_wallet";
    public static final String SESSION_CONFIRMED = "confirmed";
    public static final String SESSION_MOBILE = "session_mobile";

    public static final String RETURN_CONTINUE = "continue";
    public static final String PAGE_RETURN_ERROR = "error";

    public static final int WALLET_REQUEST_CODE_LENGTH = 15;
}
