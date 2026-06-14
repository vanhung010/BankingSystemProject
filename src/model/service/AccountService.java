package model.service;

import model.data.AccountDao;
import model.data.UserDao;
import model.entity.Account;
import model.entity.CheckingAccount;
import model.entity.Customer;
import model.entity.SavingAccount;
import model.pattern.factory.AccountFactory;

import java.util.List;

public class AccountService {

    private UserDao userDao = new UserDao();
    private AccountDao accountDao = new AccountDao();


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
        model.data.DataCenter.getInstance().getAccountList().add(newAccount);

        return "Thành công: Đã mở Tài khoản Thanh toán! Số TK của bạn là: " + newAccount.getAccountId();
    }

    // Logic mở sổ tiết kiệm
    public String openSavingAccount(Customer customer, int checkingAccId, double amount, int term) {
        // Ngoại lệ 1: điều kiện trên 1tr
        if (amount < 1000000) {
            return "Lỗi: Số tiền tối thiểu 1 triệu";
        }

        // Lấy tài khoản nguồn
        Account checkingAcc = accountDao.getAccountById(checkingAccId);
        if (checkingAcc == null || checkingAcc.getOwner().getUserId() != customer.getUserId()) {
            return "Lỗi: Không tìm thấy tài khoản thanh toán hợp lệ.";
        }

        // Ngoại lệ 2: Lỗi không đủ số dư
        if (checkingAcc.getBalance() < amount) {
            return "Lỗi: Tài khoản không đủ số dư";
        }

        // Giai đoạn 3:
        checkingAcc.setBalance(checkingAcc.getBalance() - amount); // Trừ tiền (withdraw)

        SavingAccount newSavingAcc = AccountFactory.createSavingAccount(customer, amount, term);
        model.data.DataCenter.getInstance().getAccountList().add(newSavingAcc);

        return "Thành công: Đã mở Sổ Tiết Kiệm (Mã sổ: " + newSavingAcc.getAccountId() + ") kỳ hạn " + term + " tháng.";
    }

}
