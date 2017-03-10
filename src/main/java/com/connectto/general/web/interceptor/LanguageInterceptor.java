package com.connectto.general.web.interceptor;

import com.connectto.general.model.Partition;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.general.util.Utils;
import com.connectto.general.util.resourse.CookieUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

public class LanguageInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {

        ActionContext actionContext = actionInvocation.getInvocationContext();

        Map<String, Object> session = actionContext.getSession();
        Map<String, Object> parameters = actionContext.getParameters();

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        int partitionId = 0;
        if (partition != null) {
            partitionId = partition.getId();
        }
        //generate partition language key for store
        String langKey = Generator.generateLanguageStoreKey(ConstantGeneral.LANGUAGE, partitionId);

        // if any parameters bellow mentioned exist, it means employer is changing the resource
        if (parameters.containsKey(ConstantGeneral.REQUEST_LOCALE) ||
                parameters.containsKey(ConstantGeneral.REQUEST_ONLY_LOCALE) ||
                parameters.containsKey(ConstantGeneral.REQUEST_LANGUAGE)) {

            String actionLanguage;
            if (parameters.containsKey(ConstantGeneral.REQUEST_LOCALE)) {
                actionLanguage = ((String[]) parameters.get(ConstantGeneral.REQUEST_LOCALE))[0];
            } else if (parameters.containsKey(ConstantGeneral.REQUEST_ONLY_LOCALE)) {
                actionLanguage = ((String[]) parameters.get(ConstantGeneral.REQUEST_ONLY_LOCALE))[0];
            } else {
                actionLanguage = ((String[]) parameters.get(ConstantGeneral.REQUEST_LANGUAGE))[0];
            }

            // retrieves resource
            Language lang = Language.languageOf(actionLanguage);
            Utils.setLanguage(session, lang, true, partitionId);
            //setLanguageOld(session, lang, true,langKey);
            return actionInvocation.invoke();
        }

        //retrieves client preferred resource if it's defined
        //gets 'Language' via resource
        String language = CookieUtil.getCookieValue(ServletActionContext.getRequest(), langKey);
        if(!Utils.isEmpty(language)){
            Language lang = Language.languageOf(language);
            Utils.setLanguage(session, lang, false,partitionId);
            //setLanguageOld(session, lang, true,langKey);
            return actionInvocation.invoke();
        }

        // stores default resource in session and cookie if it doesn't exist
        if (session.isEmpty() || !session.containsKey(langKey)) {
            // gets default resource
            Language lang = Language.getDefault();
            Utils.setLanguage(session, lang, true,partitionId);
            //setLanguageOld(session, lang, true,langKey);
        }

        return actionInvocation.invoke();
    }

    /**
     * Stores chosen resource into session and cookie,
     * if resource exists in any storage it will be replaced.
     *
     * @param session        (session mapped data)
     * @param lang           (chosen resource)
     * @param storeInCookies
     */
    private void setLanguageOld_(Map<String, Object> session, Language lang, boolean storeInCookies, String langKey) {

        Language sessionLang = (Language) session.get(langKey);

        // if resource not stored in session
        // or it is not matched with chosen resource
        if (sessionLang == null || sessionLang != lang) {

            // sets resource for client usage
            session.put(langKey, lang);
            // sets locale for application localization (i18n)
            // sets locale for application localization (i18n)
            session.put(ConstantGeneral.SESSION_LANGUAGE, lang.getLocale());
            ActionContext.getContext().setLocale(lang.getLocale());

        }

        if (storeInCookies) {
            // add resource cookie
            CookieUtil.addCookie(ServletActionContext.getResponse(), langKey, lang.getLocale().getLanguage(), ConstantGeneral.COOKIE_TIMEOUT_DAYS);
        }
    }
}
