package com.connectto.wallet.model.wallet.lcp;

import java.util.ArrayList;
import java.util.List;

public enum TransactionType {

    WALLET(1, "Wallet","wallet_money_logo.png",false),
    GAME_POINT(2, "Game Point","",false),
    CASH(3, "Cash","cash_logo.png",false),
    BIT_COIN(4, "Bit Coin","bit_coin.png",false),

    MASTER_CARD(5, "Master Card","mastercard.png",true),
    VISA_CARD(6, "Visa Card","vsisa.png",true),
    AMERICAN_EXPRESS(7, "American Express","american_express.png",true),
    DISCOVER (8, "Discover","discover.png",true),
    
    DINERS_CLUB (9, "Diners Club","diners_club.png",false),
    JCB (10, "JCB","jcb.png",false);

    TransactionType(int id, String value,String logo, boolean isCreditCard) {
        this.id = id;
        this.value = value;
        this.logo = logo;
        this.isCreditCard = isCreditCard;
    }

    public static TransactionType getDefault() {
        return null;
    }

    public static synchronized TransactionType valueOf(final int value) {
        for (TransactionType e : TransactionType.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return getDefault();
    }

    public static synchronized List<TransactionType> getCreditCards() {
        final List<TransactionType> creditCards = new ArrayList<TransactionType>();
        for (TransactionType e : TransactionType.values()) {
            if (e.isCreditCard) {
                creditCards.add(e);
            }
        }
        return creditCards;
    }

    private final int id;
    private final String value;
    private final String logo;
    private final boolean isCreditCard;

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getLogo() {
        return logo;
    }

    public boolean getIsCreditCard() {
        return isCreditCard;
    }
}
