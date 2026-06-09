package model.data;

import model.entity.Transaction;

public class TransactionDao {
    private DataCenter dataCenter = DataCenter.getInstance();

    public void addTransactionPlus(Transaction transaction) {
        transaction.setTransactionId(dataCenter.getTransactionList().size() + 1);
        dataCenter.getTransactionList().add(transaction);
    }
}
