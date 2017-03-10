package com.connectto.wallet.dataaccess.service.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.dao.ITransactionDataDao;
import com.connectto.wallet.dataaccess.dao.ITransactionDisputeDao;
import com.connectto.wallet.dataaccess.service.ITransactionDisputeManager;
import com.connectto.wallet.model.wallet.TransactionData;
import com.connectto.wallet.model.wallet.TransactionDispute;
import com.connectto.wallet.model.wallet.lcp.DisputeState;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */
@Transactional(readOnly = true)
public class TransactionDisputeManagerImpl implements ITransactionDisputeManager {

    private static final Logger logger = Logger.getLogger(TransactionDisputeManagerImpl.class.getSimpleName());

    private ITransactionDisputeDao transactionDisputeDao;
    private ITransactionDataDao transactionDataDao;

    public void setTransactionDisputeDao(ITransactionDisputeDao transactionDisputeDao) {
        this.transactionDisputeDao = transactionDisputeDao;
    }

    public void setTransactionDataDao(ITransactionDataDao transactionDataDao) {
        this.transactionDataDao = transactionDataDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(TransactionDispute transactionDispute) throws InternalErrorException {
        try {
            transactionDisputeDao.add(transactionDispute);
            Long disputeId = transactionDispute.getId();
            List<TransactionData> disputeDatas = transactionDispute.getTransactionDatas();
            if (!Utils.isEmpty(disputeDatas)) {

                for (TransactionData data : disputeDatas) {

                    data.setDisputeId(disputeId);

                    String fileName = data.getFileName();
                    String extension = fileName.substring(fileName.indexOf("."));
                    //
                    fileName = System.currentTimeMillis() + extension;
                    //

                    data.setFileName(fileName);
                    //data.setTransactionId(transactionDispute.getId());

                    transactionDataDao.add(data);

                    File originalFile = new File(Initializer.getDisputeUploadDir() + ConstantGeneral.FILE_SEPARATOR + disputeId + ConstantGeneral.FILE_SEPARATOR + fileName);
                    FileUtils.writeByteArrayToFile(originalFile, data.getData());
                }
            }

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionDispute getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionDisputeDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<TransactionDispute> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return transactionDisputeDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Long getCountByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return transactionDisputeDao.getCountByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
     @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
     public void update(TransactionDispute transactionDispute) throws InternalErrorException, EntityNotFoundException {
        try {
            transactionDisputeDao.update(transactionDispute);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void closeDispute(Long id)  throws InternalErrorException, EntityNotFoundException {
        TransactionDispute transactionDispute = new TransactionDispute();
        transactionDispute.setId(id);
        transactionDispute.setState(DisputeState.ClOSE);
        try {
            transactionDisputeDao.update(transactionDispute);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            transactionDisputeDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
