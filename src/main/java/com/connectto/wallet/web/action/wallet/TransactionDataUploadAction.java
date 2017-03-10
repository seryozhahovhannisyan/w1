package com.connectto.wallet.web.action.wallet;

import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.model.wallet.TransactionData;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by htdev001 on 3/5/14.
 */
public class TransactionDataUploadAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(TransactionDataUploadAction.class.getSimpleName());
    private static final String EXISTS = "exists";

    private InputStream result = new ByteArrayInputStream(INPUT.getBytes());
    private ResponseDto responseDto;

    private File datas[];
    private String datasFileName[];
    private String datasContentType[];

    private String dataFileName;


    public String upload() {

        if (datas == null || datasFileName == null) {
            result = new ByteArrayInputStream(NONE.getBytes());
            logger.error("TransactionDataUploadAction, data or dataFileName is null");
            return INPUT;
        }

        List<TransactionData> sessionDatas = (List<TransactionData>) session.get(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);
        List<TransactionData> transactionDatas = new ArrayList<TransactionData>();
        if(sessionDatas == null){
            sessionDatas = new ArrayList<TransactionData>();
        }

        try {

            for (int i = 0; i < datas.length; i++) {

                File data = datas[i];
                String dataFileName = datasFileName[i];
                String dataContentType = datasContentType[i];
                if(exists(sessionDatas, dataFileName) || exists(transactionDatas, dataFileName)  ){
                    result = new ByteArrayInputStream(EXISTS.getBytes());
                    logger.error("TransactionDataUploadAction, dataFileName is exists "+dataFileName);
                    return INPUT;
                }

                byte[] fileData = FileUtils.readFileToByteArray(data);
                if (fileData == null) {
                    result = new ByteArrayInputStream(NONE.getBytes());
                    logger.error("TransactionDataUploadAction, data or dataFileName is null");
                    return INPUT;
                }

                TransactionData td = new TransactionData();
                td.setData(fileData);
                td.setFileName(dataFileName);
                td.setContentType(dataContentType);
                td.setSize(fileData.length);
                td.setCreationDate(new Date(System.currentTimeMillis()));

                transactionDatas.add(td);

            }

            sessionDatas.addAll(transactionDatas);

            session.put(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS, sessionDatas);

        } catch (IOException e) {
            result = new ByteArrayInputStream(ERROR.getBytes());
            logger.error(e);
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            return ERROR;
        }
        result = new ByteArrayInputStream(SUCCESS.getBytes());
        return SUCCESS;
    }

    private synchronized boolean exists(List<TransactionData> transactionDatas , String dataFileName){
        for (int i = 0; i < transactionDatas.size(); i++) {
            TransactionData data = transactionDatas.get(i);
            if (dataFileName.equalsIgnoreCase(data.getFileName())) {
                return true;
            }
        }
        return false;
    }

    public String removeUploaded() {

        if (Utils.isEmpty(dataFileName)) {
            logger.error("TransactionDataUploadAction remove, dataFileName is null");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        List<TransactionData> transactionDatas = (List<TransactionData>) session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);

        if (!Utils.isEmpty(transactionDatas)) {
            for (int i = 0; i < transactionDatas.size(); i++) {
                TransactionData data = transactionDatas.get(i);
                if (dataFileName.equalsIgnoreCase(data.getFileName())) {
                    transactionDatas.remove(i);
                    responseDto.setStatus(ResponseStatus.SUCCESS);
                    session.put(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS, transactionDatas);
                    return SUCCESS;
                }
            }
        }
        session.put(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS, transactionDatas);
        responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        return SUCCESS;
    }

    public String removeAllUploaded() {
        session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);
        responseDto.setStatus(ResponseStatus.SUCCESS);
        return SUCCESS;
    }



    /*public void setData(File data) {
        this.data = data;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }*/

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public void setDatas(File[] datas) {
        this.datas = datas;
    }

    public void setDatasFileName(String[] datasFileName) {
        this.datasFileName = datasFileName;
    }

    public void setDatasContentType(String[] datasContentType) {
        this.datasContentType = datasContentType;
    }

    public InputStream getResult() {
        return result;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
