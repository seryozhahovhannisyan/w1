package transaction;

import com.connectto.wallet.creditCard.common.TransferSetting;
import com.connectto.wallet.creditCard.common.soap.ServiceLocator;
import com.connectto.wallet.creditCard.common.soap.ServiceSoap;
import com.connectto.wallet.creditCard.common.soap.encodedTypes.Transaction;
import com.connectto.wallet.creditCard.common.soap.encodedTypes.TransactionResult;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.model.wallet.CreditCard;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.net.URL;

/**
 * Created by Serozh on 11/23/2016.
 */
public class CreditCardTest {

    public static void main(String[] args) {
        TransferSetting transferSetting = new TransferSetting();
        transferSetting.setExactId("S50973-57");
        transferSetting.setTransferPwd("oySK6uCvxCS25P4RjB4Wd6MPawx4NaW7");
        try {
            TransactionResult transactionResult = chargeTest(transferSetting);
            System.out.println(transactionResult.isTransaction_Approved() + " "+
                    transactionResult.isTransaction_Error() );
        } catch (CreditCardException e) {
            e.printStackTrace();
        }
    }

    private static TransactionResult chargeTest(TransferSetting transferSetting) throws CreditCardException {

        TransactionResult res = null;
        Transaction src = new Transaction();

        src.setExactID(transferSetting.getExactId());
        src.setPassword(transferSetting.getTransferPwd());
        src.setTransaction_Type("00");

        // test params
        src.setCurrency("USD");
        src.setCard_Number("1111"); //5178058808358801 card type determined automatically from card number
        src.setCardHoldersName("1111 1111");///
        src.setDollarAmount("0.01");
        src.setExpiry_Date("0121"); // mmyy

        ServiceLocator locator = new ServiceLocator();
        try {
            ServiceSoap sc = locator.getServiceSoap(new URL("https://api.globalgatewaye4.firstdata.com/transaction/v11"));
            res = sc.sendAndCommit(src);
        } catch (Exception e) {
            throw new CreditCardException(e);
        }

        return res;
    }

    private static TransactionResult chargeTest(Double amount , CurrencyType currencyType, TransferSetting transferSetting, CreditCard creditCard) throws CreditCardException {

        TransactionResult res = null;
        Transaction src = new Transaction();

        src.setExactID(transferSetting.getExactId());
        src.setPassword(transferSetting.getTransferPwd());
        src.setTransaction_Type("00");

        // test params
        src.setCurrency(currencyType.getCode());
        src.setCard_Number(creditCard.getNumber()); //5178058808358801 card type determined automatically from card number
        src.setCardHoldersName(creditCard.getHolderName());///
        src.setDollarAmount(amount.toString());
        src.setExpiry_Date(creditCard.getExpiryDate().toString()); // mmyy

        ServiceLocator locator = new ServiceLocator();
        try {
            ServiceSoap sc = locator.getServiceSoap(new URL("https://api.globalgatewaye4.firstdata.com/transaction/v11"));
            res = sc.sendAndCommit(src);
        } catch (Exception e) {
            throw new CreditCardException(e);
        }

        return res;
    }

}
