package com.connectto.general.util;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Arthur
 * Date: 12/15/13
 * Time: 5:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSizeLoader implements Serializable {


    private Properties props;

    //pages
    public int thumbWidth = 48;
    public int thumbHeight = 48;

    public int mainWidth = 256;
    public int mainHeight = 256;

    //post
    public int largeWidth = 750;
    public int largeHeight = 750;

    public ImageSizeLoader() {
        createFolderNames();
    }

    public ImageSizeLoader(Properties properties) {
        try {
            this.props = properties;

            thumbWidth = Integer.parseInt(properties.getProperty("thumb.width"));
            thumbHeight = Integer.parseInt(properties.getProperty("thumb.height"));

            mainWidth = Integer.parseInt(properties.getProperty("main.width"));
            mainHeight = Integer.parseInt(properties.getProperty("main.height"));

            largeWidth = Integer.parseInt(properties.getProperty("large.width"));
            largeHeight = Integer.parseInt(properties.getProperty("large.height"));

        } catch (Exception e) {

        } finally {
            createFolderNames();
        }
    }

    private String fName(int width, int height) {
        return width + "x" + height;
    }

    private void createFolderNames() {
        thumb = fName(thumbWidth, thumbWidth);
        main = fName(mainWidth, mainHeight);
        large = fName(largeWidth, largeHeight);
    }

    public final Properties getProperties() {
        return props;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String thumb;
    public String main;
    public String large;

}
