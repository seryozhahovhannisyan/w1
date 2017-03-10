package com.connectto.general.web.action;

import com.connectto.general.util.ShellAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by htdev001 on 3/5/14.
 */
public class FileUpload extends ShellAction {

    private InputStream result = new ByteArrayInputStream(INPUT.getBytes());
    private File data;
    private String dataFileName;
    private String dataContentType;

    public String execute() {

        /*if (data == null || dataFileName == null) {
            result = new ByteArrayInputStream(NONE.getBytes());
            return ERROR;
        }

        try {
            byte[] fileData = FileUtils.readFileToByteArray(data);
            if (fileData == null) {
                result = new ByteArrayInputStream(ERROR.getBytes());
                return ERROR;
            }

            UserData data = new UserData();
            data.setData(fileData);
            data.setFileName(dataFileName);
            data.setContentType(dataContentType);
            data.setSize(fileData.length);
            data.setStatus(Status.ACTIVE);

            session.put(ConstantGeneral.USER_FILE_DATA, data);
        } catch (IOException e) {
            result = new ByteArrayInputStream(ERROR.getBytes());
            return ERROR;
        }
        result = new ByteArrayInputStream(SUCCESS.getBytes());*/
        return SUCCESS;
    }

    public void setData(File data) {
        this.data = data;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public InputStream getResult() {
        return result;
    }
}
