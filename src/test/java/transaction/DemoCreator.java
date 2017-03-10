package transaction;

import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.model.transaction.purchase.PurchaseTicket;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionPurchaseType;

import java.util.Date;

/**
 * Created by Serozh on 11/11/2016.
 */
public class DemoCreator {

    public static synchronized PurchaseTicket initPurchase() throws InvalidParameterException {
        try {
            PurchaseTicket purchaseTicket = new PurchaseTicket();
            purchaseTicket.setItemId(Long.parseLong("1"));
            purchaseTicket.setPurchaseType(TransactionPurchaseType.typeOf("purchase"));
            purchaseTicket.setName("Name");
            purchaseTicket.setDescription("description");
            return purchaseTicket;
        } catch (Exception e) {
            throw new InvalidParameterException(e);
        }
    }

    public static synchronized Wallet initWallet(CurrencyType currencyType, String userId) {
        Wallet wallet = new Wallet();
        wallet.setId(350l);
        wallet.setUserId(Long.parseLong(userId));
        wallet.setMoney(1000000d);
        wallet.setFrozenAmount(0d);
        wallet.setCurrencyType(currencyType);

        return wallet;
    }

    public static synchronized WalletSetup initWalletSetup(CurrencyType currencyType, int partitionId) {
        WalletSetup walletSetup = new WalletSetup();
        walletSetup.setId(7l);
        walletSetup.setPartitionId(partitionId);
        walletSetup.setCurrencyType(currencyType);
        walletSetup.setBalance(1000d);
        walletSetup.setAvailableRateValues("152, 4, 125");
        walletSetup.parseAvailableRates();

        walletSetup.setTransferMinFee(9d);
        walletSetup.setTransferMaxFee(900d);
        walletSetup.setTransferFeePercent(10d);

        walletSetup.setExchangeTransferMinFee(9d);
        walletSetup.setExchangeTransferMaxFee(900d);
        walletSetup.setExchangeTransferFeePercent(10d);

        walletSetup.setReceiverMinFee(9d);
        walletSetup.setReceiverMaxFee(900d);
        walletSetup.setReceiverFeePercent(10d);

        walletSetup.setExchangeReceiverMinFee(9d);
        walletSetup.setExchangeReceiverMaxFee(900d);
        walletSetup.setExchangeReceiverFeePercent(10d);

        return walletSetup;
    }

    public static synchronized ExchangeRate initExchangeRate() {
        ExchangeRate rate = new ExchangeRate();
        rate.setOneCurrency(CurrencyType.USD);
        rate.setToCurrency(CurrencyType.AMD);
        rate.setBuy(480d);
        rate.setSell(480d);
        rate.setUpdatedDate(new Date(System.currentTimeMillis()));
        rate.setId(452149L);
        return rate;
    }

}
