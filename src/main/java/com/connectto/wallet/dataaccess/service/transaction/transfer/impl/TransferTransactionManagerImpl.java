package com.connectto.wallet.dataaccess.service.transaction.transfer.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.dataaccess.dao.transaction.transfer.ITransferTicketDao;
import com.connectto.wallet.dataaccess.dao.transaction.transfer.ITransferTransactionDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.transaction.transfer.ITransferTransactionManager;
import com.connectto.wallet.model.transaction.transfer.TransferTicket;
import com.connectto.wallet.model.transaction.transfer.TransferTransaction;
import com.connectto.wallet.model.wallet.Wallet;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Serozh on 2/14/2017.
 */
@Transactional(readOnly = true)
public class TransferTransactionManagerImpl implements ITransferTransactionManager {

    private IWalletSetupDao walletSetupDao;
    private IWalletDao  walletDao;

    private ITransferTransactionDao dao;
    private ITransferTicketDao ticketDao;

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setDao(ITransferTransactionDao dao) {
        this.dao = dao;
    }

    public void setTicketDao(ITransferTicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(TransferTransaction data) throws InternalErrorException, EntityNotFoundException, PermissionDeniedException {

        TransferTicket ticket = data.getTransferTicket();
        Long walletId = ticket.getWalletId();
        Integer partitionId = ticket.getSystemAdminPartitionId();

        try {
            Wallet wallet = walletDao.getById(walletId);
            WalletSetup walletSetup = walletSetupDao.getByPartitionId(partitionId);
            Long walletSetupId = walletSetup.getId();

            if(wallet.getCurrencyType().getId() != walletSetup.getCurrencyType().getId()){
                throw new PermissionDeniedException("Inappropriate currency types, Partition and User used currency types not matched");
            }

            Double dataTransferAmount = data.getTransferAmount();

            Double walletMoney = wallet.getMoney();
            Double walletSetupBalance = walletSetup.getBalance();

            walletMoney += dataTransferAmount;
            walletSetupBalance -= dataTransferAmount;

            if(walletSetupBalance < 0){
                throw new PermissionDeniedException("The partition cannot transfer so many money");
            }

            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setMoney(walletMoney);

            walletDao.update(updateWallet);

            WalletSetup updateWalletSetup = new WalletSetup();
            updateWalletSetup.setId(walletSetupId);
            updateWalletSetup.setBalance(walletSetupBalance);

            walletSetupDao.updateNotNull(updateWalletSetup);

            data.setWalletSetupId(walletSetupId);
            data.setWalletId(walletId);

            ticketDao.add(ticket);
            data.setTransferTicketId(ticket.getId());

            dao.add(data);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }
}
