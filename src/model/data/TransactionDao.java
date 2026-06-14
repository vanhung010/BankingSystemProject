package model.data;

import model.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private DataCenter dataCenter = DataCenter.getInstance();

    public void addTransactionPlus(Transaction transaction) {
        transaction.setTransactionId(dataCenter.getTransactionList().size() + 1);
        dataCenter.getTransactionList().add(transaction);
    }
    // Thêm hàm này để lấy lịch sử giao dịch
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> result = new ArrayList<>();
        // Duyệt qua kho dữ liệu dùng chung
        for (Transaction t : dataCenter.getTransactionList()) {
            if (t.getAccountId() != null && t.getAccountId() == accountId) {
                result.add(t);
            }
        }
        return result;
    }
}
