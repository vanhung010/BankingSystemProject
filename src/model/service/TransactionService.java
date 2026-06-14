package model.service;

import model.data.AccountDao;
import model.data.TransactionDao;
import model.entity.Account;
import model.entity.Customer;
import model.entity.Transaction;

import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDao();
    private AccountDao accountDao = new AccountDao();

    public void viewTransactionHistory(Customer customer, int accountId) {
        // 1. Kiểm tra tài khoản có tồn tại và thuộc về khách hàng đang đăng nhập không
        Account account = accountDao.getAccountById(accountId);
        if (account == null || account.getOwner() == null || account.getOwner().getUserId() != customer.getUserId()) {
            System.out.println("=> Lỗi: Không tìm thấy tài khoản [" + accountId + "] hoặc tài khoản không thuộc quyền sở hữu của bạn.");
            return;
        }

        System.out.println("\n=========================================================================================================");
        System.out.println("                                    SAO KÊ GIAO DỊCH TÀI KHOẢN: " + accountId);
        System.out.println("=========================================================================================================");

        // 2. Sử dụng DAO (có chứa Singleton Pattern) để lấy danh sách giao dịch
        List<Transaction> transactions = transactionDao.getTransactionsByAccountId(accountId);

        // 3. In kết quả
        if (transactions.isEmpty()) {
            System.out.println("   Tài khoản này chưa có giao dịch nào phát sinh.");
        } else {
            for (Transaction t : transactions) {
                t.printReceipt(); // Gọi hàm in bạn vừa tạo ở file Transaction.java
            }
        }
        System.out.println("=========================================================================================================\n");
    }
}
