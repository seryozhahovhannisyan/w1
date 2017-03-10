package com.connectto.general.util;


import com.connectto.general.model.SetupInfo;
import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

//import javax.servlet.SessionTrackingMode;


public class Initializer implements ServletContextListener {

    /**
     * application context object (container)
     */
    private static ApplicationContext applicationContext;

    private static Logger logger = Logger.getLogger(Initializer.class);

    /**
     *
     */
    public static ServletContext context;

    public static String WALLET_DATA_FOLDER = "walletData";

    public static String WALLET_UPLOAD_FOLDER = "wallet";
    public static String CONFIG_FOLDER = "config";

    public static String WALLET_TRANSACTION_FOLDER = "transaction";
    public static String WALLET_DISPUTE_FOLDER = "dispute";
    public static String WALLET_EXCHANGE_SOURCE = "exchange";

    private static String contextPath;
    private static String dataPath;
    private static String imageContextPath;

    private static final String PROD = "prod";
    private static final String DEV = "dev";

    private final static String VELOCITY_INPUT_ENCODING = "UTF-8";
    private final static String VELOCITY_OUTPUT_ENCODING = "UTF-8";

    private static int paginationCount;
    private static int bannerItemCount;
    private static String corporateVshoo;

    public static String BULK_SMS_USERNAME = "aramter";
    public static String BULK_SMS_PASSWORD = "Art22Arax";

    public static final String NOC_ADMIN_EMAIL = "nocadmin@connectto.com";
    public static final String TRANSPORTATION_TEST_CONNECTION = "https://mobile.vshoo.com/transportation_online/GetTestConnection";
    public static final String TRANSPORTATION_LOGOUT = "https://mobile.vshoo.com/transportation_online/Logout";



    private SetupInfo setupInfo;
    private static boolean isUnderConstruction;

    private static ImageSizeLoader imageSizeLoader;
    private static String browserDatabaseName;
    private static String browserDatabaseVersion;

    public Initializer() {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        try {

            logger.info("-- start application -- ");
            System.setProperty("file.encoding", "UTF-8");

            context = event.getServletContext();
            //context.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

            applicationContext = (ApplicationContext) context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

            imageSizeLoader = (ImageSizeLoader) applicationContext.getBean("imageSizeLoader");
            setupInfo = (SetupInfo) applicationContext.getBean("setupInfo");
            paginationCount = setupInfo.getPaginationCount();
            bannerItemCount = setupInfo.getBannerItemCount();

            corporateVshoo = setupInfo.corporateVshoo;
            //context real path
            contextPath = context.getRealPath("/");
            if (setupInfo.getSetup().equals(DEV)) {

                String[] contextPathDirs = contextPath.split("\\\\");

                /*StringBuffer contextPathBuffer = new StringBuffer();
                for (int i = 0; i < contextPathDirs.length - 1; i++) {
                    String dir = contextPathDirs[i];
                    contextPathBuffer.append(dir).append(File.separator);
                }*/

                //contextPath = contextPathBuffer.toString();
                logger.info("**************************************************************************************************");
                logger.info("contextPath=>" + contextPath);
                logger.info("**************************************************************************************************");

                isUnderConstruction = true;
                //images real path
                if (!Utils.isEmpty(setupInfo.getStaticDir())) {
                    dataPath = contextPath + File.separator + WALLET_DATA_FOLDER;
                } else {
                    dataPath = setupInfo.getStaticDir();
                }

                //images context path
                imageContextPath = context.getContextPath() + File.separator + WALLET_DATA_FOLDER;
            } else if (setupInfo.getSetup().equals(PROD)) {

                isUnderConstruction = false;


                //images real path
                dataPath = setupInfo.getStaticDirProd() + File.separator + WALLET_DATA_FOLDER;

                //images context path
                //imageContextPath = context.getContextPath() + File.separator + IMAGES_FOLDER;
                imageContextPath = WALLET_DATA_FOLDER;
            } else {
                throw new RuntimeException("application init fail " + setupInfo.getSetup());
            }
            logger.info(String.format("Application %s intitilize params [imagePath:%s, imageContextPath:%s ]", setupInfo.getVersion(), dataPath, imageContextPath));
            //init data folder
            initFolders(dataPath);
            initFolders(dataPath + File.separator + WALLET_DATA_FOLDER);
            initFolders(dataPath + File.separator + CONFIG_FOLDER);

            initFolders(dataPath + File.separator + WALLET_DATA_FOLDER + File.separator + WALLET_UPLOAD_FOLDER );

            initFolders(dataPath + File.separator + WALLET_DATA_FOLDER + File.separator + WALLET_UPLOAD_FOLDER);
            initFolders(dataPath + File.separator + WALLET_DATA_FOLDER + File.separator + WALLET_UPLOAD_FOLDER + File.separator + WALLET_TRANSACTION_FOLDER);
            initFolders(dataPath + File.separator + WALLET_DATA_FOLDER + File.separator + WALLET_UPLOAD_FOLDER + File.separator + WALLET_DISPUTE_FOLDER);

            //set tmp dir
            File tempDir = (File) context.getAttribute("javax.servlet.context.tempdir");
            ImageIO.setCacheDirectory(tempDir);

            initVelocity();
            logger.info("-- application started -- ");
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("unable intitilize application");
        }
    }

    private void initFolders(String contextRealPath) {
        File imageFolder = new File(contextRealPath);
        if (!imageFolder.exists()) {
            boolean created = imageFolder.mkdirs();
            if (created)
                logger.info(String.format("%s folder created successfully", contextRealPath));
            else
                throw new RuntimeException(String.format("Unable to create posts folder[%s]", contextRealPath));
        } else
            logger.info(String.format("%s folder already exist", contextRealPath));
    }

    public static boolean initFolders(String contextRealPath, String... folders) {

        try {
            for (String folder : folders) {
                String path = contextRealPath + File.separator + folder;
                File imageFolder = new File(path);
                if (!imageFolder.exists()) {
                    boolean created = imageFolder.mkdirs();
                    if (created)
                        logger.info(String.format("%s folder created successfully", path));
                    else {
                        logger.info(String.format("Unable to create folder[%s]", path));
                        return false;
                        //throw new InternalErrorException(String.format("Unable to create folder[%s]", path));
                    }
                } else
                    logger.info(String.format("%s folder already exist", path));
            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }

        return true;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void initVelocity() {
        try {
            //classpath config
            //Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            //Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            //file config
            /*String vmPath = context.getRealPath("template");
            Velocity.setProperty("resource.loader","file");
            Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            Velocity.setProperty("file.resource.loader.path", vmPath);
            Velocity.setProperty("file.resource.loader.cache", "false");
            Velocity.setProperty("file.resource.loader.modificationCheckInterval", "0");*/

            //webapp config
            Velocity.setProperty("resource.loader", "webapp");
            Velocity.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
            Velocity.setProperty("webapp.resource.loader.path", "/WEB-INF/webapptemplate/");
            Velocity.setApplicationAttribute("javax.servlet.ServletContext", context);

            //disable template file cache
            //Velocity.setProperty(RuntimeConstants.VM_LIBRARY_AUTORELOAD, true);
            //Velocity.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
            //Velocity.setProperty(RuntimeConstants.VM_PERM_ALLOW_INLINE_REPLACE_GLOBAL, true);

            Velocity.setProperty(RuntimeConstants.INPUT_ENCODING, VELOCITY_INPUT_ENCODING);
            Velocity.setProperty(RuntimeConstants.OUTPUT_ENCODING, VELOCITY_OUTPUT_ENCODING);

            //VelocityLogger vLogger = new VelocityLogger(this.getClass());
            //Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, vLogger);
            Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");

            Velocity.setProperty("runtime.log.logsystem.log4j.logger", "VLOG");

            Velocity.init();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static String getContextPath() {
        return contextPath;
    }

    public static boolean isUnderConstruction() {
        return isUnderConstruction;
    }


    public static int getPaginationCount() {
        return paginationCount;
    }
    public static int getBannerItemCount() {
        return bannerItemCount;
    }

    public static String getCorporateVshoo() {
        return corporateVshoo;
    }

    public static ImageSizeLoader getImageSizeLoader() {
        return imageSizeLoader;
    }

    public static boolean isFileExist(String path) {
        try {
            String pPath = path;
            pPath = pPath.replaceAll("//", "/");
            pPath = pPath.replaceAll("\\\\", "/");
            File file = new File(pPath);
            if (file.exists() && file.isFile()) return true;
        } catch (Exception e) {
            logger.warn(String.format("file not exist[%s]", path));
        }
        return false;
    }

    public static boolean isFileExist(String context, String path) {

        try {

            String pPath = context + File.separator + path;
            pPath = pPath.replaceAll("\\\\", "/");

            File file = new File(pPath);
            if (file.exists() && file.isFile()) return true;
        } catch (Exception e) {
            logger.warn(String.format("file not exist[%s]", path));
        }
        return false;
    }

    public static String getWalletTransactionUploadDir() {
        return dataPath + File.separator + WALLET_UPLOAD_FOLDER + File.separator + WALLET_TRANSACTION_FOLDER;
    }

    public static String getDisputeUploadDir() {
        return dataPath + File.separator + WALLET_UPLOAD_FOLDER + File.separator + WALLET_DISPUTE_FOLDER;
    }


}