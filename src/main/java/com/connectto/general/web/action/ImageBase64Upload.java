package com.connectto.general.web.action;

import com.connectto.general.util.ShellAction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by htdev001 on 3/5/14.
 */
public class ImageBase64Upload extends ShellAction {

    private InputStream result = new ByteArrayInputStream(INPUT.getBytes());
    private String base64Data;

    public String execute() {

        /*if (Utils.isEmpty(base64Data)) {
            result = new ByteArrayInputStream(NONE.getBytes());
            return ERROR;
        }

        try {
            byte[] fileData = ImageUtils.decodeToImageByte(base64Data);
            if (fileData == null) {
                result = new ByteArrayInputStream(ERROR.getBytes());
                return ERROR;
            }

            String dataFileName = System.currentTimeMillis() + ".png";
            String dataContentType = "image/png";

            UserData data = new UserData();
            data.setData(fileData);
            data.setFileName(dataFileName);
            data.setContentType(dataContentType);
            data.setSize(fileData.length);
            data.setStatus(Status.ACTIVE);

            session.put(ConstantGeneral.USER_FILE_DATA, data);
        } catch (Exception e) {
            result = new ByteArrayInputStream(ERROR.getBytes());
            return ERROR;
        }
        result = new ByteArrayInputStream(SUCCESS.getBytes());*/
        return SUCCESS;
    }


    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public InputStream getResult() {
        return result;
    }
}
