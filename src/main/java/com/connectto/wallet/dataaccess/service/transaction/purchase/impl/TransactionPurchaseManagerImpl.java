package com.connectto.wallet.dataaccess.service.transaction.purchase.impl;

import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.dataaccess.dao.transaction.purchase.*;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.transaction.TransactionCreditCardManager;
import com.connectto.wallet.dataaccess.service.transaction.purchase.ITransactionPurchaseManager;
import com.connectto.wallet.model.transaction.purchase.*;
import com.connectto.wallet.model.wallet.Wallet;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by htdev001 on 8/26/14.
 */

@Transactional(readOnly = true)
public class TransactionPurchaseManagerImpl extends TransactionCreditCardManager implements ITransactionPurchaseManager {

    private IPurchaseTicketDao purchaseTicketDao;
    private ITransactionPurchaseDao transactionPurchaseDao;
    private ITransactionPurchaseExchangeDao exchangeDao;
    private ITransactionPurchaseExchangeTaxDao exchangeTaxDao;
    private ITransactionPurchaseProcessDao processDao;
    private ITransactionPurchaseProcessTaxDao transactionPurchaseProcessTaxDao;
    private ITransactionPurchaseTaxDao taxDao;

    private IWalletDao walletDao;
    private IWalletSetupDao walletSetupDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void freezeDriverBalance(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException {

        TransactionPurchaseProcess process = null;
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
        TransactionPurchaseTax tax = data.getTax();

        TransactionPurchaseProcessTax processTax;
        TransactionPurchaseExchange exchange;
        TransactionPurchaseExchange processTaxExchange;

        if (data.getProcessStart() != null) {
            process = data.getProcessStart();
        } else {
            throw new PermissionDeniedException("Transaction was already frozen");
        }

        try {

            validOrderCode = getValidOrderCode(validOrderCode);
            data.setOrderCode(validOrderCode);
            exchange = process.getExchange();
            if (exchange != null) {

                TransactionPurchaseExchangeTax exchangeTax = exchange.getExchangeTax();
                exchangeTaxDao.add(exchangeTax);
                Long exchangeTaxId = exchangeTax.getId();

                exchange.setExchangeTaxId(exchangeTaxId);
                exchangeDao.add(exchange);
                Long exchangeId = exchange.getId();
                process.setExchangeId(exchangeId);
                tax.setExchangeTaxId(exchangeTaxId);
            }

            processTax = process.getProcessTax();
            processTaxExchange = processTax.getExchange();
            if (processTaxExchange != null) {

                TransactionPurchaseExchangeTax processTaxExchangeTax = processTaxExchange.getExchangeTax();
                exchangeTaxDao.add(processTaxExchangeTax);
                Long processTaxExchangeTaxId = processTaxExchangeTax.getId();

                processTaxExchange.setExchangeTaxId(processTaxExchangeTaxId);
                exchangeDao.add(processTaxExchange);

                Long processTaxExchangeId = processTaxExchange.getId();
                processTax.setExchangeId(processTaxExchangeId);

                tax.setProcessTaxExchangeTaxId(processTaxExchangeTaxId);

            }
            transactionPurchaseProcessTaxDao.add(processTax);
            Long processTaxId = processTax.getId();
            process.setProcessTaxId(processTaxId);
            tax.setProcessTaxId(processTaxId);

            processDao.add(process);
            long processId = process.getId();

            taxDao.add(tax);
            long taxId = tax.getId();

            data.setTaxId(taxId);
            data.setProcessStartId(processId);

            transactionPurchaseDao.add(data);
            List<PurchaseTicket> tickets = data.getTickets();
            for (PurchaseTicket ticket : tickets) {
                ticket.setTransactionId(data.getId());
                purchaseTicketDao.add(ticket);
            }
            //
            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            wfa += wtp;
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wfa);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();
            Double stt = tax.getTotalTax();
            Double stp = sta - stt;

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sra = walletSetupOriginal.getReceivingAmount();
            Double stta = walletSetupOriginal.getTransferTaxAmount();
            sra += stp;
            stta += stt;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setReceivingAmount(sra);
            walletSetup.setTransferTaxAmount(stta);
            walletSetupDao.updateNotNull(walletSetup);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void freeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException {

        TransactionPurchaseProcess process = null;
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
        TransactionPurchaseTax tax = data.getTax();

        TransactionPurchaseProcessTax processTax;
        TransactionPurchaseExchange exchange;
        TransactionPurchaseExchange processTaxExchange;

        if (data.getProcessStart() != null) {
            process = data.getProcessStart();
        } else {
            throw new PermissionDeniedException("Transaction was already frozen");
        }

        try {

            validOrderCode = getValidOrderCode(validOrderCode);
            data.setOrderCode(validOrderCode);
            exchange = process.getExchange();
            if (exchange != null) {

                TransactionPurchaseExchangeTax exchangeTax = exchange.getExchangeTax();
                exchangeTaxDao.add(exchangeTax);
                Long exchangeTaxId = exchangeTax.getId();

                exchange.setExchangeTaxId(exchangeTaxId);
                exchangeDao.add(exchange);
                Long exchangeId = exchange.getId();
                process.setExchangeId(exchangeId);
                tax.setExchangeTaxId(exchangeTaxId);
            }

            processTax = process.getProcessTax();
            processTaxExchange = processTax.getExchange();
            if (processTaxExchange != null) {

                TransactionPurchaseExchangeTax processTaxExchangeTax = processTaxExchange.getExchangeTax();
                exchangeTaxDao.add(processTaxExchangeTax);
                Long processTaxExchangeTaxId = processTaxExchangeTax.getId();

                processTaxExchange.setExchangeTaxId(processTaxExchangeTaxId);
                exchangeDao.add(processTaxExchange);

                Long processTaxExchangeId = processTaxExchange.getId();
                processTax.setExchangeId(processTaxExchangeId);

                tax.setProcessTaxExchangeTaxId(processTaxExchangeTaxId);

            }
            transactionPurchaseProcessTaxDao.add(processTax);
            Long processTaxId = processTax.getId();
            process.setProcessTaxId(processTaxId);
            tax.setProcessTaxId(processTaxId);

            processDao.add(process);
            long processId = process.getId();

            taxDao.add(tax);
            long taxId = tax.getId();

            data.setTaxId(taxId);
            data.setProcessStartId(processId);

            transactionPurchaseDao.add(data);
            List<PurchaseTicket> tickets = data.getTickets();
            for (PurchaseTicket ticket : tickets) {
                ticket.setTransactionId(data.getId());
                purchaseTicketDao.add(ticket);
            }
            //
            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            wfa += wtp;
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wfa);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();
            Double stt = tax.getTotalTax();
            Double stp = sta - stt;

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sra = walletSetupOriginal.getReceivingAmount();
            Double stta = walletSetupOriginal.getTransferTaxAmount();
            sra += stp;
            stta += stt;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setReceivingAmount(sra);
            walletSetup.setTransferTaxAmount(stta);
            walletSetupDao.updateNotNull(walletSetup);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void cancelFreeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException {

        TransactionPurchaseProcess process = null;

        //
        Long walletId = data.getWalletId();
        Long setupId = data.getSetupId();

        if (data.getProcessEnd() != null) {
            process = data.getProcessEnd();
        } else {
            throw new PermissionDeniedException("Unsupported process ");
        }

        try {

            TransactionPurchase startPurchase = transactionPurchaseDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startPurchase.getId());
            TransactionPurchaseTax tax = startPurchase.getTax();

            process = startPurchase.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionPurchaseDao.update(data);

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            wfa -= wtp;
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wfa);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();
            Double stt = tax.getTotalTax();
            Double stp = sta - stt;

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sra = walletSetupOriginal.getReceivingAmount();
            Double stta = walletSetupOriginal.getTransferTaxAmount();
            sra -= stp;
            stta -= stt;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setReceivingAmount(sra);
            walletSetup.setTransferTaxAmount(stta);
            walletSetupDao.updateNotNull(walletSetup);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void approveFreeze(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException, CreditCardException {
        TransactionPurchaseProcess process = null;
        Long walletId = data.getWalletId();
        Long setupId = data.getSetupId();

        if (data.getProcessEnd() != null) {
            process = data.getProcessEnd();
        } else {
            throw new PermissionDeniedException("Unsupported process ");
        }

        try {

            TransactionPurchase startPurchase = transactionPurchaseDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startPurchase.getId());
            TransactionPurchaseTax tax = startPurchase.getTax();
            tax.setIsPaid(true);
            taxDao.pay(tax);

            process = startPurchase.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionPurchaseDao.update(data);
            //
            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            Double wm = wallet.getMoney();
            Double chargeFromCreditCard = 0d;

            wfa -= wtp;
            wm -= wtp;

            if (wm < 0) {
                chargeFromCreditCard = -1 * wm;
                wm = 0d;
            }

            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setMoney(wm);
            updateWallet.setFrozenAmount(wfa);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();
            Double stt = tax.getTotalTax();
            Double stp = sta - stt;

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sra = walletSetupOriginal.getReceivingAmount();
            Double stta = walletSetupOriginal.getTransferTaxAmount();
            Double sb = walletSetupOriginal.getBalance();
            sra -= stp;
            stta -= stt;
            sb += sta;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setBalance(sb);
            walletSetup.setReceivingAmount(sra);
            walletSetup.setTransferTaxAmount(stta);
            walletSetupDao.updateNotNull(walletSetup);

            // TODO PDF Implementation
            // TODO Email Implementation
            // TODO: 11/25/2016 Credit Card implementation
            boolean charged = chargeFromWalletCreditCard(walletSetupOriginal.getPartitionId(), walletId, data.getId(), chargeFromCreditCard, walletSetupOriginal.getCurrencyType());
            if (!charged) {
                throw new InternalErrorException("Could not charge from credit card ");
            }

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void directChargeFromWallet(TransactionPurchase data) throws InternalErrorException, PermissionDeniedException, CreditCardException {

        TransactionPurchaseProcess process = null;
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
        TransactionPurchaseTax tax = data.getTax();

        TransactionPurchaseProcessTax processTax;
        TransactionPurchaseExchange exchange;
        TransactionPurchaseExchange processTaxExchange;

        if (data.getProcessStart() != null) {
            process = data.getProcessStart();
        } else {
            throw new PermissionDeniedException("Transaction was already frozen");
        }

        try {

            validOrderCode = getValidOrderCode(validOrderCode);
            data.setOrderCode(validOrderCode);
            exchange = process.getExchange();
            if (exchange != null) {

                TransactionPurchaseExchangeTax exchangeTax = exchange.getExchangeTax();
                exchangeTaxDao.add(exchangeTax);
                Long exchangeTaxId = exchangeTax.getId();

                exchange.setExchangeTaxId(exchangeTaxId);
                exchangeDao.add(exchange);
                Long exchangeId = exchange.getId();
                process.setExchangeId(exchangeId);
                tax.setExchangeTaxId(exchangeTaxId);
            }

            processTax = process.getProcessTax();
            processTaxExchange = processTax.getExchange();
            if (processTaxExchange != null) {

                TransactionPurchaseExchangeTax processTaxExchangeTax = processTaxExchange.getExchangeTax();
                exchangeTaxDao.add(processTaxExchangeTax);
                Long processTaxExchangeTaxId = processTaxExchangeTax.getId();

                processTaxExchange.setExchangeTaxId(processTaxExchangeTaxId);
                exchangeDao.add(processTaxExchange);

                Long processTaxExchangeId = processTaxExchange.getId();
                processTax.setExchangeId(processTaxExchangeId);

                tax.setProcessTaxExchangeTaxId(processTaxExchangeTaxId);

            }
            transactionPurchaseProcessTaxDao.add(processTax);
            Long processTaxId = processTax.getId();
            process.setProcessTaxId(processTaxId);
            tax.setProcessTaxId(processTaxId);

            processDao.add(process);
            long processId = process.getId();

            taxDao.add(tax);
            long taxId = tax.getId();

            data.setTaxId(taxId);
            data.setProcessStartId(processId);
            data.setProcessEndId(processId);

            transactionPurchaseDao.add(data);
            List<PurchaseTicket> tickets = data.getTickets();
            for (PurchaseTicket ticket : tickets) {
                ticket.setTransactionId(data.getId());
                purchaseTicketDao.add(ticket);
            }
            //
            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wm = wallet.getMoney();
            Double chargeFromCreditCard = 0d;
            wm -= wtp;

            if (wm < 0) {
                chargeFromCreditCard = -1 * wm;
                wm = 0d;
            }

            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setMoney(wm);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sb = walletSetupOriginal.getBalance();
            sb += sta;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setBalance(sb);
            walletSetupDao.updateNotNull(walletSetup);


            //// TODO: 12/5/2016
            boolean charged = chargeFromWalletCreditCard(walletSetupOriginal.getPartitionId(), walletId, data.getId(), chargeFromCreditCard, walletSetupOriginal.getCurrencyType());
            if (!charged) {
                throw new InternalErrorException("Could not charge from credit card ");
            }

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Override
    public TransactionPurchase getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionPurchaseDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    private synchronized String getValidOrderCode(String orderCode) throws DatabaseException {
        try {
            if (!transactionPurchaseDao.isOrderCode(orderCode)) {
                return orderCode;
            } else {
                orderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
                return getValidOrderCode(orderCode);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void setPurchaseTicketDao(IPurchaseTicketDao purchaseTicketDao) {
        this.purchaseTicketDao = purchaseTicketDao;
    }

    public void setTransactionPurchaseDao(ITransactionPurchaseDao transactionPurchaseDao) {
        this.transactionPurchaseDao = transactionPurchaseDao;
    }

    public void setExchangeDao(ITransactionPurchaseExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    public void setExchangeTaxDao(ITransactionPurchaseExchangeTaxDao exchangeTaxDao) {
        this.exchangeTaxDao = exchangeTaxDao;
    }

    public void setProcessDao(ITransactionPurchaseProcessDao processDao) {
        this.processDao = processDao;
    }

    public void setTransactionPurchaseProcessTaxDao(ITransactionPurchaseProcessTaxDao transactionPurchaseProcessTaxDao) {
        this.transactionPurchaseProcessTaxDao = transactionPurchaseProcessTaxDao;
    }

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setTaxDao(ITransactionPurchaseTaxDao taxDao) {
        this.taxDao = taxDao;
    }
}
