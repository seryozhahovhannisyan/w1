package transaction;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.wallet.dataaccess.service.transaction.purchase.ITransactionPurchaseManager;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchase;
import com.connectto.wallet.util.TransactionExporter;
import com.itextpdf.text.DocumentException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by Serozh on 11/16/2016.
 */
public class TransactionPurchaseManagerTest {
    public static void main(String[] args) {
        try {

            ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
            ITransactionPurchaseManager transactionPurchaseManager = ( ITransactionPurchaseManager)  context.getBean("TransactionPurchaseManagerImpl");
            TransactionPurchase transactionPurchase = TransactionUtil.initDemoTransactionPurchase();

            File file = new File("C:/Users/Serozh/Desktop/TransactionExporter/"+ System.currentTimeMillis()+".pdf");

            TransactionExporter.export(transactionPurchase, file);
            //transactionPurchaseManager.freeze(transactionPurchase);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        } catch (InternalErrorException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
