package transaction;

import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.encryption.WalletEncription;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionDecripter;

/**
 * Created by Serozh on 12/5/2016.
 */
public class EncodeTest {
    public static void main(String[] args) throws EncryptException {
        System.out.println(WalletEncription.encrypt("152"));;
    }
}
