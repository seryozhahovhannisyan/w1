package com.connectto.general.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Arthur
 * Date: 2/17/14
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetupInfo implements Serializable {

    private Properties props;

    //pages
    public String setup = "dev";
    public String version = "1.0";
    public String staticDir = ".";
    public String staticDirProd = "/opt/tomcat/webapps";//".";
    public int paginationCount = 10;
    public int bannerItemCount = 4;
    public String coreUrl = "https://mobile.vshoo.com";
    public String corePartitionImgMainDir = "staticData/core/partitionImages/";
    public String corporateVshoo = "https://corporate.vshoo.com";

    public SetupInfo() {
    }

    public SetupInfo(Properties properties) {
        try {
            this.props = properties;
            setup = properties.getProperty("setup");
            version = properties.getProperty("version");
            staticDir = properties.getProperty("static.dir");
            staticDirProd = properties.getProperty("static.dir.prod");
            coreUrl = properties.getProperty("core.url");
            corePartitionImgMainDir = properties.getProperty("core.partition.img.main.dir");
            paginationCount = Integer.parseInt(properties.getProperty("pagination.count"));
            bannerItemCount = Integer.parseInt(properties.getProperty("banner.item.count"));
            corporateVshoo = properties.getProperty("corporate.vshoo.com");
        } catch (Exception e) {

        }
    }

    private String fName(int width, int height) {
        return width + "x" + height;
    }

    public final Properties getProperties() {
        return props;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getSetup() {
        return setup;
    }

    public String getVersion() {
        return version;
    }

    public String getStaticDir() {
        return staticDir;
    }

    public int getPaginationCount() {
        return paginationCount;
    }

    /*public int getPaginationGuideCount() {
        return paginationGuideCount;
    }*/

    public int getBannerItemCount() {
        return bannerItemCount;
    }

    public String getStaticDirProd() {
        return staticDirProd;
    }

}
