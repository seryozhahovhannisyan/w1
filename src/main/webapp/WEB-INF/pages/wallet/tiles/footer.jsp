<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.general.util.Initializer" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev01
  Date: 9/24/2015
  Time: 12:44 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%
    Partition partition = (Partition)session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    int partitionId = partition.getId();
%>
<s:if test="%{!getIsMobile()}">
  <link href="<%=request.getContextPath()%>/css/general/vshoo/footer.css" rel="stylesheet"/>
  <div style="clear: both"></div>
  <div class="container-fluid">
    <div class="row-fluid">
      <footer class="footer">
        <div class="row">
          <div class="text-center col-lg-3 col-md-3 col-sm-3 col-xs-12">
            <div class="icons  ">
              <img style="cursor: pointer" onclick="goTo('<%=partition.getUrlFb()%>')" class="" src="<%=request.getContextPath()%>/img/general/footer/fb.png"/>
              <img style="cursor: pointer" onclick="goTo('<%=partition.getUrlGmail()%>')" class="" src="<%=request.getContextPath()%>/img/general/footer/gplus.png"/>
              <img style="cursor: pointer" onclick="goTo('<%=partition.getUrlTwitter()%>')" class="" src="<%=request.getContextPath()%>/img/general/footer/twitter.png"/>

                <%
                    if(PartitionLCP.isVshoo(partitionId)){
                %>
              <div class="contact_vshoo" onclick="goTo('<%=Initializer.getCorporateVshoo()%>')">
                <img style="cursor: pointer"   src="<%=request.getContextPath()%>/img/general/vshoo/corporat_logo_vsho.png" alt="corporat_logo_vsho"/>
                <span><s:text name="pages.vshoo.main.Contact.vshoo.com">Corparate</s:text></span>
              </div>
              <%
                }
              %>

            </div>
          </div>
          <tiles:insertAttribute name="language"/>
          <div class="copyright col-lg-6 col-md-6  col-sm-6  col-xs-12">
            <div> 
              <s:text  name="hollor.footer.Copyright">Copyright @ 2015ConnectTo Communications inc. All right reserved</s:text>
            </div>
          </div>
        </div>

      </footer>
    </div>
  </div>
</s:if>
