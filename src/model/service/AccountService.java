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
    public boolean changeAccountStatus(Customer customer, int accountId, model.entity.enums.AccountStatus newStatus) {
        if (customer.getAccountList() == null || customer.getAccountList().isEmpty()) {
            System.out.println("=> Lỗi: Bạn chưa có tài khoản nào trong hệ thống.");
            return false;
        }

        for (model.entity.Account acc : customer.getAccountList()) {
            if (acc.getAccountId() == accountId) {
                // Cập nhật trạng thái
                acc.setAccountStatus(newStatus);
                System.out.println("=> Thành công: Tài khoản [" + accountId + "] đã được chuyển sang trạng thái " + newStatus);
                return true;
            }
        }

        System.out.println("=> Lỗi: Không tìm thấy tài khoản [" + accountId + "] của bạn.");
        return false;
    }

}

