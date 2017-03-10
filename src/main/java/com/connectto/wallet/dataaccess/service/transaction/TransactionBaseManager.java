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
public class TransactionBaseManager {

    private static final Logger logger = Logger.getLogger(TransactionBaseManager.class.getSimpleName());

    protected synchronized boolean sendMail(Partition partition, MailContent mailContent) {
        String fromEmail = partition.getPartitionEmail();
        String fromEmailPassword = partition.getPartitionEmailPassword();
        MailSender mailNotification = new MailSender();
        try {
            mailNotification.sendEmailFromConnectTo(mailContent, fromEmail, fromEmailPassword);
        } catch (MailException e) {
            return false;
        }

        return true;

    }

}
