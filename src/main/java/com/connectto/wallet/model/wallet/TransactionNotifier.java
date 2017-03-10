package com.connectto.wallet.model.wallet;

/**
 * Created by Serozh on 2/22/16.
 */
public class TransactionNotifier {

    private int pendingCountTotal;
    private int pendingCountSend;
    private int pendingCountRequestTransaction;

    private int completedCountTotal;
    private int completedCountSend;
    private int completedCountRequestTransaction;

    public int getPendingCountTotal() {
        return pendingCountTotal;
    }

    public void setPendingCountTotal(int pendingCountTotal) {
        this.pendingCountTotal = pendingCountTotal;
    }

    public int getPendingCountSend() {
        return pendingCountSend;
    }

    public void setPendingCountSend(int pendingCountSend) {
        this.pendingCountSend = pendingCountSend;
    }

    public int getPendingCountRequestTransaction() {
        return pendingCountRequestTransaction;
    }

    public void setPendingCountRequestTransaction(int pendingCountRequestTransaction) {
        this.pendingCountRequestTransaction = pendingCountRequestTransaction;
    }

    public int getCompletedCountTotal() {
        return completedCountTotal;
    }

    public void setCompletedCountTotal(int completedCountTotal) {
        this.completedCountTotal = completedCountTotal;
    }

    public int getCompletedCountSend() {
        return completedCountSend;
    }

    public void setCompletedCountSend(int completedCountSend) {
        this.completedCountSend = completedCountSend;
    }

    public int getCompletedCountRequestTransaction() {
        return completedCountRequestTransaction;
    }

    public void setCompletedCountRequestTransaction(int completedCountRequestTransaction) {
        this.completedCountRequestTransaction = completedCountRequestTransaction;
    }
}
