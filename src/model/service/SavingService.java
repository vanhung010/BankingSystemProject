package model.service;

import model.pattern.factory.AccountFactory;
import model.entity.Account;
import model.entity.Customer;
import model.entity.BankingSystem;
import model.data.SavingDao;
import java.util.ArrayList;

public class SavingService {
    private final SavingDao savingDao;

    public SavingService() {
        this.savingDao = new SavingDao();
    }

    public boolean openSavingAccount(int accountId, Customer customer, double initialBalance, int term) {
        // 1. Kiểm tra số tiền gửi tối thiểu
        double minDeposit = BankingSystem.getMinSavingDeposit();
        if (initialBalance < minDeposit) {
            System.out.println("=> Thất bại: Số tiền gửi tiết kiệm phải từ " + minDeposit + " VNĐ trở lên.");
            return false;
        }

        // 2. Xác định mức lãi suất theo kỳ hạn
        double interestRate = 0;
        if (term == 1) {
            interestRate = BankingSystem.getInterestRate1M();
        } else if (term == 6) {
            interestRate = BankingSystem.getInterestRate6M();
        } else if (term == 12) {
            interestRate = BankingSystem.getInterestRate12M();
        } else {
            System.out.println("=> Thất bại: Hệ thống chỉ hỗ trợ kỳ hạn 1, 6, hoặc 12 tháng.");
            return false;
        }

        // 3. Nhờ Factory tạo sổ tiết kiệm
        Account newAccount = AccountFactory.createSavingAccount(accountId, customer, initialBalance, term, interestRate);

        // 4. Lưu sổ vào danh sách của khách hàng và hệ thống
        if (customer.getAccountList() == null) {
            customer.setAccountList(new ArrayList<>());
        }
        customer.getAccountList().add(newAccount);
        savingDao.saveAccount(newAccount);

        System.out.println("=> Mở sổ tiết kiệm thành công! Mã sổ: " + accountId + " | Kỳ hạn: " + term + " tháng.");
        return true;
    }
    // Thêm hàm này vào trong SavingService
    public boolean closeSavingAccount(Customer customer, int accountId) {
        if (customer.getAccountList() == null || customer.getAccountList().isEmpty()) {
            System.out.println("=> Lỗi: Bạn chưa có tài khoản/sổ tiết kiệm nào.");
            return false;
        }

        Account targetAccount = null;

        for (Account acc : customer.getAccountList()) {
            if (acc.getAccountId() == accountId && acc instanceof model.entity.SavingAccount) {
                targetAccount = acc;
                break;
            }
        }

        if (targetAccount == null) {
            System.out.println("=> Lỗi: Không tìm thấy sổ tiết kiệm mã [" + accountId + "].");
            return false;
        }

        model.entity.SavingAccount savingAcc = (model.entity.SavingAccount) targetAccount;


        double principal = savingAcc.getBalance();
        double interestRate = savingAcc.getInterest();

        /
        double interestEarned = principal * interestRate;
        double totalAmount = principal + interestEarned;

        // Thực hiện tất toán: Đưa số dư về 0
        savingAcc.setBalance(0);



        System.out.println("=> TẤT TOÁN SỔ TIẾT KIỆM THÀNH CÔNG!");
        System.out.println("- Mã sổ: " + accountId);
        System.out.printf("- Tiền gốc: %.2f VNĐ\n", principal);
        System.out.printf("- Tiền lãi: %.2f VNĐ\n", interestEarned);
        System.out.printf("- Tổng tiền thực nhận: %.2f VNĐ\n", totalAmount);

        return true;
    }
}
