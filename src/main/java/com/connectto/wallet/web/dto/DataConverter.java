package com.connectto.wallet.web.dto;

import com.connectto.general.util.Utils;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.encryption.WalletEncription;
import com.connectto.wallet.model.transaction.merchant.MerchantTransactionReviewDto;
import com.connectto.wallet.model.transaction.merchant.deposit.TransactionDeposit;
import com.connectto.wallet.model.transaction.merchant.transfer.MerchantTransferTransaction;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;
import com.connectto.wallet.model.wallet.Wallet;

/**
 * Created by Serozh on 11/17/2016.
 */
public class DataConverter {

    public static WalletDto convertWallet(Wallet wallet, boolean encript) throws EncryptException {

        WalletDto dto = new WalletDto();
        dto.setMoney(encript ? WalletEncription.encrypt("" + wallet.getMoney()) : "" + wallet.getMoney());
        dto.setFrozenAmount(encript ? WalletEncription.encrypt("" + wallet.getFrozenAmount()) : "" + wallet.getFrozenAmount());
        dto.setReceivingAmount(encript ? WalletEncription.encrypt("" + wallet.getReceivingAmount()) : "" + wallet.getReceivingAmount());
        dto.setCurrencyType(encript ? WalletEncription.encrypt("" + wallet.getCurrencyType().getId()) : "" + wallet.getCurrencyType().getId());

        return dto;
    }

    public static MerchantTransactionReviewDto convertToMerchantTransactionReviewDto(TransactionWithdraw entity, boolean encript) throws EncryptException {
        MerchantTransactionReviewDto transactionReviewDto = new MerchantTransactionReviewDto();

        transactionReviewDto.setTransactionId(entity.getId() == null ? "-1" : entity.getId().toString());
        transactionReviewDto.setActionDate(Utils.toMerchantDate(entity.getOpenedAt()));

        transactionReviewDto.setWalletId(entity.getWalletId().toString());
        transactionReviewDto.setSetupId(entity.getSetupId().toString());
        //
        transactionReviewDto.setWalletTotalPrice(encript ? WalletEncription.encrypt(entity.getWalletTotalPrice().toString()) : entity.getWalletTotalPrice().toString());
        transactionReviewDto.setWalletCurrencyType(encript ? WalletEncription.encrypt("" + entity.getWalletTotalPriceCurrencyType().getId()) : "" + entity.getWalletTotalPriceCurrencyType().getId());

        transactionReviewDto.setSetupTotalAmount(encript ? WalletEncription.encrypt(entity.getSetupTotalAmount().toString()) : entity.getSetupTotalAmount().toString());
        transactionReviewDto.setSetupCurrencyType(encript ? WalletEncription.encrypt("" + entity.getSetupTotalAmountCurrencyType().getId()) : "" + entity.getSetupTotalAmountCurrencyType().getId());

        return transactionReviewDto;
    }

    public static MerchantTransactionReviewDto convertToMerchantTransactionReviewDto(TransactionDeposit entity, boolean encript) throws EncryptException {
        MerchantTransactionReviewDto transactionReviewDto = new MerchantTransactionReviewDto();

        transactionReviewDto.setTransactionId(entity.getId() == null ? "-1" : entity.getId().toString());
        transactionReviewDto.setActionDate(Utils.toMerchantDate(entity.getOpenedAt()));

        transactionReviewDto.setWalletId(entity.getWalletId().toString());
        transactionReviewDto.setSetupId(entity.getSetupId().toString());
        //
        transactionReviewDto.setWalletTotalPrice(encript ? WalletEncription.encrypt(entity.getWalletTotalPrice().toString()) : entity.getWalletTotalPrice().toString());
        transactionReviewDto.setWalletCurrencyType(encript ? WalletEncription.encrypt("" + entity.getWalletTotalPriceCurrencyType().getId()) : "" + entity.getWalletTotalPriceCurrencyType().getId());

        transactionReviewDto.setSetupTotalAmount(encript ? WalletEncription.encrypt(entity.getSetupTotalAmount().toString()) : entity.getSetupTotalAmount().toString());
        transactionReviewDto.setSetupCurrencyType(encript ? WalletEncription.encrypt("" + entity.getSetupTotalAmountCurrencyType().getId()) : "" + entity.getSetupTotalAmountCurrencyType().getId());

        return transactionReviewDto;
    }

    public static MerchantTransactionReviewDto convertToMerchantTransactionReviewDto(MerchantTransferTransaction entity, boolean encript) throws EncryptException {
        MerchantTransactionReviewDto transactionReviewDto = new MerchantTransactionReviewDto();

        transactionReviewDto.setTransactionId(entity.getId() == null ? "-1" : entity.getId().toString());
        transactionReviewDto.setActionDate(Utils.toMerchantDate(entity.getActionDate()));

        transactionReviewDto.setWalletId(entity.getWalletId().toString());
        //
        transactionReviewDto.setWalletTotalPrice(encript ? WalletEncription.encrypt(entity.getTransferAmount().toString()) : entity.getTransferAmount().toString());
        transactionReviewDto.setWalletCurrencyType(encript ? WalletEncription.encrypt("" + entity.getTransferAmountCurrencyType().getId()) : "" + entity.getTransferAmountCurrencyType().getId());

        return transactionReviewDto;
    }


}
