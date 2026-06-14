package model.service;

import model.data.AccountDao;
import model.data.TransactionDao;
import model.data.UserDao;
import model.entity.*;
import model.pattern.factory.AccountFactory;
import model.entity.enums.TransactionType;
import java.time.LocalDateTime;

import java.util.List;

public class AccountService {

    private UserDao userDao = new UserDao();
    private AccountDao accountDao = new AccountDao();
    private TransactionDao transactionDao = new TransactionDao();


    public List<Account> getAllAccount(int idCustomer) {
        return accountDao.getAllAccountOfCustomerDao(idCustomer);

    }

    public Customer getCustomerbyId(int idCus){
        Customer customer = userDao.getCustomerById(idCus);
        if(customer == null){
            throw new RuntimeException("Không tìm thấy khách hàng");
        }
        return customer;
    }
    // Logic mở tài khoản thanh toán
    public String openCheckingAccount(Customer customer, double initialDeposit) {
        if (initialDeposit < 50000) { // Giả sử yêu cầu nạp tối thiểu 50k để mở TK thanh toán
            return "Lỗi: Số tiền nạp ban đầu tối thiểu 50,000 VNĐ.";
        }
        CheckingAccount newAccount = AccountFactory.createCheckingAccount(customer, initialDeposit);

        // Lưu vào Singleton DataCenter
       // model.data.DataCenter.getInstance().getAccountList().add(newAccount);
        accountDao.addNewAccount(newAccount);
        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                initialDeposit,
                LocalDateTime.now(),
                newAccount.getAccountId(),
                null,
                "Nạp tiền ban đầu khi mở tài khoản thanh toán"
        );
        transactionDao.addTransactionPlus(transaction);

        return "Thành công: Đã mở Tài khoản Thanh toán! Số TK của bạn là: " + newAccount.getAccountId();
    }

    // Logic mở sổ tiết kiệm
    public String openSavingAccount(Customer customer, int checkingAccId, double amount, int term) {
        // Ngoại lệ 1: điều kiện trên 1tr
        if (amount < 1000000) {
            return "Lỗi: Số tiền tối thiểu 1 triệu";
        }

        // Lấy tài khoản nguồn
        Account sourceAcc = accountDao.getAccountById(checkingAccId);

        // Không tồn tại hoặc không phải của khách hàng
        if (sourceAcc == null || sourceAcc.getOwner() == null
                || sourceAcc.getOwner().getUserId() != customer.getUserId()) {
            return "Lỗi: Không tìm thấy tài khoản thanh toán hợp lệ.";
        }

        // Phải là tài khoản THANH TOÁN
        if (!(sourceAcc instanceof CheckingAccount)) {
            return "Lỗi: Chỉ được mở sổ tiết kiệm từ tài khoản THANH TOÁN (Checkings).";
        }

        // Tài khoản phải đang hoạt động
        if (sourceAcc.getAccountStatus() != model.entity.enums.AccountStatus.ACTIVE) {
            return "Lỗi: Tài khoản thanh toán không ở trạng thái ACTIVE.";
        }

        CheckingAccount checkingAcc = (CheckingAccount) sourceAcc;

        // Ngoại lệ 2: Lỗi không đủ số dư
        if (checkingAcc.getBalance() < amount) {
            return "Lỗi: Tài khoản không đủ số dư";
        }

        // Giai đoạn 3:
        checkingAcc.setBalance(checkingAcc.getBalance() - amount); // Trừ tiền (withdraw)

        SavingAccount newSavingAcc = AccountFactory.createSavingAccount(customer, amount, term);
        accountDao.addNewAccount(newSavingAcc);

        Transaction transaction = new Transaction(
                TransactionType.OPEN_SAVING,
                amount,
                LocalDateTime.now(),
                checkingAcc.getAccountId(),
                null,
                "Mở sổ tiết kiệm mã " + newSavingAcc.getAccountId() + " kỳ hạn " + term + " tháng"
        );
        transactionDao.addTransactionPlus(transaction);

        Transaction savingTransaction = new Transaction(
                TransactionType.OPEN_SAVING,
                amount,
                LocalDateTime.now(),
                newSavingAcc.getAccountId(),
                null,
                "Nhận tiền mở sổ tiết kiệm từ tài khoản thanh toán " + checkingAcc.getAccountId()
        );
        transactionDao.addTransactionPlus(savingTransaction);

        return "Thành công: Đã mở Sổ Tiết Kiệm (Mã sổ: " + newSavingAcc.getAccountId() + ") kỳ hạn " + term + " tháng.";
    }
    public List<Transaction> getTransactionHistory(Customer customer, int accountId) {
        Account account = accountDao.getAccountById(accountId);
        if (account == null || account.getOwner().getUserId() != customer.getUserId()) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        return transactionDao.getTransactionsByAccountId(accountId);
    }

}
