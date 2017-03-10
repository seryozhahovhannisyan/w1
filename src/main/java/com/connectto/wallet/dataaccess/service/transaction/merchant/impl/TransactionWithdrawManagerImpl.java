package com.connectto.wallet.dataaccess.service.transaction.merchant.impl;

import com.connectto.general.exception.*;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.general.util.HttpURLBaseConnection;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.withdraw.*;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.model.transaction.merchant.withdraw.*;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by htdev001 on 8/26/14.
 */

@Transactional(readOnly = true)
public class TransactionWithdrawManagerImpl implements ITransactionWithdrawManager {

    private IMerchantWithdrawDao merchantWithdrawDao;
    private IMerchantWithdrawTaxDao merchantWithdrawTaxDao;
    private ITransactionWithdrawDao transactionWithdrawDao;
    private ITransactionWithdrawExchangeDao exchangeDao;
    private ITransactionWithdrawExchangeTaxDao exchangeTaxDao;
    private ITransactionWithdrawProcessDao processDao;
    private ITransactionWithdrawProcessTaxDao transactionWithdrawProcessTaxDao;
    private ITransactionWithdrawTaxDao taxDao;
    private IWalletSetupWithdrawTaxDao walletSetupWithdrawTaxDao;

    private IWalletDao walletDao;
    private IWalletSetupDao walletSetupDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void start(TransactionWithdraw data) throws InternalErrorException, PermissionDeniedException {

        TransactionWithdrawProcess process = null;
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);

        TransactionWithdrawTax tax = data.getTax();

        TransactionWithdrawProcessTax processTax = tax.getProcessTax();
        TransactionWithdrawExchangeTax exchangeTax = tax.getExchangeTax();
        WalletSetupWithdrawTax setupWithdrawTax = tax.getSetupWithdrawTax();
        MerchantWithdrawTax merchantWithdrawTax = tax.getMerchantWithdrawTax();

        TransactionWithdrawExchange exchange;
        TransactionWithdrawExchange processTaxExchange;

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

                exchangeTaxDao.add(exchangeTax);
                Long exchangeTaxId = exchangeTax.getId();

                exchange.setExchangeTaxId(exchangeTaxId);
                exchangeDao.add(exchange);
                Long exchangeId = exchange.getId();
                process.setExchangeId(exchangeId);
                tax.setExchangeTaxId(exchangeTaxId);
            }

            processTaxExchange = processTax.getExchange();
            if (processTaxExchange != null) {

                TransactionWithdrawExchangeTax processTaxExchangeTax = processTaxExchange.getExchangeTax();
                if (processTaxExchangeTax != null) {
                    exchangeTaxDao.add(processTaxExchangeTax);
                    Long processTaxExchangeTaxId = processTaxExchangeTax.getId();
                    processTaxExchange.setExchangeTaxId(processTaxExchangeTaxId);
                }

                exchangeDao.add(processTaxExchange);

                Long processTaxExchangeId = processTaxExchange.getId();
                processTax.setExchangeId(processTaxExchangeId);

            }

            TransactionWithdrawExchange setupWithdrawTaxExchange = setupWithdrawTax.getExchange();
            if (setupWithdrawTaxExchange != null) {
                exchangeDao.add(setupWithdrawTaxExchange);
                setupWithdrawTax.setExchangeId(setupWithdrawTaxExchange.getId());

            }

            walletSetupWithdrawTaxDao.add(setupWithdrawTax);
            Long setupWithdrawTaxId = setupWithdrawTax.getId();
            process.setSetupWithdrawTaxId(setupWithdrawTaxId);
            tax.setSetupWithdrawTaxId(setupWithdrawTaxId);

            transactionWithdrawProcessTaxDao.add(processTax);
            Long processTaxId = processTax.getId();
            process.setProcessTaxId(processTaxId);
            tax.setProcessTaxId(processTaxId);

            processDao.add(process);
            long processId = process.getId();


            MerchantWithdraw merchantWithdraw = data.getMerchantWithdraw();
            TransactionWithdrawExchange merchantWithdrawTaxExchange = merchantWithdrawTax.getExchange();
            if (merchantWithdrawTaxExchange != null) {
                exchangeDao.add(merchantWithdrawTaxExchange);
                merchantWithdrawTax.setExchangeId(merchantWithdrawTaxExchange.getId());
            }

            merchantWithdrawTaxDao.add(merchantWithdrawTax);
            merchantWithdraw.setMerchantWithdrawTaxId(merchantWithdrawTax.getId());
            tax.setMerchantWithdrawTaxId(merchantWithdrawTax.getId());

            merchantWithdrawDao.add(merchantWithdraw);
            data.setMerchantWithdrawId(merchantWithdraw.getId());

            taxDao.add(tax);
            long taxId = tax.getId();

            data.setTaxId(taxId);
            data.setProcessStartId(processId);

            transactionWithdrawDao.add(data);
            //
            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wMoney = wallet.getMoney();
            Double wFa = wallet.getFrozenAmount();
            wFa += wtp;
            if(wMoney < wFa){
                throw new PermissionDeniedException("Te wallet available balance less from needed withdrawal transaction");
            }
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wFa);
            updateWallet.setIsLocked(true);
            walletDao.update(updateWallet);
            //wait for tax
            Double sta = data.getSetupTotalAmount();

            WalletSetup walletSetupOriginal = walletSetupDao.getById(setupId);
            Double sTta = walletSetupOriginal.getTransferTaxAmount();
            sTta += sta;
            //
            WalletSetup walletSetup = new WalletSetup();
            walletSetup.setId(setupId);
            walletSetup.setTransferTaxAmount(sTta);
            walletSetupDao.updateNotNull(walletSetup);

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TransactionWithdraw cancel(Map<String,Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException , HttpConnectionDeniedException, MerchantApiException {

        TransactionWithdraw  data = null;
        TransactionWithdrawProcess process = null;

        try {

            data = transactionWithdrawDao.getUniqueByParams(params);
            String orderCode = (String)params.get("orderCode");
            if(!data.getOrderCode().equalsIgnoreCase(orderCode)){
                throw new PermissionDeniedException("Invalid Order code");
            }
            if(data == null){
                throw new EntityNotFoundException("Could not found withdraw by params" + params);
            }

            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            if (data.getProcessEnd() == null && data.getProcessStart().getState().getId() == TransactionState.PENDING.getId()) {
                process = data.getProcessEnd();
            } else {
                throw new PermissionDeniedException("Unsupported process ");
            }

            TransactionWithdraw startWithdraw = transactionWithdrawDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startWithdraw.getId());
            TransactionWithdrawTax tax = startWithdraw.getTax();

            process = startWithdraw.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionWithdrawDao.update(data);

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            wfa -= wtp;
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wfa);
            updateWallet.setIsLocked(false);
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

            HttpURLBaseConnection.merchantCancelWithdraw("",prepareToMerchantRequest(data), data.isEncoded());

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
        return data;
    }

    private JSONObject prepareToMerchantRequest(TransactionWithdraw  data){
        JSONObject object = new JSONObject();
        object.put("withdrawId", data.getId());
        object.put("walletId", data.getWalletId());
        object.put("orderCode", data.getOrderCode());
        return object;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TransactionWithdraw approve(Map<String,Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException {

        TransactionWithdraw  data = null;
        TransactionWithdrawProcess process = null;

        try {

            data = transactionWithdrawDao.getUniqueByParams(params);
            String orderCode = (String)params.get("orderCode");
            if(!data.getOrderCode().equalsIgnoreCase(orderCode)){
                throw new PermissionDeniedException("Invalid Order code");
            }
            if(data == null){
                throw new EntityNotFoundException("Could not found withdraw by params" + params);
            }

            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            if (data.getProcessEnd() == null && data.getProcessStart().getState().getId() == TransactionState.PENDING.getId()) {
                process = data.getProcessEnd();
            } else {
                throw new PermissionDeniedException("Unsupported process ");
            }

            TransactionWithdraw startWithdraw = transactionWithdrawDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startWithdraw.getId());
            TransactionWithdrawTax tax = startWithdraw.getTax();

            process = startWithdraw.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionWithdrawDao.update(data);

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wfa = wallet.getFrozenAmount();
            wfa -= wtp;
            //
            Wallet updateWallet = new Wallet();
            updateWallet.setId(walletId);
            updateWallet.setFrozenAmount(wfa);
            updateWallet.setIsLocked(false);
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

            HttpURLBaseConnection.merchantApproveWithdraw("",prepareToMerchantRequest(data), data.isEncoded());

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
        return data;
    }

    @Override
    public TransactionWithdraw getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionWithdrawDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionWithdraw getUniqueByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionWithdrawDao.getUniqueByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    private synchronized String getValidOrderCode(String orderCode) throws DatabaseException {
        try {
            if (!transactionWithdrawDao.isOrderCode(orderCode)) {
                return orderCode;
            } else {
                orderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
                return getValidOrderCode(orderCode);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void setMerchantWithdrawDao(IMerchantWithdrawDao merchantWithdrawDao) {
        this.merchantWithdrawDao = merchantWithdrawDao;
    }

    public void setMerchantWithdrawTaxDao(IMerchantWithdrawTaxDao merchantWithdrawTaxDao) {
        this.merchantWithdrawTaxDao = merchantWithdrawTaxDao;
    }

    public void setTransactionWithdrawDao(ITransactionWithdrawDao transactionWithdrawDao) {
        this.transactionWithdrawDao = transactionWithdrawDao;
    }

    public void setExchangeDao(ITransactionWithdrawExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    public void setExchangeTaxDao(ITransactionWithdrawExchangeTaxDao exchangeTaxDao) {
        this.exchangeTaxDao = exchangeTaxDao;
    }

    public void setProcessDao(ITransactionWithdrawProcessDao processDao) {
        this.processDao = processDao;
    }

    public void setTransactionWithdrawProcessTaxDao(ITransactionWithdrawProcessTaxDao transactionWithdrawProcessTaxDao) {
        this.transactionWithdrawProcessTaxDao = transactionWithdrawProcessTaxDao;
    }

    public void setTaxDao(ITransactionWithdrawTaxDao taxDao) {
        this.taxDao = taxDao;
    }

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setWalletSetupWithdrawTaxDao(IWalletSetupWithdrawTaxDao walletSetupWithdrawTaxDao) {
        this.walletSetupWithdrawTaxDao = walletSetupWithdrawTaxDao;
    }
}
