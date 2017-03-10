package com.connectto.wallet.web.action.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.dataaccess.service.wallet.ICreditCardManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.CreditCard;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.TransactionType;
import org.apache.log4j.Logger;

import java.util.*;

/*import com.e_xact.secure2.vplug_in.transaction.rpc_enc.ServiceLocator;
import com.e_xact.secure2.vplug_in.transaction.rpc_enc.ServiceSoap;
import com.e_xact.secure2.vplug_in.transaction.rpc_enc.encodedTypes.Constant;
import com.e_xact.secure2.vplug_in.transaction.rpc_enc.encodedTypes.Transaction;
import com.e_xact.secure2.vplug_in.transaction.rpc_enc.encodedTypes.TransactionResult;*/

/**
 * Created by Serozh on 1/29/16.
 */
public class CreditCardAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(CreditCardAction.class.getSimpleName());
    private static final int MAX_CREDIT_CARD_COUNT = 6;

    private ICreditCardManager creditCardManager;
    private IWalletLoggerManager walletLoggerManager;
    //payment
    // private TransactionResult transactionResult;
    private String errorMSg;
    private String exactId;
    private String password;
    private String transactionType;
    private String cardNumber;
    private String cardName;
    private String amount;
    private String expDate;
    //card
    private Long creditCardId;
    private List<Long> creditCardIdes;

    private String holderName;
    private String number;
    private String expiryDate;
    private String cvv;
    private String type;
    //optional billingAddress
    private String country;
    private String zip;
    private String state;
    private String city;

    private ResponseDto responseDto;
    private List<CreditCard> creditCards;
    private CreditCard creditCard;

    private String walletId;

   /* public String payment() {
        System.out.println("entry");
        cardNumber = "24242";
        cardName = "fssfsf";
        amount = "0.1";
        expDate = "02/17";
        try {

            Transaction src = new Transaction();

            // arajin 3 parametrer@ chshtel Aramic
            src.setExactID("B36748-01");
            src.setPassword("539528wm");
            src.setTransaction_Type("00");
            src.setCard_Number(cardNumber);
            src.setCardHoldersName(cardName);
            src.setDollarAmount(amount);
            src.setExpiry_Date(expDate);


            ServiceLocator locator = new ServiceLocator();
            ServiceSoap sc = locator.getServiceSoap(new URL(Constant.GLOBALGETWAYE4_URL));

            transactionResult = sc.sendAndCommit(src);


        } catch (Exception ex) {
            logger.error(ex);
        }

        return SUCCESS;
    }*/

    public String createCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        Double money = wallet.getMoney();
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());

        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return getIsMobile() ? "m-error" : ERROR;
        }
        if (!validateFields()) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            return getIsMobile() ? "m-error" : ERROR;
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setWalletId(wallet.getId());
        creditCard.setUserId(user.getId());
        //
        creditCard.setHolderName(holderName.trim());
        creditCard.setNumber(number.trim());
        creditCard.setExpiryDate(Utils.toSimpleDate(expiryDate));
        creditCard.setCvv(cvv.trim());
        creditCard.setTransactionType(TransactionType.valueOf(Integer.parseInt(type)));
        //optional billingAddress
        if (!Utils.isEmpty(country)) {
            creditCard.setCountry(country.trim());
        }
        if (!Utils.isEmpty(zip)) {
            creditCard.setZip(zip.trim());
        }
        if (!Utils.isEmpty(state)) {
            creditCard.setState(state.trim());
        }
        if (!Utils.isEmpty(city)) {
            creditCard.setCity(city.trim());
        }
        //
        creditCard.setIsEnabled(true);
        creditCard.setIsDeleted(false);
        creditCard.setPriority(0);
        //
        creditCard.setCreatedAt(currentDate);
        //creditCard.setUpdatedAt(updatedAt);
        //creditCard.setupdatedDesc(updatedDesc);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("walletId", wallet.getId());
        try {
            int count = creditCardManager.getCountByParams(params);
            if (count >= MAX_CREDIT_CARD_COUNT) {
                //todo msg
                session.put(ConstantGeneral.ERR_MESSAGE, getText("your credit card limit is 6"));
                writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.ERROR, LogAction.CREATE, "limit expired");
                return ERROR;
            }
            creditCardManager.add(creditCard);
        } catch (InternalErrorException e) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.CREATE, "");
            return getIsMobile() ? "m-error" : ERROR;
        }

        return SUCCESS;
    }

    public String viewCards() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return getIsMobile() ? "m-error" : ERROR;
        }

        return getIsMobile() ? "m-success" : SUCCESS;
    }

    public String viewCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (creditCardId < 1) {
            String msg = "Invalid credit card id";
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            creditCard = creditCardManager.getById(creditCardId);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, ex, LogLevel.ERROR, LogAction.READ, "");
        }

        return SUCCESS;
    }

    public String loadCards() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("walletId", wallet.getId());

        try {

            creditCards = creditCardManager.getByParams(params);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, ex, LogLevel.ERROR, LogAction.READ, "");
        }

        return SUCCESS;
    }

    public String getAvailableCardsCount() {

        responseDto.cleanMessages();

        if (Utils.isEmpty(walletId)) {
            String msg = getText("wallet.back.end.message.empty.walletId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
//            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, msg);
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();

        try {
            params.put("walletId", Long.parseLong(walletId));
            int count  = creditCardManager.getCountByParams(params);
            responseDto.addResponse("count",count);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            writeLog(CreditCardAction.class.getSimpleName(), null, null, ex, LogLevel.ERROR, LogAction.READ, "");
        }

        return SUCCESS;
    }

    public String editCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());

        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return getIsMobile() ? "m-error" : ERROR;
        }
        if (!validateFields()) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            return getIsMobile() ? "m-error" : ERROR;
        }

        if (creditCardId < 1) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            return getIsMobile() ? "m-error" : ERROR;
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setId(creditCardId);
        creditCard.setWalletId(wallet.getId());
        creditCard.setUserId(user.getId());
        //
        creditCard.setHolderName(holderName.trim());
        creditCard.setNumber(number.trim());
        creditCard.setExpiryDate(Utils.toSimpleDate(expiryDate));
        creditCard.setCvv(cvv.trim());
        creditCard.setTransactionType(TransactionType.valueOf(Integer.parseInt(type)));
        //optional billingAddress
        //optional billingAddress
        if (!Utils.isEmpty(country)) {
            creditCard.setCountry(country.trim());
        }
        if (!Utils.isEmpty(zip)) {
            creditCard.setZip(zip.trim());
        }
        if (!Utils.isEmpty(state)) {
            creditCard.setState(state.trim());
        }
        if (!Utils.isEmpty(city)) {
            creditCard.setCity(city.trim());
        }
        //

        creditCard.setUpdatedAt(currentDate);

        try {
            CreditCard old = creditCardManager.getById(creditCardId);
            creditCard.initDesc(old);
            creditCardManager.update(creditCard);
        } catch (InternalErrorException e) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            return getIsMobile() ? "m-error" : ERROR;
        } catch (EntityNotFoundException e) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            return getIsMobile() ? "m-error" : ERROR;
        }

        return SUCCESS;
    }

    public String enableCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (creditCardId < 1) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {

            CreditCard creditCard = new CreditCard();
            creditCard.setId(creditCardId);
            creditCard.setIsEnabled(true);
            creditCard.setUpdatedAt(currentDate);
            creditCard.setUpdatedDesc(";Credit card enabled at " + currentDate.toString());

            creditCardManager.enable(creditCard);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");

        } catch (EntityNotFoundException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);

        }

        return SUCCESS;
    }

    public String disableCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (creditCardId < 1) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {

            CreditCard creditCard = new CreditCard();
            creditCard.setId(creditCardId);
            creditCard.setIsEnabled(false);
            creditCard.setUpdatedAt(currentDate);
            creditCard.setUpdatedDesc(";Credit card disabled at " + currentDate.toString());

            creditCardManager.disable(creditCard);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String deleteCard() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (creditCardId < 1) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {

            CreditCard creditCard = new CreditCard();
            creditCard.setId(creditCardId);
            creditCard.setIsDeleted(true);
            creditCard.setUpdatedAt(currentDate);
            creditCard.setUpdatedDesc(";Credit card deleted at " + currentDate.toString());

            creditCardManager.delete(creditCard);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String prioritiesCard() {
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (Utils.isEmpty(creditCardIdes)) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            List<CreditCard> cards = new ArrayList<CreditCard>();
            for (int i = 0; i < creditCardIdes.size(); i++) {
                CreditCard creditCard = new CreditCard();
                creditCard.setId(creditCardIdes.get(i));
                creditCard.setPriority(i + 1);
                creditCard.setUpdatedAt(currentDate);
                creditCard.setUpdatedDesc(";Credit card reordered at " + currentDate.toString());
                cards.add(creditCard);
            }
            creditCardManager.reOrderPriorities(cards);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String makeDefaultCard() {
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Date currentDate = new Date(System.currentTimeMillis());
        if (wallet == null) {
            logger.error("wallet does not exists  or something error occurred with manager layer");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            return ERROR;
        }

        if (creditCardId < 1) {
            String msg = (String) session.get(ConstantGeneral.ERR_MESSAGE);// todo
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, null, LogLevel.DEBUG, LogAction.UTIL, msg);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {

            CreditCard creditCard = new CreditCard();
            creditCard.setId(creditCardId);
            creditCard.setWalletId(wallet.getId());
            creditCard.setPriority(1);
            creditCard.setUpdatedAt(currentDate);
            creditCard.setUpdatedDesc(";Credit card enabled at " + currentDate.toString());

            creditCardManager.makeDefaultCard(creditCard);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(CreditCardAction.class.getSimpleName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");

        }

        return SUCCESS;
    }

    private synchronized boolean validateFields() {
        boolean isValid = true;

        if (Utils.isEmpty(holderName)) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.required", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        if (Utils.isEmpty(number)) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.required", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        if (Utils.isEmpty(expiryDate)) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.required", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        if (Utils.isEmpty(cvv)) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.required", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        if (Utils.isEmpty(type)) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.required", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        if (!isValid) {
            return isValid;
        }

        try {
            int t = Integer.parseInt(this.type);
            TransactionType tt = TransactionType.valueOf(t);
            if (tt != null && !tt.getIsCreditCard()) {
                session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.invalid", new String[]{getText("pages.registration.surname")}));
                isValid = true;
            }
        } catch (Exception e) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.invalid", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        try {
            if (Utils.toSimpleDate(expiryDate) != null) {
                session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.invalid", new String[]{getText("pages.registration.surname")}));
                isValid = true;
            }
        } catch (Exception e) {
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.invalid", new String[]{getText("pages.registration.surname")}));
            isValid = true;
        }

        return isValid;
    }

    

    /*getter / setter*/

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setCreditCardId(String creditCardId) {
        try {
            this.creditCardId = Long.parseLong(creditCardId);
        } catch (Exception e) {
            logger.error(e);
            this.creditCardId = -1L;
        }
    }

    public void setCreditCardIdes(String cardIdes) {
        creditCardIdes = new ArrayList<Long>();
        String[] ides = cardIdes.split(",");
        try {
            for (String id : ides) {
                creditCardIdes.add(Long.parseLong(id));
            }
        } catch (Exception e) {
            logger.error(e);
            this.creditCardIdes = null;
        }
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    /*Payment*/
    /*public TransactionResult getTransactionResult() {
        return transactionResult;
    }*/

    public String getErrorMSg() {
        return errorMSg;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setCreditCardManager(ICreditCardManager creditCardManager) {
        this.creditCardManager = creditCardManager;
    }

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }
}
