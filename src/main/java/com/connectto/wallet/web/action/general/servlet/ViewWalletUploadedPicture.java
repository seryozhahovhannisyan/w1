package com.connectto.wallet.web.action.general.servlet;

import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.wallet.model.general.FileData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewWalletUploadedPicture extends HttpServlet {

    private String IMAGE_TYPE = "image/jpeg";

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OutputStream out = null;
        try {
            List<FileData> fileDatas = (List<FileData>) request.getSession().getAttribute(ConstantGeneral.WALLET_FILE_DATAS);
            if (!Utils.isEmpty(fileDatas)) {
                byte[] srcImage = fileDatas.get(0).getData();
                if (srcImage != null && srcImage.length > 0) {
                    response.setContentType(IMAGE_TYPE);
                    out = response.getOutputStream();
                    out.write(srcImage);
                }
            }
            //UserData data = (UserData) request.getSession().getAttribute(ConstantGeneral.USER_FILE_DATA);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // todo log
                }
            }
        }

    }
}
