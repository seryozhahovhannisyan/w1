package com.connectto.wallet.dataaccess.service.transaction;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.Partition;
import com.connectto.general.util.Utils;
import com.connectto.notification.MailContent;
import com.connectto.notification.MailException;
import com.connectto.notification.MailSender;
import com.connectto.wallet.creditCard.common.TransferSetting;
import com.connectto.wallet.creditCard.common.soap.ServiceLocator;
import com.connectto.wallet.creditCard.common.soap.ServiceSoap;
import com.connectto.wallet.creditCard.common.soap.encodedTypes.Transaction;
import com.connectto.wallet.creditCard.common.soap.encodedTypes.TransactionResult;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardDao;
import com.connectto.wallet.dataaccess.dao.wallet.ICreditCardTransferDao;
import com.connectto.wallet.dataaccess.dao.wallet.ITransferSettingDao;
import com.connectto.wallet.model.wallet.CreditCard;
import com.connectto.wallet.model.wallet.CreditCardTransactionResult;
import com.connectto.wallet.model.wallet.CreditCardTransfer;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.util.TransactionUtil;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 11/28/2016.
 */
public class TransactionCreditCardManager extends TransactionBaseManager{

    private static final Logger logger = Logger.getLogger(TransactionCreditCardManager.class.getSimpleName());

    private ICreditCardDao creditCardDao;
    private ITransferSettingDao transferSettingDao;
    private ICreditCardTransferDao creditCardTransferDao;

    protected synchronized boolean chargeFromWalletCreditCard(int partitionId, Long walletId,
                                          Long transactionPurchaseId,
                                          Double transferAmount, CurrencyType transferCurrencyType) throws CreditCardException, PermissionDeniedException, InternalErrorException {

        TransferSetting transferSetting = null;
        List<CreditCard> creditCards = null;

        Map<String, Object> creditCardParams = new HashMap<String, Object>();
        creditCardParams.put("walletId", walletId);

        try {
            transferSetting = transferSettingDao.getByPartitionId(partitionId);
            creditCards = creditCardDao.getByParams(creditCardParams);
            if (Utils.isEmpty(creditCards)) {
                throw new PermissionDeniedException("The wallet has not creditCards walletId=" + walletId);
            }
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }

        for (CreditCard creditCard : creditCards) {

            if(creditCard.getIsEnabled() && !creditCard.getIsDeleted() && !creditCard.getIsBlocked()  ){

                CreditCardTransfer creditCardTransfer = new CreditCardTransfer();
                creditCardTransfer.setTransactionPurchaseId(transactionPurchaseId);
                creditCardTransfer.setCreditCardId(creditCard.getId());
                creditCardTransfer.setTransferAmount(transferAmount);
                creditCardTransfer.setTransferDt(new java.sql.Date(System.currentTimeMillis()));

                try {
                    creditCardTransferDao.add(creditCardTransfer);
                } catch (DatabaseException e) {
                    throw new CreditCardException(e);
                }

                TransactionResult res = null;
                Transaction src = new Transaction();

                src.setExactID(transferSetting.getExactId());
                src.setPassword(transferSetting.getTransferPwd());
                src.setTransaction_Type("00");

                // test params
                src.setCurrency(transferCurrencyType.getCode());
                src.setCard_Number(creditCard.getNumber()); //5178058808358801 card type determined automatically from card number
                src.setCardHoldersName(creditCard.getHolderName());///
                src.setDollarAmount(transferAmount.toString());
                src.setExpiry_Date(Utils.toCreditCardDate(creditCard.getExpiryDate())); // mmyy

                ServiceLocator locator = new ServiceLocator();
                ServiceSoap sc = null;
                try {
                    sc = locator.getServiceSoap(new URL("https://api.globalgatewaye4.firstdata.com/transaction/v11"));
                    res = sc.sendAndCommit(src);
                    writeCreditCardLog(creditCardTransfer, res);
                    return true;
                } catch (ServiceException e) {
                    logger.error(e);
                    writeCreditCardLog(creditCardTransfer, res);
                    blockCreditCard(creditCard);
                } catch (MalformedURLException e) {
                    logger.error(e);
                    writeCreditCardLog(creditCardTransfer, res);
                    blockCreditCard(creditCard);
                } catch (RemoteException e) {
                    logger.error(e);
                    writeCreditCardLog(creditCardTransfer, res);
                    blockCreditCard(creditCard);
                }
            }
        }

        return false;
    }

    private synchronized void writeCreditCardLog(CreditCardTransfer creditCardTransfer, TransactionResult res) {

        creditCardTransfer.setTransferResponseCode(res.getEXact_Resp_Code());
        creditCardTransfer.setTransferResponseMsg(res.getEXact_Message());

        try {

            creditCardTransferDao.edit(creditCardTransfer);

            CreditCardTransactionResult result = TransactionUtil.convertToCreditCardTransactionResult(res);
            result.setTransferId(creditCardTransfer.getId());
            creditCardTransferDao.add(result);

        } catch (DatabaseException e) {
            logger.error(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
        }


    }

    private synchronized void blockCreditCard(CreditCard creditCard) {
        creditCard.setIsBlocked(true);
        try {
            creditCardDao.blockCreditCard(creditCard);
        } catch (DatabaseException e) {
            logger.error(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
        }
    }

    public void setCreditCardDao(ICreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

    public void setTransferSettingDao(ITransferSettingDao transferSettingDao) {
        this.transferSettingDao = transferSettingDao;
    }

    public void setCreditCardTransferDao(ICreditCardTransferDao creditCardTransferDao) {
        this.creditCardTransferDao = creditCardTransferDao;
    }
}
