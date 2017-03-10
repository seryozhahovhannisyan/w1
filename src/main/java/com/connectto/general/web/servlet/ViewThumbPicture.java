package com.connectto.general.web.servlet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ViewThumbPicture extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ViewThumbPicture.class.getSimpleName());
    private String IMAGE_TYPE = "image/jpeg";

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OutputStream out = null;
        try {

            String id = request.getParameter("userId");
            String path = request.getParameter("name");
            String type = request.getParameter("type");

            logger.info("id=" + id + ",path=" + path + ",type=" + type);

            File data = null;//Initializer.getUserThumbImage(id, path);
            if (data != null && data.exists() && data.isFile()) {
                byte[] srcImage = FileUtils.readFileToByteArray(data);

                if (srcImage != null && srcImage.length > 0) {
                    response.setContentType(IMAGE_TYPE);
                    out = response.getOutputStream();
                    out.write(srcImage);
                }
            } else {

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

}
