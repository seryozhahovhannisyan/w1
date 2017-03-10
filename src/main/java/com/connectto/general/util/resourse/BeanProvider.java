package com.connectto.general.util.resourse;

import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.util.Initializer;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionDepositManager;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.dataaccess.service.wallet.ICreditCardManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : Serozh
 *         Date: 24.08.13
 *         Time: 1:55
 */
public class BeanProvider {

//    private static ApplicationContext context = Initializer.getApplicationContext();
    private static ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");


    public static IPartitionSetupManager getPartitionSetupManager() throws BeansException {
        return (IPartitionSetupManager) context.getBean("PartitionSetupManagerImpl");
    }

    public static IWalletManager getWalletWalletManager() throws BeansException {
        return (IWalletManager) context.getBean("WalletManagerImpl");
    }

    public static ICreditCardManager getCreditCardManager() throws BeansException {
        return (ICreditCardManager) context.getBean("CreditCardManagerImpl");
    }

}
