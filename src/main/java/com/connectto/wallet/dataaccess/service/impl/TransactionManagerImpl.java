package com.connectto.wallet.dataaccess.service.impl;

import com.connectto.general.dataaccess.dao.IPartitionSetupDao;
import com.connectto.general.dataaccess.dao.IUserDao;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.PartitionDto;
import com.connectto.general.model.UserDto;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.dao.*;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.ITransactionManager;
import com.connectto.wallet.model.wallet.*;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.web.action.wallet.dto.TransactionDto;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 2/12/2016.
 */
public class TransactionManagerImpl implements ITransactionManager {

    private static final Logger logger = Logger.getLogger(TransactionManagerImpl.class.getSimpleName());

    private IUserDao userDao;
    private IPartitionSetupDao partitionSetupDao;

    private IWalletDao walletDao;
    private IWalletSetupDao walletSetupDao;
    private ITransactionDao transactionDao;
    private ITransactionTaxDao transactionTaxDao;
    private ITransactionDataDao transactionDataDao;
    private ITransactionProcessDao transactionActionDao;
    private IWalletExchangeDao walletExchangeDao;

    private IExchangeDao exchangeDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void exchangeWalletBalance(WalletExchange walletExchange) throws InternalErrorException {

        Wallet wallet =  new Wallet();
        wallet.setId(walletExchange.getWalletId());
        wallet.setMoney(walletExchange.getNewMoneyPaidTaxPrice());
        wallet.setFrozenAmount(walletExchange.getFrozenAmountPrice());
        wallet.setReceivingAmount(walletExchange.getReceivingAmountPrice());
        wallet.setCurrencyType(walletExchange.getNewCurrencyType());

        Exchange newMoneyPaidTaxExchange = walletExchange.getNewMoneyPaidTaxExchange() ;
        Exchange frozenAmountExchange = walletExchange.getFrozenAmountExchange();
        Exchange receivingAmountExchange = walletExchange.getReceivingAmountExchange();
        Exchange totalExchange = walletExchange.getTotalExchange();

        try {
            if(newMoneyPaidTaxExchange != null){
                exchangeDao.add(newMoneyPaidTaxExchange);
                walletExchange.setNewMoneyPaidTaxExchangeId(newMoneyPaidTaxExchange.getId()) ;
            }
            if(frozenAmountExchange != null){
                exchangeDao.add(frozenAmountExchange);
                walletExchange.setFrozenAmountExchangeId(frozenAmountExchange.getId());
            }
            if(receivingAmountExchange != null){
                exchangeDao.add(receivingAmountExchange);
                walletExchange.setReceivingAmountExchangeId(receivingAmountExchange.getId());
            }
            if(totalExchange != null){
                exchangeDao.add(totalExchange);
                walletExchange.setTotalExchangeId(totalExchange.getId());
            }

            walletExchangeDao.add(walletExchange);
            List<WalletExchangePending> walletExchangePendings = walletExchange.getWalletExchangePendings();
            for(WalletExchangePending walletExchangePending : walletExchangePendings){

                Exchange fromTotalPriceExchange = walletExchangePending.getFromTotalPriceExchange();
                if(fromTotalPriceExchange != null){
                    exchangeDao.add(fromTotalPriceExchange);
                    walletExchangePending.setFromTotalPriceExchangeId(fromTotalPriceExchange.getId());
                }

                Exchange toTotalPriceExchange = walletExchangePending.getToTotalPriceExchange();
                if(toTotalPriceExchange != null){
                    exchangeDao.add(toTotalPriceExchange);
                    walletExchangePending.setToTotalPriceExchangeId(toTotalPriceExchange.getId());
                }

                walletExchangePending.setWalletExchangeId(walletExchange.getId());
                walletExchangeDao.add(walletExchangePending);

                Transaction transaction = new Transaction();
                transaction.setId(walletExchangePending.getTransactionId());

                Double newFromTotalPrice = walletExchangePending.getNewFromTotalPrice();
                Integer newFromTotalPriceCurrencyTypeId = walletExchangePending.getNewFromTotalPriceCurrencyType();

                if(newFromTotalPriceCurrencyTypeId != null && newFromTotalPrice != null){
                    transaction.setFromTotalPrice(newFromTotalPrice);
                    transaction.setFromTotalPriceCurrencyType(CurrencyType.valueOf(newFromTotalPriceCurrencyTypeId));
                    transactionDao.update(transaction);
                }

                Integer newToTotalPriceCurrencyTypeId = walletExchangePending.getNewToTotalPriceCurrencyType();
                Double newToTotalPrice = walletExchangePending.getNewToTotalPrice();

                if(newToTotalPriceCurrencyTypeId != null && newToTotalPrice != null){
                    transaction.setToTotalPrice(newToTotalPrice);
                    transaction.setToTotalPriceCurrencyType(CurrencyType.valueOf(newToTotalPriceCurrencyTypeId));
                }
            }

            walletDao.update(wallet);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(Transaction transaction) throws InternalErrorException {

        TransactionProcess fromTransactionAction = transaction.getFromTransactionProcess();
        TransactionProcess toTransactionAction = transaction.getToTransactionProcess();
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);

        try {

            validOrderCode = getValidOrderCode(validOrderCode);
            transaction.setOrderCode(validOrderCode);

            if(fromTransactionAction != null){
                Exchange totalAmountExchange = fromTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    fromTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }
                TransactionTax tax = fromTransactionAction.getTax();
                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }
                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }
                transactionTaxDao.add(tax);
                fromTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(fromTransactionAction);
                transaction.setFromTransactionProcessId(fromTransactionAction.getId());
                //froze amount
                Long frozenWalletId = fromTransactionAction.getWalletId();
                Double fromTotalPrice = transaction.getFromTotalPrice();
                Wallet originalWallet = walletDao.getById(frozenWalletId);
                Double originalFrozenAmount = originalWallet.getFrozenAmount();
                originalFrozenAmount += fromTotalPrice;

                Wallet updateWallet = new Wallet();
                updateWallet.setId(frozenWalletId);
                updateWallet.setFrozenAmount(originalFrozenAmount);
                //walletDao.updateIncreaseFrozenAmount(updateWallet);
                walletDao.update(updateWallet);
                //wait for tax
                Long taxWalletSetupId = fromTransactionAction.getWalletSetupId();
                Double totalAmountTax =  tax.getTotalAmountTax();
                WalletSetup originalWalletSetup = walletSetupDao.getById(taxWalletSetupId);
                Double originalTotalAmountTax = originalWalletSetup.getTransferTaxAmount();
                originalTotalAmountTax += totalAmountTax;

                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(taxWalletSetupId);
                updateWalletSetup.setTransferTaxAmount(originalTotalAmountTax);
                //walletSetupDao.updateIncreaseTransferTaxAmount(walletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {

                Long fromWalletSetupId = transaction.getFromWalletSetupId();
                Double fromAmount =  transaction.getSetupAmount();
                WalletSetup originalWalletSetup = walletSetupDao.getById(fromWalletSetupId);
                Double originalFrozenAmount = originalWalletSetup.getFrozenAmount();
                originalFrozenAmount -= fromAmount;

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(fromWalletSetupId);
                updateWalletSetup.setFrozenAmount(originalFrozenAmount);
                //walletSetupDao.updateIncreaseFrozenAmount(updateWalletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            if(toTransactionAction != null){
                Exchange totalAmountExchange = toTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    toTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }

                TransactionTax tax = toTransactionAction.getTax();
                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }
                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }
                transactionTaxDao.add(tax);
                toTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(toTransactionAction);
                transaction.setToTransactionProcessId(toTransactionAction.getId());
                //
                Long receivingWalletId = toTransactionAction.getWalletId();
                Double toTotalPrice = transaction.getToTotalPrice();
                Wallet originalWallet = walletDao.getById(receivingWalletId);
                Double originalReceivingAmount = originalWallet.getReceivingAmount();
                originalReceivingAmount += toTotalPrice;
                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(toTransactionAction.getWalletId());
                updateWallet.setReceivingAmount(originalReceivingAmount);
                //walletDao.updateIncreaseReceivingAmount(updateWallet);
                walletDao.update(updateWallet);
                //
                Long taxWalletSetupId = toTransactionAction.getWalletSetupId();
                Double totalAmountTax =  tax.getTotalAmountTax();
                WalletSetup originalWalletSetup = walletSetupDao.getById(taxWalletSetupId);
                Double originalTotalAmountTax = originalWalletSetup.getTransferTaxAmount();
                originalTotalAmountTax += totalAmountTax;

                //wait for tax
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(toTransactionAction.getWalletSetupId());
                updateWalletSetup.setTransferTaxAmount(originalTotalAmountTax);
                //walletSetupDao.updateIncreaseTransferTaxAmount(walletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {

                Long toWalletSetupId = transaction.getToWalletSetupId();
                Double toAmount =  transaction.getSetupAmount();
                WalletSetup originalWalletSetup = walletSetupDao.getById(toWalletSetupId);
                Double originalReceivingAmount = originalWalletSetup.getFrozenAmount();
                originalReceivingAmount += toAmount;

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(transaction.getToWalletSetupId());
                updateWalletSetup.setReceivingAmount(originalReceivingAmount);
                //walletSetupDao.updateIncreaseReceivingAmount(walletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            transactionDao.add(transaction);

            List<TransactionData> transactionDatas = transaction.getTransactionDatas();
            if (!Utils.isEmpty(transactionDatas)) {

                for (TransactionData data : transactionDatas) {
                    String fileName = data.getFileName();
                    String extension = fileName.substring(fileName.indexOf("."));
                    //
                    fileName = System.currentTimeMillis() + extension;
                    //
                    data.setFileName(fileName);
                    data.setTransactionId(transaction.getId());


                    transactionDataDao.add(data);

                    File originalFile = new File(Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + transaction.getId() + ConstantGeneral.FILE_SEPARATOR + fileName);
                    FileUtils.writeByteArrayToFile(originalFile, data.getData());
                }
            }

        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void chargeWalletImmediately(Transaction transaction) throws InternalErrorException {

        TransactionProcess fromTransactionAction = transaction.getFromTransactionProcess();
        TransactionProcess toTransactionAction = transaction.getToTransactionProcess();
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);

        Date currentDate = transaction.getOpenedAt();
        transaction.setState(TransactionState.APPROVED);
        transaction.setClosedAt(currentDate);

        try {

            validOrderCode = getValidOrderCode(validOrderCode);
            transaction.setOrderCode(validOrderCode);

            if(fromTransactionAction != null){
                Exchange totalAmountExchange = fromTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    fromTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }

                TransactionTax tax = fromTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);

                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }
                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }
                transactionTaxDao.add(tax);
                fromTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(fromTransactionAction);
                transaction.setFromTransactionProcessId(fromTransactionAction.getId());
                // chargeWalletImmediately
                Long frozenWalletId = fromTransactionAction.getWalletId();
                Double fromTotalPrice = transaction.getFromTotalPrice();
                Wallet originalWallet = walletDao.getById(frozenWalletId);
                Double money = originalWallet.getMoney();
                money -= fromTotalPrice;

                Wallet updateWallet = new Wallet();
                updateWallet.setId(frozenWalletId);
                updateWallet.setMoney(money);
                walletDao.update(updateWallet);
                //wait for tax
                Long taxWalletSetupId = fromTransactionAction.getWalletSetupId();
                Double totalAmountTax =  tax.getTotalAmountTax();
                WalletSetup originalWalletSetup = walletSetupDao.getById(taxWalletSetupId);
                Double balance = originalWalletSetup.getBalance();
                balance += totalAmountTax;

                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(taxWalletSetupId);
                updateWalletSetup.setBalance(balance);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {
                Long fromWalletSetupId = transaction.getFromWalletSetupId();
                Double fromAmount =  transaction.getSetupAmount();
                WalletSetup originalWalletSetup = walletSetupDao.getById(fromWalletSetupId);
                Double balance = originalWalletSetup.getBalance();
                balance -= fromAmount;

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(fromWalletSetupId);
                updateWalletSetup.setBalance(balance);
                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            if(toTransactionAction != null){
                Exchange totalAmountExchange = toTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    toTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }

                TransactionTax tax = toTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);

                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }

                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }

                transactionTaxDao.add(tax);
                toTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(toTransactionAction);
                transaction.setToTransactionProcessId(toTransactionAction.getId());
                //
                Long receivingWalletId = toTransactionAction.getWalletId();
                Double toTotalPrice = transaction.getToTotalPrice();
                Wallet originalWallet = walletDao.getById(receivingWalletId);
                Double originalMoney = originalWallet.getMoney();
                originalMoney += toTotalPrice;
                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(toTransactionAction.getWalletId());
                updateWallet.setMoney(originalMoney);
                walletDao.update(updateWallet);
                //
                Long taxWalletSetupId = toTransactionAction.getWalletSetupId();
                Double totalAmountTax =  tax.getTotalAmountTax();
                WalletSetup originalWalletSetup = walletSetupDao.getById(taxWalletSetupId);
                Double originalBalance = originalWalletSetup.getBalance();
                originalBalance += totalAmountTax;

                //wait for tax
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(toTransactionAction.getWalletSetupId());
                updateWalletSetup.setBalance(originalBalance);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {

                Long toWalletSetupId = transaction.getToWalletSetupId();
                Double toAmount =  transaction.getSetupAmount();
                WalletSetup originalWalletSetup = walletSetupDao.getById(toWalletSetupId);
                Double originalBalance = originalWalletSetup.getBalance();
                originalBalance += toAmount;

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(transaction.getToWalletSetupId());
                updateWalletSetup.setBalance(originalBalance);
                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            transactionDao.add(transaction);

            List<TransactionData> transactionDatas = transaction.getTransactionDatas();
            if (!Utils.isEmpty(transactionDatas)) {

                for (TransactionData data : transactionDatas) {
                    String fileName = data.getFileName();
                    String extension = fileName.substring(fileName.indexOf("."));
                    //
                    fileName = System.currentTimeMillis() + extension;
                    //
                    data.setFileName(fileName);
                    data.setTransactionId(transaction.getId());


                    transactionDataDao.add(data);

                    File originalFile =null;// new File(Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + transaction.getId() + ConstantGeneral.FILE_SEPARATOR + fileName);
                    FileUtils.writeByteArrayToFile(originalFile, data.getData());
                }
            }
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void chargeFrozenTransaction(Transaction chargeTransaction) throws InternalErrorException, PermissionDeniedException {

        Date currentDate = chargeTransaction.getOpenedAt();
        //prev transaction
        Transaction inspectionTransaction  = chargeTransaction.getFrozenTransaction();
        inspectionTransaction.setState(TransactionState.PASSED_TO_CHARGE);
        inspectionTransaction.setClosedAt(currentDate);

        TransactionProcess fromTransactionAction = chargeTransaction.getFromTransactionProcess();
        TransactionProcess toTransactionAction = chargeTransaction.getToTransactionProcess();
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);

        chargeTransaction.setState(TransactionState.APPROVED);
        chargeTransaction.setClosedAt(currentDate);

        boolean isEmptyWallet = false;

        try {

            transactionDao.updateState(inspectionTransaction);

            validOrderCode = getValidOrderCode(validOrderCode);
            chargeTransaction.setOrderCode(validOrderCode);

            if(fromTransactionAction != null){
                Exchange totalAmountExchange = fromTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    fromTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }

                TransactionTax tax = fromTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);

                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }

                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }
                transactionTaxDao.add(tax);
                fromTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(fromTransactionAction);
                chargeTransaction.setFromTransactionProcessId(fromTransactionAction.getId());
                //
                Long frozenWalletId = fromTransactionAction.getWalletId();
                Long setupId = tax.getWalletSetupId();
                //
                Double fznFtp = inspectionTransaction.getFromTotalPrice();
                Double chargeFtp = chargeTransaction.getFromTotalPrice();
                //
                Double fznTaxAmount = chargeTransaction.getFromTransactionProcess().getTax().getTotalAmountTax();
                Double chargeTaxAmount = tax.getTotalAmountTax();

                //0       = 10.150 - 10.150
                //+3.000  = 10.150 - 7.150
                //-60.000 = 10.150 - 70.150
                Double difFtp = fznFtp - chargeFtp;
                //
                Wallet originalWallet = walletDao.getById(frozenWalletId);
                WalletSetup walletSetup  = walletSetupDao.getById(setupId);
                //
                Double money = originalWallet.getMoney();
                Double originalFtp = originalWallet.getFrozenAmount();
                //
                Double originalTaxAmount = walletSetup.getTransferTaxAmount();
                Double balance = walletSetup.getBalance();

                originalFtp -= fznFtp;
                if(difFtp >= 0){

                    money -= chargeFtp;

                    originalTaxAmount -= fznTaxAmount;
                    balance += chargeTaxAmount;

                } else {

                    Double difMoney = money - difFtp;
                    if(difMoney >=0){
                        money -= difFtp;
                    } else {
                        isEmptyWallet = true;
                        money = 0d;
                        originalFtp += difMoney;
                    }

                    //todo ask from Aram about less tax
                    originalTaxAmount -= fznTaxAmount;
                    balance += chargeTaxAmount;

                }

                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(frozenWalletId);

                updateWallet.setFrozenAmount(originalFtp);
                updateWallet.setMoney(money);

                walletDao.update(updateWallet);
                //
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(setupId);

                updateWalletSetup.setTransferTaxAmount(originalTaxAmount);
                updateWalletSetup.setBalance(balance);

                walletSetupDao.updateNotNull(updateWalletSetup);

            } else {

                Double fznAmount = inspectionTransaction.getSetupAmount();
                Double chargeAmount = chargeTransaction.getSetupAmount();

                Long fromWalletSetupId = chargeTransaction.getFromWalletSetupId();

                WalletSetup originalWalletSetup = walletSetupDao.getById(fromWalletSetupId);
                Double originalFrozenAmount = originalWalletSetup.getFrozenAmount();
                Double balance = originalWalletSetup.getBalance();

                //0       = 10.150 - 10.150
                //+3.000  = 10.150 - 7.150
                //-60.000 = 10.150 - 70.150
                Double difFtp = fznAmount - chargeAmount;
                if(difFtp >= 0){
                    originalFrozenAmount -= fznAmount;
                    balance -= chargeAmount;
                } else {

                    Double difBalance = balance - difFtp;
                    if(difBalance >=0 ){
                        balance -= difFtp;
                    } else {
                        //todo ask Aram about partition balance controle
                        throw new PermissionDeniedException("The administrators could not transfer money if partition have not kind money");
                    }
                }

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(fromWalletSetupId);

                updateWalletSetup.setFrozenAmount(originalFrozenAmount);
                updateWalletSetup.setBalance(balance);

                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            if(toTransactionAction != null){

                Exchange totalAmountExchange = toTransactionAction.getTotalAmountExchange();
                if(totalAmountExchange != null){
                    exchangeDao.add(totalAmountExchange);
                    toTransactionAction.setTotalAmountExchangeId(totalAmountExchange.getId());
                }

                TransactionTax tax = toTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);

                Exchange transferTaxExchange = tax.getTransferTaxExchange();
                if(transferTaxExchange != null){
                    exchangeDao.add(transferTaxExchange);
                    tax.setTransferTaxExchangeId(transferTaxExchange.getId());
                }

                Exchange transferExchangeTaxExchange = tax.getTransferExchangeTaxExchange();
                if(transferExchangeTaxExchange != null){
                    exchangeDao.add(transferExchangeTaxExchange);
                    tax.setTransferExchangeTaxExchangeId(transferExchangeTaxExchange.getId());
                }

                transactionTaxDao.add(tax);
                toTransactionAction.setTaxId(tax.getId());
                transactionActionDao.add(toTransactionAction);
                chargeTransaction.setToTransactionProcessId(toTransactionAction.getId());
                //
                Long receiverWalletId = toTransactionAction.getWalletId();
                Long setupId = tax.getWalletSetupId();
                //
                Double inspectRtp = inspectionTransaction.getToTotalPrice();
                Double chargeRtp = chargeTransaction.getToTotalPrice();
                //
                Double inspectTaxAmount = inspectionTransaction.getToTransactionProcess().getTax().getTotalAmountTax();
                Double chargeTaxAmount = tax.getTotalAmountTax();

                //0       = 9.990 - 9.990
                //+3.000  = 9.990 - 6.990
                //-60.000 = 9.990 - 69.990
                Double difRtp = inspectRtp - chargeRtp;
                //
                Wallet originalWallet = walletDao.getById(receiverWalletId);
                WalletSetup walletSetup  = walletSetupDao.getById(setupId);
                //
                Double money = originalWallet.getMoney();
                Double originalRtp = originalWallet.getReceivingAmount();
                //
                Double originalTaxAmount = walletSetup.getTransferTaxAmount();
                Double balance = walletSetup.getBalance();

                originalRtp -= inspectRtp;
                if(difRtp >= 0 && !isEmptyWallet){

                    money += chargeRtp;

                    originalTaxAmount -= inspectTaxAmount;
                    balance += chargeTaxAmount;

                } else {
                     //todo crash
                }

                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(receiverWalletId);

                updateWallet.setReceivingAmount(originalRtp);
                updateWallet.setMoney(money);

                walletDao.update(updateWallet);
                //
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(setupId);

                updateWalletSetup.setTransferTaxAmount(originalTaxAmount);
                updateWalletSetup.setBalance(balance);

                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {

                Double inspectAmount = inspectionTransaction.getSetupAmount();
                Double chargeAmount = chargeTransaction.getSetupAmount();

                Long toWalletSetupId = chargeTransaction.getToWalletSetupId();

                WalletSetup originalWalletSetup = walletSetupDao.getById(toWalletSetupId);
                Double originalReceivingAmount = originalWalletSetup.getReceivingAmount();
                Double balance = originalWalletSetup.getBalance();

                //0       = 10.150 - 10.150
                //+3.000  = 10.150 - 7.150
                //-60.000 = 10.150 - 70.150
                Double difRtp = inspectAmount - chargeAmount;
                if(difRtp >= 0 && !isEmptyWallet){
                    originalReceivingAmount -= inspectAmount;
                    balance += chargeAmount;
                } else {
                    //todo crash
                }

                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(chargeTransaction.getToWalletSetupId());

                updateWalletSetup.setReceivingAmount(originalReceivingAmount);
                updateWalletSetup.setBalance(balance);

                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            transactionDao.add(chargeTransaction);

            List<TransactionData> transactionDatas = chargeTransaction.getTransactionDatas();
            if (!Utils.isEmpty(transactionDatas)) {

                for (TransactionData data : transactionDatas) {
                    String fileName = data.getFileName();
                    String extension = fileName.substring(fileName.indexOf("."));
                    //
                    fileName = System.currentTimeMillis() + extension;
                    //
                    data.setFileName(fileName);
                    data.setTransactionId(chargeTransaction.getId());


                    transactionDataDao.add(data);

                    File originalFile = null;// new File(Initializer.getWalletTransactionUploadDir() + ConstantGeneral.FILE_SEPARATOR + chargeTransaction.getId() + ConstantGeneral.FILE_SEPARATOR + fileName);
                    FileUtils.writeByteArrayToFile(originalFile, data.getData());
                }
            }
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Transaction getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionDto getDtoById(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {
            TransactionDto dto = transactionDao.getDtoById(params);

            Long userWalletId = dto.getUserWalletId();
            Integer walletSetupId = dto.getWalletSetupId();

            if(userWalletId != null){
                UserDto userDto = userDao.getUserDtoByWalletId(userWalletId);
                dto.setUserDto(userDto);
            } else if(walletSetupId != null) {
                PartitionDto partitionDto = partitionSetupDao.getPartitionDtoByWalletSetupId(walletSetupId);
                dto.setPartitionDto(partitionDto);
            }

            return dto;
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Transaction getByOrderCode(String orderCode) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionDao.getByOrderCode(orderCode);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionNotifier getTransactionNotifier(Map<String, Object> params) throws InternalErrorException {
        try {
            return transactionDao.getTransactionNotifier(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<TransactionDto> getDtosByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {

            String searchLike = (String)params.get("searchLike");
            if(!Utils.isEmpty(searchLike)){
                Map<String,Object> p = new HashMap<String, Object>();

                String[] ns = searchLike.split(" ");
                if(ns.length == 2){
                    if(Utils.isEmpty(ns[0].trim())){
                        p.put("searchName",ns[0].trim());
                    }
                    if(Utils.isEmpty(ns[1].trim())){
                        p.put("searchSurname",ns[1].trim());
                    }
                } else {
                    p.put("searchLike",searchLike);
                }

                p.put("userId",params.get("userId"));

                List<Long> userIdes = userDao.getUserIdesByUNameOrSurname(p);
                if(Utils.isEmpty(userIdes)){
                    throw new EntityNotFoundException("Could not found data by searchLike =" +searchLike);
                }
                params.put("userIdes",userIdes);
            }

            List<TransactionDto> transactionDtos = transactionDao.getDtosByParams(params);//360.390
            for(TransactionDto dto : transactionDtos){
                Long userWalletId = dto.getUserWalletId();
                Integer walletSetupId = dto.getWalletSetupId();

                if(userWalletId != null){
                    UserDto userDto = userDao.getUserDtoByWalletId(userWalletId);
                    dto.setUserDto(userDto);
                } else if(walletSetupId != null) {
                    PartitionDto partitionDto = partitionSetupDao.getPartitionDtoByWalletSetupId(walletSetupId);
                    dto.setPartitionDto(partitionDto);
                }
            }
            return transactionDtos;
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<WalletExchange> getWalletExchangesByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return walletExchangeDao.getByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public WalletExchange getWalletExchangesById(Long walletExchangeId) throws InternalErrorException, EntityNotFoundException {
        try {
            return walletExchangeDao.getById(walletExchangeId);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }



    @Override
    public List<Transaction> getSampleTransactionsByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return transactionDao.getSampleTransactionsByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Transaction transaction) throws InternalErrorException, EntityNotFoundException {

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void approve(Transaction transaction) throws InternalErrorException, EntityNotFoundException {

        TransactionProcess fromTransactionAction = transaction.getFromTransactionProcess();
        TransactionProcess toTransactionAction = transaction.getToTransactionProcess();
        Date currentDate = new Date(System.currentTimeMillis());

        transaction.setState(TransactionState.APPROVED);
        transaction.setClosedAt(currentDate);

        try {

            if(fromTransactionAction != null){

                TransactionTax tax = fromTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);
                transactionTaxDao.updatePayment(tax);

                //unfroze amount
                Long walletId = tax.getWalletId();
                Double ftp = transaction.getFromTotalPrice();
                Wallet wallet = walletDao.getById(walletId);
                Double oftp = wallet.getFrozenAmount();
                Double money = wallet.getMoney();

                oftp -= ftp;
                money -= ftp;

                Wallet updateWallet = new Wallet();
                updateWallet.setId(walletId);
                updateWallet.setFrozenAmount(oftp);

                updateWallet.setMoney(money);
                walletDao.update(updateWallet);
                //wait for tax
                Long setupId = tax.getWalletSetupId();
                Double ta = tax.getTotalAmountTax();
                WalletSetup walletSetup  = walletSetupDao.getById(setupId);
                Double ota = walletSetup.getTransferTaxAmount();
                Double balance = walletSetup.getBalance();
                ota -= ta;
                balance += ta;

                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(setupId);
                updateWalletSetup.setTransferTaxAmount(ota);

                updateWalletSetup.setBalance(balance);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {
                Long setupId = transaction.getFromWalletSetupId();
                Double fa = transaction.getSetupAmount();
                WalletSetup walletSetup = walletSetupDao.getById(setupId);

                Double ofa = walletSetup.getFrozenAmount();
                Double balance = walletSetup.getBalance();
                ofa -= fa;
                balance -= fa;

                //prepare decrease receiving amount
                WalletSetup updateWalletSetup  = new WalletSetup();
                updateWalletSetup.setId(setupId);
                updateWalletSetup.setFrozenAmount(ofa);
                //walletSetupDao.updateDecreaseFrozenAmount(walletSetup);

                updateWalletSetup.setBalance(balance);
                //walletSetupDao.updateDecreaseBalance(walletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            }

            if(toTransactionAction != null){

                TransactionTax tax = toTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(true);
                transactionTaxDao.updatePayment(tax);
                //
                Long walletId = tax.getWalletId();
                Double tra = transaction.getToTotalPrice();
                Wallet wallet = walletDao.getById(walletId);
                Double otra = wallet.getReceivingAmount();
                Double money = wallet.getMoney();
                otra -= tra;
                money += tra;

                Wallet updateWallet = new Wallet();
                updateWallet.setId(walletId);
                updateWallet.setReceivingAmount(otra);
                //walletDao.updateDecreaseReceivingAmount(updateWallet);
                updateWallet.setMoney(money);
                //walletDao.updateIncreaseMoneyAmount(updateWallet);
                walletDao.update(updateWallet);

                //wait for tax
                //
                Long setupId = tax.getWalletSetupId();
                Double ta = tax.getTotalAmountTax();
                WalletSetup walletSetup = walletSetupDao.getById(setupId);
                Double ota = walletSetup.getTransferTaxAmount();
                Double balance = walletSetup.getBalance();
                ota -= ta;
                balance += ta;

                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(setupId);
                updateWalletSetup.setTransferTaxAmount(ota);
                //walletSetupDao.updateDecreaseTransferTaxAmount(walletSetup);

                updateWalletSetup.setBalance(balance);
                //walletSetupDao.updateIncreaseBalance(walletSetup);
                walletSetupDao.updateNotNull(updateWalletSetup);
            } else {
                Long setupId = transaction.getToWalletSetupId();
                Double ra = transaction.getSetupAmount();
                WalletSetup walletSetup = walletSetupDao.getById(setupId);
                Double ora = walletSetup.getReceivingAmount();
                Double balance = walletSetup.getBalance();
                ora -= ra;
                balance += ra;
                //prepare decrease receiving amount
                WalletSetup updateWalletSetup = new WalletSetup();
                updateWalletSetup.setId(setupId);
                updateWalletSetup.setReceivingAmount(ora);
                //walletSetupDao.updateDecreaseReceivingAmount(updateWalletSetup);
                walletSetup.setBalance(balance);
                //walletSetupDao.updateIncreaseBalance(walletSetup);
                walletSetupDao.updateNotNull(walletSetup);
            }

            transactionDao.updateState(transaction);

        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void rejectOrCancel(Transaction transaction, TransactionState transactionState) throws InternalErrorException, EntityNotFoundException {

        TransactionProcess fromTransactionAction = transaction.getFromTransactionProcess();
        TransactionProcess toTransactionAction = transaction.getToTransactionProcess();
        Date currentDate = new Date(System.currentTimeMillis());

        transaction.setState(transactionState);
        transaction.setClosedAt(currentDate);

        try {

            if(fromTransactionAction != null){

                TransactionTax tax = fromTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(false);
                transactionTaxDao.updatePayment(tax);

                //unfroze amount
                Long walletId = tax.getWalletId();
                Double ftp = transaction.getFromTotalPrice();
                Wallet wallet = walletDao.getById(walletId);
                Double oftp = wallet.getFrozenAmount();
                oftp -= ftp;
                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(walletId);
                updateWallet.setFrozenAmount(oftp);
                //walletDao.updateDecreaseFrozenAmount(updateWallet);
                walletDao.update(updateWallet);
                //wait for tax
                Long setupId = tax.getWalletSetupId();
                Double taxAmount = tax.getTotalAmountTax();
                WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
                Double originalTax =  walletSetupOriginal.getTransferTaxAmount();
                originalTax -= taxAmount;
                //
                WalletSetup walletSetup = new WalletSetup();
                walletSetup.setId(setupId);
                walletSetup.setTransferTaxAmount(originalTax);
                //walletSetupDao.updateDecreaseTransferTaxAmount(walletSetup);
                walletSetupDao.updateNotNull(walletSetup);

            } else {
                Long setupId = transaction.getFromWalletSetupId();
                Double frozenAmount = transaction.getSetupAmount();
                WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
                Double originalFA =  walletSetupOriginal.getFrozenAmount();
                originalFA -= frozenAmount;

                //prepare decrease receiving amount
                WalletSetup walletSetup = new WalletSetup();
                walletSetup.setId(transaction.getFromWalletSetupId());
                walletSetup.setFrozenAmount(originalFA);
                //walletSetupDao.updateDecreaseFrozenAmount(walletSetup);
                walletSetupDao.updateNotNull(walletSetup);
            }

            if(toTransactionAction != null){

                TransactionTax tax = toTransactionAction.getTax();
                tax.setPaymentDate(currentDate);
                tax.setIsPaid(false);
                transactionTaxDao.updatePayment(tax);
                //
                Long walletId = tax.getWalletId();
                Double receivingAmount = transaction.getToTotalPrice();
                Wallet wallet = walletDao.getById(walletId);
                Double originalReceivingAmount = wallet.getReceivingAmount();
                originalReceivingAmount -= receivingAmount;
                //
                Wallet updateWallet = new Wallet();
                updateWallet.setId(walletId);
                updateWallet.setReceivingAmount(originalReceivingAmount);
                //walletDao.updateDecreaseReceivingAmount(updateWallet);
                walletDao.update(updateWallet);
                //
                Long setupId = tax.getWalletSetupId();
                Double taxAmount = tax.getTotalAmountTax();
                WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
                Double originalTax =  walletSetupOriginal.getTransferTaxAmount();
                originalTax -=taxAmount;
                //wait for tax
                WalletSetup walletSetup = new WalletSetup();
                walletSetup.setId(setupId);
                walletSetup.setTransferTaxAmount(originalTax);
                //walletSetupDao.updateDecreaseTransferTaxAmount(walletSetup);
                walletSetupDao.updateNotNull(walletSetup);
            } else {
                Long setupId = transaction.getToWalletSetupId();
                Double receivingAmount = transaction.getSetupAmount();
                WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
                Double originalRA =  walletSetupOriginal.getReceivingAmount();
                originalRA -= receivingAmount;
                //prepare decrease receiving amount
                WalletSetup walletSetup = new WalletSetup();
                walletSetup.setId(setupId);
                walletSetup.setReceivingAmount(originalRA);
                //walletSetupDao.updateDecreaseReceivingAmount(walletSetup);
                walletSetupDao.updateNotNull(walletSetup);
            }

            transactionDao.updateState(transaction);


        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            transactionDao.delete(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    private synchronized String getValidOrderCode(String orderCode) throws DatabaseException {
        try {
            if (!transactionDao.isOrderCode(orderCode)) {
                return orderCode;
            } else {
                orderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
                return getValidOrderCode(orderCode);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setTransactionDao(ITransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public void setTransactionTaxDao(ITransactionTaxDao transactionTaxDao) {
        this.transactionTaxDao = transactionTaxDao;
    }

    public void setTransactionDataDao(ITransactionDataDao transactionDataDao) {
        this.transactionDataDao = transactionDataDao;
    }

    public void setTransactionActionDao(ITransactionProcessDao transactionActionDao) {
        this.transactionActionDao = transactionActionDao;
    }

    public void setExchangeDao(IExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setPartitionSetupDao(IPartitionSetupDao partitionSetupDao) {
        this.partitionSetupDao = partitionSetupDao;
    }

    public void setWalletExchangeDao(IWalletExchangeDao walletExchangeDao) {
        this.walletExchangeDao = walletExchangeDao;
    }
}
