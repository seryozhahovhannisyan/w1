package com.connectto.wallet.web.action.wallet;

import com.connectto.general.model.User;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by htdev001 on 3/5/14.
 */
public class TransactionDataDownloadAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(TransactionDataDownloadAction.class.getSimpleName());

    private InputStream inputStream;
    private Long transactionId;
    private String dataFileName;

    public String execute() {

        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);

        if (user == null || wallet == null) {
            logger.error("session time expired");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            return "start";
        }

        if (Utils.isEmpty(dataFileName) || transactionId == -1) {
            logger.error("TransactionDataDownloadAction, dataFileName or transactionId is null");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            return "start";
        }
        dataFileName = Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + transactionId + ConstantGeneral.FILE_SEPARATOR + dataFileName;
        dataFileName = dataFileName.replaceAll("//", "/");
        dataFileName = dataFileName.replaceAll("\\\\", "/");

        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                logger.error("The attached file not exists; transaction id=" + transactionId + "; file name = " + dataFileName);
                session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
                return "start";
            }

            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error(e);
        }

        return SUCCESS;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setTransactionId(String transactionId) {
        try {
            this.transactionId = Long.parseLong(transactionId);
        } catch (Exception e) {
            this.transactionId = -1L;
            logger.error("Incorrect transaction id ,  transactionId=" + transactionId);
        }
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

}
