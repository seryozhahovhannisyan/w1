package com.connectto.general.util;

import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.util.resourse.CookieUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getSimpleName());

    private static long aDayMills = 60 * 60 * 24 * 1000L;
    private static DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static DateFormat viewDateFormat = new SimpleDateFormat("dd:MM:yyyy");
    private static DateFormat creditCard = new SimpleDateFormat("MMyy");

    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    private static DateFormat merchantDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //2014-03-18 16:42:31.673098-07
    private static DateFormat freeSwitchDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
    private static DateFormat javaScriptDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");//"EEE, d MMM yyyy HH:mm:ss Z"    Wed, 4 Jul 2001 12:08:56 -0700
    private static DateFormat guideDateFormat = new SimpleDateFormat("yyyyMMddHHmm`ss");//20150301235959

    private static Pattern expDigit = Pattern.compile("[0-9]");
    private static Pattern expNumeric = Pattern.compile("[0-9]*");

    public static synchronized int safeLongToInt(long l) throws InvalidParameterException {//IllegalArgumentException
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new InvalidParameterException(String.format("%d cannot be cast to int without changing its value.", l));
        }
        return (int) l;
    }

    public static synchronized int safeStringToInt(String str) throws InvalidParameterException {//IllegalArgumentException
        if (isEmpty(str)) {
            throw new InvalidParameterException("Empty String could not be cast to int");
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            new InvalidParameterException(String.format("%s cannot be cast to int ", str));
        }
        return -1;
    }

    public static synchronized Long safeStringToLong(String str) throws InvalidParameterException {//IllegalArgumentException
        if (isEmpty(str)) {
            throw new InvalidParameterException("Empty String could not be cast to Long");
        }
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            new InvalidParameterException(String.format("%s cannot be cast to Long ", str));
        }
        return -1l;
    }


    public static synchronized boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static synchronized boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static synchronized boolean isEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public static synchronized boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static synchronized boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return password.length() >= 6;
    }

    public static synchronized boolean isValidBankCode(String code) {
        return true;
    }

    public static synchronized boolean isValidIPAddress(String ipAddress) {
        return ipAddress != null && !"0:0:0:0:0:0:0:1".equals(ipAddress) && !"127.0.0.1".equals(ipAddress);
    }

    public static synchronized boolean isValidOrderType(String orderType) {
        return "asc".equals(orderType) || "desc".equals(orderType);
    }

    public static synchronized boolean isValidMACAddress(String macAddress) {//todo
        return macAddress != null;
    }

    public static synchronized String toString(Date date) {

        return date == null
                ? null
                : dateFormat.format(date);
    }

    public static synchronized Date toDate(String strDate) {
        try {
            return Utils.isEmpty(strDate)
                    ? null
                    : dateFormat.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized String toMerchantDate(Date date) {
        return date == null
                ? null
                : merchantDateFormat.format(date);
    }

    public static synchronized String toFreeSwitchDate(Date date) {
        return date == null
                ? null
                : freeSwitchDateFormat.format(date);
    }

    public static synchronized String toJavaScriptDate(Date date) {
        return date == null
                ? null
                : javaScriptDateFormat.format(date);
    }

    public static synchronized String toGuideDate(Date date) {
        return date == null
                ? null
                : guideDateFormat.format(date);
    }

    public static synchronized Date toSimpleDate(String strDate) {

        try {
            return Utils.isEmpty(strDate)
                    ? null
                    : simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            logger.error(e);
            return null;
            //throw new RuntimeException(e);
        }
    }

    public static synchronized String toViewDate(Date date) {
        return date == null
                ? null
                : viewDateFormat.format(date);
    }

    public static synchronized String toCreditCardDate(Date date) {
        return date == null
                ? null
                : creditCard.format(date);
    }

    public static synchronized Date toDateyyyyMMddhhmmssS(String strDate) {
        try {
            return Utils.isEmpty(strDate)
                    ? null
                    : freeSwitchDateFormat.parse(strDate);
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }
    }

    /**
     * Calculates seconds for given days
     *
     * @param day
     * @return <code>java.lang.Integer</code>
     */
    public static synchronized int dayToSeconds(int day) {
        return 60 * 60 * 24 * day;
    }

    public static synchronized long dayToMilliSeconds(int day) {
        return aDayMills * day;
    }

    public static synchronized int secondToMilliSeconds(int second) {
        return second * 1000;
    }

    public static synchronized Date getAfterSecunds(Date date, Long seconds) throws InvalidParameterException {
        if (date == null || seconds == null) {
            throw new InvalidParameterException("Empty Parameter date or second");
        }

        return new Date(date.getTime() + (seconds * 1000));
    }

    public static synchronized Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    public static synchronized Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static synchronized Date getWeekStartDay() {
        Calendar first = Calendar.getInstance();
        first.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - first.get(Calendar.DAY_OF_WEEK));
        return getStartOfDay(first.getTime());
    }

    public static synchronized Date getWeekEndDay() {
        Calendar first = Calendar.getInstance();
        first.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - first.get(Calendar.DAY_OF_WEEK));
        first.add(Calendar.DAY_OF_WEEK, 6);
        return getEndOfDay(first.getTime());
    }

    public static synchronized Date getDaysBeforeStart(int day) {
        return getStartOfDay(new Date(System.currentTimeMillis() - (day * aDayMills)));
    }

    public static synchronized Date getDaysAfterEnd(int day) {
        return getEndOfDay(new Date(System.currentTimeMillis() + (day * aDayMills)));
    }

    public static synchronized int getDifferenceInDays(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        double diffDays = (double) diffTime / (double) aDayMills;
        return (int) Math.ceil(diffDays);
    }

    public static boolean isDigit(String str) {
        return !Utils.isEmpty(str)
                ? expDigit.matcher(str).matches()
                : false;
    }

    public static synchronized boolean isNumeric(String str) {
        return !Utils.isEmpty(str)
                ? expNumeric.matcher(str).matches()
                : false;
    }

    public static synchronized boolean isEmailAddress(String email) {
        return email != null && !email.isEmpty()
                && email.matches("^[a-z,A-Z]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z0-9]{2,4})$");
    }

    public static synchronized boolean isPositiveNumber(String str) {
        return str != null && str.matches("[0-9]+");
    }

    public static synchronized boolean isPhoneNumber(String str) {
        if (str != null && str.startsWith("+")) {
            str = str.substring(1);
        }
        return str != null && str.matches("[0-9]+");
    }

    public static synchronized int diffMonth(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int months1 = cal1.get(Calendar.YEAR) * 12 + cal1.get(Calendar.MONTH);
        int months2 = cal2.get(Calendar.YEAR) * 12 + cal2.get(Calendar.MONTH);

        return months2 - months1;
    }

    public static synchronized Date getStartToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static synchronized Date getEndToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static synchronized Date getStartCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static synchronized Date getEndCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static double getGeoRadiusOfMetre(int metre) {
        double oneRadius = 111000d;//
        return metre / oneRadius;
    }

    public static synchronized <T> String getEnumLangName(T field) {
        return String.format("enum.%s.%s", field.getClass().getSimpleName(), field);
    }

    public static synchronized <T> String[] getEnumLangName(Class<T> type) {
        if (!type.isEnum()) {
            return null;
        }
        T[] constants = type.getEnumConstants();
        String[] val = new String[constants.length];

        for (int i = 0; i < constants.length; i++) {
            T t = constants[i];
            val[i] = String.format("enum.%s.%s", t.getClass().getSimpleName(), t);
        }
        return val;
    }

    public static synchronized String getPartOfDomainName(String domainName) {
        int i = domainName.indexOf(".x");
        if (i < 1) {
            return null;
        }
        String dn = domainName.substring(i);

        return dn;
    }

    public static String convertDoubleToViewString(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    /**
     * Stores chosen resource into session and cookie,
     * if resource exists in any storage it will be replaced.
     *
     * @param session        (session mapped data)
     * @param lang           (chosen resource)
     * @param storeInCookies
     */
    public static synchronized void setLanguage(Map<String, Object> session, Language lang, boolean storeInCookies, int partitionId) {

        //generate partition language store key
        String langKey = Generator.generateLanguageStoreKey(ConstantGeneral.LANGUAGE, partitionId);
        Language sessionLang = (Language) session.get(langKey);//!Utils.isEmpty(lKey) ? Language.keyOf(lKey) : Language.getDefault();

        // if resource not stored in session
        // or it is not matched with chosen resource
        if (sessionLang == null || sessionLang != lang) {
            // sets resource for client usage
            session.put(langKey, lang);
            // sets locale for application localization (i18n)
            session.put(ConstantGeneral.LANGUAGE, lang);
            // sets locale for application localization (i18n)
            session.put(ConstantGeneral.SESSION_LANGUAGE, lang.getLocale());
            ActionContext.getContext().setLocale(lang.getLocale());

        }

        if (storeInCookies) {
            // add resource cookie
            CookieUtil.addCookie(ServletActionContext.getResponse(), langKey, lang.getLocale().getLanguage(), ConstantGeneral.COOKIE_TIMEOUT_DAYS);
        }
    }

    /**
     * Stores chosen resource into session and cookie,
     * if resource exists in any storage it will be replaced.
     *
     * @param lang           (chosen resource)
     * @param storeInCookies
     */
    public static synchronized void setLanguage(HttpServletRequest req, HttpServletResponse resp, Language lang, boolean storeInCookies, int partitionId) {
        HttpSession session = req.getSession();
        //generate partition language store key
        String langKey = Generator.generateLanguageStoreKey(ConstantGeneral.LANGUAGE, partitionId);
        Language sessionLang = (Language) session.getAttribute(langKey);//!Utils.isEmpty(lKey) ? Language.keyOf(lKey) : Language.getDefault();

        // if resource not stored in session
        // or it is not matched with chosen resource
        if (sessionLang == null || sessionLang != lang) {
            // sets resource for client usage
            session.setAttribute(langKey, lang);
            // sets locale for application localization (i18n)
            session.setAttribute(ConstantGeneral.LANGUAGE, lang);
            // sets locale for application localization (i18n)
            session.setAttribute(ConstantGeneral.SESSION_LANGUAGE, lang.getLocale());
            ActionContext ctx = ActionContext.getContext();
            if (ctx != null) {
                ctx.setLocale(lang.getLocale());
            }

        }

        if (storeInCookies) {
            // add resource cookie
            CookieUtil.addCookie(resp, langKey, lang.getLocale().getLanguage(), ConstantGeneral.COOKIE_TIMEOUT_DAYS);
        }
    }

    public static void main(String[] args) {
        System.out.println(toCreditCardDate(new Date(System.currentTimeMillis())));
    }
}