package model.service;

import model.pattern.factory.AccountFactory;
import model.entity.Account;
import model.entity.Customer;
import model.entity.BankingSystem;
import model.data.AccountDao;
import java.util.ArrayList;

public class AccountService {
    private final AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }

    public boolean openCheckingAccount(int accountId, Customer customer, double initialBalance) {
        // 1. Kiểm tra số dư tối thiểu
        double minBalance = BankingSystem.getMinCheckingBalance();
        if (initialBalance < minBalance) {
            System.out.println("Giao dịch thất bại: Số dư không đạt mức tối thiểu: " + minBalance);
            return false;
        }

        // 2. tạo tài khoản
        Account newAccount = AccountFactory.createCheckingAccount(accountId, customer, initialBalance);

        // 3. Gắn tài khoản này vào danh sách sở hữu của khách hàng
        if (customer.getAccountList() == null) {
            customer.setAccountList(new ArrayList<>());
        }
        customer.getAccountList().add(newAccount);

        // 4.lưu vào hệ thống
        accountDao.saveAccount(newAccount);

        System.out.println("Mở tài khoản giao dịch thành công, Số tài khoản là: " + accountId);
        return true;
    }
}
