package com.connectto.general.web.interceptor;

import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.model.Partition;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.general.util.resourse.BeanProvider;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeansException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by htdev001 on 4/1/14.
 */
public class PartitionInterceptor implements Interceptor {

    private static final Logger logger = Logger.getLogger(PartitionInterceptor.class.getSimpleName());

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception{

        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        HttpServletRequest httpServletRequest = ServletActionContext.getRequest();

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);
        if(partition == null){
            String serverName = new String(httpServletRequest.getServerName());
            if(serverName.contains("127.0.0.1")  || serverName.contains("192.168.40.25")  || serverName.contains("192.168.1.110")  || serverName.contains("localhost") || serverName.contains("204.174.104.63")){
                logger.info("DEV MODE");
                serverName = "hollor.com";
            }

            PartitionLCP partitionLCP = PartitionLCP.findCorrespondingPartitionLCP(serverName);
            session.put("lcp", partitionLCP);
            try {
                IPartitionSetupManager partitionSetupManager = BeanProvider.getPartitionSetupManager();
                partition = partitionSetupManager.getPartitionById(partitionLCP.getId());
                int partitionId = partition.getId();
                //set default language from partition setup
                Language language = partition.getLanguage();
                Utils.setLanguage(session, language, true, partitionId);

                partition.setServerName(serverName);
                //image valid path
                String pUrl = partition.getPartitionServerUrl();
                if(pUrl.contains("https:")) {
                    String dns = pUrl.replace("https:", "");
                    int indexOfPort = dns.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        dns = dns.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl("https:"+ dns);
                } else if(pUrl.contains("http:")) {
                    String dns = pUrl.replace("http:", "");
                    int indexOfPort = dns.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        dns = dns.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl("http:"+ dns);
                } else {
                    int indexOfPort = pUrl.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        pUrl = pUrl.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl(pUrl);
                }
            } catch (BeansException e) {
                logger.error(e);
            }
            session.put(ConstantGeneral.SESSION_URL_PARTITION, partition);
        }
        return actionInvocation.invoke();
    }
}