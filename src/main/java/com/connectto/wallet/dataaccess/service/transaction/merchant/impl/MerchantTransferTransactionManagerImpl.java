package com.connectto.wallet.dataaccess.service.transaction.merchant.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.TsmCompany;
import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.transfer.IMerchantTransferTicketDao;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.transfer.IMerchantTransferTransactionDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.transaction.merchant.IMerchantTransferTransactionManager;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTicket;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTransaction;
import com.connectto.wallet.model.wallet.Wallet;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Serozh on 2/14/2017.
 */
@Transactional(readOnly = true)
public class MerchantTransferTransactionManagerImpl implements IMerchantTransferTransactionManager {

    private IWalletDao  walletDao;
    private IWalletSetupDao walletSetupDao;

    private IMerchantTransferTransactionDao dao;
    private IMerchantTransferTicketDao ticketDao;

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setDao(IMerchantTransferTransactionDao dao) {
        this.dao = dao;
    }

    public void setTicketDao(IMerchantTransferTicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(MerchantTransferTransaction data) throws InternalErrorException, EntityNotFoundException, PermissionDeniedException {

        MerchantTransferTicket ticket = data.getTransferTicket();
        TsmCompany tsmCompany = data.getTsmCompany();
        Long walletId = ticket.getWalletId();
        Integer partitionId = tsmCompany.getPartitionId();

        try {

            Wallet wallet = walletDao.getById(walletId);
            WalletSetup walletSetup = walletSetupDao.getByPartitionId(partitionId);

            if(wallet.getCurrencyType().getId() != walletSetup.getCurrencyType().getId()){
                throw new PermissionDeniedException("Inappropriate currency types, Partition and User used currency types not matched");
            }

            Double dataTransferAmount = data.getTransferAmount();

            Double walletMoney = wallet.getMoney();

            walletMoney += dataTransferAmount;

            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setMoney(walletMoney);

            walletDao.update(updateWallet);
            data.setWalletId(walletId);

            ticketDao.add(ticket);
            data.setTransferTicketId(ticket.getId());

            dao.add(data);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
