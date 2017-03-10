package com.connectto.wallet.dataaccess.service.transaction.merchant.impl;

import com.connectto.general.exception.*;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.general.util.HttpURLBaseConnection;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.dataaccess.dao.transaction.merchant.deposit.*;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletDao;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionDepositManager;
import com.connectto.wallet.model.transaction.merchant.deposit.*;
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
public class TransactionDepositManagerImpl implements ITransactionDepositManager {

    private IMerchantDepositDao merchantDepositDao;
    private IMerchantDepositTaxDao merchantDepositTaxDao;
    private ITransactionDepositDao transactionDepositDao;
    private ITransactionDepositExchangeDao exchangeDao;
    private ITransactionDepositExchangeTaxDao exchangeTaxDao;
    private ITransactionDepositProcessDao processDao;
    private ITransactionDepositProcessTaxDao transactionDepositProcessTaxDao;
    private ITransactionDepositTaxDao taxDao;
    private IWalletSetupDepositTaxDao walletSetupDepositTaxDao;

    private IWalletDao walletDao;
    private IWalletSetupDao walletSetupDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void start(TransactionDeposit data) throws InternalErrorException, PermissionDeniedException {

        TransactionDepositProcess process = null;
        String validOrderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);

        TransactionDepositTax tax = data.getTax();

        TransactionDepositProcessTax processTax = tax.getProcessTax();
        TransactionDepositExchangeTax exchangeTax = tax.getExchangeTax();
        WalletSetupDepositTax setupDepositTax = tax.getSetupDepositTax();
        MerchantDepositTax merchantDepositTax = tax.getMerchantDepositTax();

        TransactionDepositExchange exchange;
        TransactionDepositExchange processTaxExchange;

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

                TransactionDepositExchangeTax processTaxExchangeTax = processTaxExchange.getExchangeTax();
                if (processTaxExchangeTax != null) {
                    exchangeTaxDao.add(processTaxExchangeTax);
                    Long processTaxExchangeTaxId = processTaxExchangeTax.getId();
                    processTaxExchange.setExchangeTaxId(processTaxExchangeTaxId);
                }

                exchangeDao.add(processTaxExchange);

                Long processTaxExchangeId = processTaxExchange.getId();
                processTax.setExchangeId(processTaxExchangeId);

            }

            TransactionDepositExchange setupDepositTaxExchange = setupDepositTax.getExchange();
            if (setupDepositTaxExchange != null) {
                exchangeDao.add(setupDepositTaxExchange);
                setupDepositTax.setExchangeId(setupDepositTaxExchange.getId());

            }

            walletSetupDepositTaxDao.add(setupDepositTax);
            Long setupDepositTaxId = setupDepositTax.getId();
            process.setSetupDepositTaxId(setupDepositTaxId);
            tax.setSetupDepositTaxId(setupDepositTaxId);

            transactionDepositProcessTaxDao.add(processTax);
            Long processTaxId = processTax.getId();
            process.setProcessTaxId(processTaxId);
            tax.setProcessTaxId(processTaxId);

            processDao.add(process);
            long processId = process.getId();


            MerchantDeposit merchantDeposit = data.getMerchantDeposit();
            TransactionDepositExchange merchantDepositTaxExchange = merchantDepositTax.getExchange();
            if (merchantDepositTaxExchange != null) {
                exchangeDao.add(merchantDepositTaxExchange);
                merchantDepositTax.setExchangeId(merchantDepositTaxExchange.getId());
            }

            merchantDepositTaxDao.add(merchantDepositTax);
            merchantDeposit.setMerchantDepositTaxId(merchantDepositTax.getId());
            tax.setMerchantDepositTaxId(merchantDepositTax.getId());

            merchantDepositDao.add(merchantDeposit);
            data.setMerchantDepositId(merchantDeposit.getId());

            taxDao.add(tax);
            long taxId = tax.getId();

            data.setTaxId(taxId);
            data.setProcessStartId(processId);

            transactionDepositDao.add(data);
            //
            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            Double wtp = data.getWalletTotalPrice();
            Wallet wallet = walletDao.getById(walletId);
            Double wMoney = wallet.getMoney();
            Double wFa = wallet.getFrozenAmount();
            wFa += wtp;
            if(wMoney < wFa){
                throw new PermissionDeniedException("Te wallet available balance less from needed deposital transaction");
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
    public TransactionDeposit cancel(Map<String,Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException , HttpConnectionDeniedException, MerchantApiException {

        TransactionDeposit  data = null;
        TransactionDepositProcess process = null;

        try {

            data = transactionDepositDao.getUniqueByParams(params);
            String orderCode = (String)params.get("orderCode");
            if(!data.getOrderCode().equalsIgnoreCase(orderCode)){
                throw new PermissionDeniedException("Invalid Order code");
            }
            if(data == null){
                throw new EntityNotFoundException("Could not found deposit by params" + params);
            }

            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            if (data.getProcessEnd() == null && data.getProcessStart().getState().getId() == TransactionState.PENDING.getId()) {
                process = data.getProcessEnd();
            } else {
                throw new PermissionDeniedException("Unsupported process ");
            }

            TransactionDeposit startDeposit = transactionDepositDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startDeposit.getId());
            TransactionDepositTax tax = startDeposit.getTax();

            process = startDeposit.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionDepositDao.update(data);

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

            HttpURLBaseConnection.merchantCancelDeposit("",prepareToMerchantRequest(data), data.isEncoded());

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
        return data;
    }

    private JSONObject prepareToMerchantRequest(TransactionDeposit  data){
        JSONObject object = new JSONObject();
        object.put("depositId", data.getId());
        object.put("walletId", data.getWalletId());
        object.put("orderCode", data.getOrderCode());
        return object;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TransactionDeposit approve(Map<String,Object> params) throws InternalErrorException, PermissionDeniedException, EntityNotFoundException, HttpConnectionDeniedException, MerchantApiException {

        TransactionDeposit  data = null;
        TransactionDepositProcess process = null;

        try {

            data = transactionDepositDao.getUniqueByParams(params);
            String orderCode = (String)params.get("orderCode");
            if(!data.getOrderCode().equalsIgnoreCase(orderCode)){
                throw new PermissionDeniedException("Invalid Order code");
            }
            if(data == null){
                throw new EntityNotFoundException("Could not found deposit by params" + params);
            }

            Long walletId = data.getWalletId();
            Long setupId = data.getSetupId();

            if (data.getProcessEnd() == null && data.getProcessStart().getState().getId() == TransactionState.PENDING.getId()) {
                process = data.getProcessEnd();
            } else {
                throw new PermissionDeniedException("Unsupported process ");
            }

            TransactionDeposit startDeposit = transactionDepositDao.getBy(data.getOrderCode(), walletId, setupId);
            data.setId(startDeposit.getId());
            TransactionDepositTax tax = startDeposit.getTax();

            process = startDeposit.getProcessStart();

            processDao.add(process);
            long processId = process.getId();

            data.setTaxId(tax.getId());
            data.setProcessEndId(processId);

            transactionDepositDao.update(data);

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

            HttpURLBaseConnection.merchantApproveDeposit("",prepareToMerchantRequest(data), data.isEncoded());

        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException(e);
        }
        return data;
    }

    @Override
    public TransactionDeposit getById(Long id) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionDepositDao.getById(id);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionDeposit getUniqueByParams(Map<String, Object> params) throws InternalErrorException, EntityNotFoundException {
        try {
            return transactionDepositDao.getUniqueByParams(params);
        } catch (DatabaseException e) {
            throw new InternalErrorException(e);
        }
    }

    private synchronized String getValidOrderCode(String orderCode) throws DatabaseException {
        try {
            if (!transactionDepositDao.isOrderCode(orderCode)) {
                return orderCode;
            } else {
                orderCode = Generator.generateNumeric(ConstantGeneral.WALLET_REQUEST_CODE_LENGTH);
                return getValidOrderCode(orderCode);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void setMerchantDepositDao(IMerchantDepositDao merchantDepositDao) {
        this.merchantDepositDao = merchantDepositDao;
    }

    public void setMerchantDepositTaxDao(IMerchantDepositTaxDao merchantDepositTaxDao) {
        this.merchantDepositTaxDao = merchantDepositTaxDao;
    }

    public void setTransactionDepositDao(ITransactionDepositDao transactionDepositDao) {
        this.transactionDepositDao = transactionDepositDao;
    }

    public void setExchangeDao(ITransactionDepositExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    public void setExchangeTaxDao(ITransactionDepositExchangeTaxDao exchangeTaxDao) {
        this.exchangeTaxDao = exchangeTaxDao;
    }

    public void setProcessDao(ITransactionDepositProcessDao processDao) {
        this.processDao = processDao;
    }

    public void setTransactionDepositProcessTaxDao(ITransactionDepositProcessTaxDao transactionDepositProcessTaxDao) {
        this.transactionDepositProcessTaxDao = transactionDepositProcessTaxDao;
    }

    public void setTaxDao(ITransactionDepositTaxDao taxDao) {
        this.taxDao = taxDao;
    }

    public void setWalletDao(IWalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    public void setWalletSetupDepositTaxDao(IWalletSetupDepositTaxDao walletSetupDepositTaxDao) {
        this.walletSetupDepositTaxDao = walletSetupDepositTaxDao;
    }
}
