package com.connectto.general.web.action;

import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by htdev001 on 10/23/14.
 */
public class LocaleAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(LocaleAction.class.getSimpleName());
    private IUserManager userManager;

    private String url;
    private String lang;

    public String execute() {

        HttpServletRequest req = getHttpServletRequest();

        String referer = req.getHeader("referer");
        int indexOfAction = referer.lastIndexOf("/");
        url = referer.substring(indexOfAction + 1);

        logger.info(url);

        Language selectedLanguage = Language.keyOf(lang);
        //if url's header already contained lang param, replace it current lang
        if (url.contains("?lang=")) {
            int uLangIndex = url.indexOf("?lang=");
            int langLength = "?lang=".length();

            String uLang =  url.substring(uLangIndex + langLength, uLangIndex + langLength + 2);
            if(!uLang.equals(lang)){
                url = url.replace(url.substring(0, uLangIndex + langLength ) + uLang, url.substring(0, uLangIndex + langLength ) + lang);
            }
        } else if (url.contains("&lang=")) {
            int uLangIndex = url.indexOf("&lang=");
            int langLength = "&lang=".length();

            String uLang =  url.substring(uLangIndex + langLength, uLangIndex + langLength + 2);
            if(!uLang.equals(lang)){
                url = url.replace(url.substring(0, uLangIndex + langLength ) + uLang, url.substring(0, uLangIndex + langLength ) + lang);
            }
        }

        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (user != null) {
            user.setLanguage(selectedLanguage);
            try {
                userManager.updateLanguage(user);
            } catch (InternalErrorException e) {
                logger.error(e);
            } catch (EntityNotFoundException e) {
                logger.error(e);
            }
        }

        return SUCCESS;
    }

    public String getUrl() {
        return url;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }
}
