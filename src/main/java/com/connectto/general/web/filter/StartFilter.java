package com.connectto.general.web.filter;

import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.general.util.resourse.BeanProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by htdev001 on 9/19/14.
 */
public class StartFilter implements Filter {

    private static final Logger logger = Logger.getLogger(StartFilter.class.getSimpleName());

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_PARTITION);
        if(partition == null){

            String serverName = new String(request.getServerName());
            if(serverName.contains("127.0.0.1")  || serverName.contains("192.168.40.25")  || serverName.contains("192.168.1.110")  || serverName.contains("localhost") || serverName.contains("204.174.104.63")){
                logger.info("DEV MODE");
                serverName = "hollor.com";
            }

            PartitionLCP partitionLCP = PartitionLCP.findCorrespondingPartitionLCP(serverName);

            try {

                IPartitionSetupManager partitionSetupManager = BeanProvider.getPartitionSetupManager();
                partition = partitionSetupManager.getPartitionById(partitionLCP.getId());
                int partitionId = partition.getId();
                //set default language from partition setup
                Language language = partition.getLanguage();
                Utils.setLanguage(request, response, language, true, partitionId);

                partition.setServerName(serverName);

                logger.info("FILTER : partition"+partition);
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
            } catch (EntityNotFoundException e) {
                logger.error(e);
            } catch (InternalErrorException e) {
                logger.error(e);
            }
            session.setAttribute(ConstantGeneral.SESSION_PARTITION, partition);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }

}