package model.service;

import model.data.AccountDao;
import model.data.SystemDao;
import model.data.TransactionDao;
import model.entity.SavingAccount;
import model.entity.Transaction;
import model.entity.enums.AccountStatus;
import model.entity.enums.TransactionType;
import model.pattern.observer.AccountStatusLogger;
import model.pattern.strategy.ClosureStrategy;
import model.pattern.strategy.EarlyClosureStrategy;
import model.pattern.strategy.NormalClosureStrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Singleton Service để xử lý tất toán sổ tiết kiệm
 * Sử dụng Observer Pattern (AccountStatusLogger) để log sự kiện thay đổi trạng thái CLOSED
 */
public class SavingClosureService {
    private static SavingClosureService instance;
    private AccountDao accountDao;
    private SystemDao systemDao;
    private TransactionDao transactionDao;
    private AccountStatusLogger statusLogger; // Observer để theo dõi thay đổi trạng thái

    private SavingClosureService() {
        this.accountDao = new AccountDao();
        this.systemDao = new SystemDao();
        this.transactionDao = new TransactionDao();
        this.statusLogger = new AccountStatusLogger(); // Dùng AccountStatusLogger
    }

    public static SavingClosureService getInstance() {
        if (instance == null) {
            instance = new SavingClosureService();
        }
        return instance;
    }

    /**
     * Tất toán sổ tiết kiệm
     */
    public String closeSavingAccount(int accountId) {
        try {
            SavingAccount account = accountDao.getSavingAccountById(accountId);
            if (account == null) {
                return "❌ Lỗi: Không tìm thấy sổ tiết kiệm này!";
            }

            if (account.getAccountStatus() == AccountStatus.CLOSED) {
                return "❌ Lỗi: Sổ tiết kiệm đã được tất toán!";
            }

            if (account.getAccountStatus() == AccountStatus.LOCKED) {
                return "❌ Lỗi: Sổ tiết kiệm đang bị khóa!";
            }

            LocalDate systemDate = systemDao.getTimeSystem();
            LocalDate maturityDate = account.getMaturityDate();
            
            // Tính số ngày còn lại
            long daysBeforeMaturity = ChronoUnit.DAYS.between(systemDate, maturityDate);

            // Tính tiền lãi đã kiếm được từ ngày gửi đến hôm nay
            long daysPassed = ChronoUnit.DAYS.between(account.getDepositDate(), systemDate);
            double interestEarned = account.getInterestStrategy()
                    .calcInterest(account.getBalance(), account.getInterest(), 
                                 (int)(daysPassed / 365));

            // Chọn strategy dựa trên thời gian tất toán
            ClosureStrategy strategy;
            String closureType;
            
            if (daysBeforeMaturity <= 0) {
                // Đã hết hạn hoặc tất toán đúng hạn
                strategy = new NormalClosureStrategy();
                closureType = "Tất toán bình thường (đã hết hạn)";
            } else {
                // Tất toán sớm
                strategy = new EarlyClosureStrategy();
                closureType = "Tất toán sớm (còn " + daysBeforeMaturity + " ngày)";
            }

            // Tính số tiền thực nhận
            double amountToReceive = strategy.calculateClosureAmount(
                    account.getBalance(), 
                    interestEarned, 
                    (int)daysBeforeMaturity);

            // Cập nhật số dư tài khoản
            account.setBalance(amountToReceive);
            
            // 📌 ĐĂNG KÝ OBSERVER (AccountStatusLogger) TRƯỚC KHI THAY ĐỔI TRẠNG THÁI
            // Khi changeState() được gọi, nó sẽ notify observer này
            account.addObserver(statusLogger);
            
            // Cập nhật trạng thái tài khoản → Observer sẽ được NOTIFY
            account.changeState(AccountStatus.CLOSED, "Tất toán sổ tiết kiệm");

            // Cập nhật database
            accountDao.updateBalance(accountId, amountToReceive);

            // Tạo bản ghi giao dịch
            Transaction transaction = new Transaction(
                    TransactionType.CLOSE_SAVING,
                    amountToReceive,
                    systemDate.atTime(LocalTime.now()),
                    accountId,
                    null,
                    "Tất toán sổ tiết kiệm - " + closureType
            );
            transactionDao.addTransactionPlus(transaction);

            return "✅ Tất toán sổ tiết kiệm thành công!\n" +
                   "   Loại tất toán: " + closureType + "\n" +
                   "   Số tiền lãi: " + String.format("%.2f VNĐ", interestEarned) + "\n" +
                   "   Số tiền thực nhận: " + String.format("%.2f VNĐ", amountToReceive);

        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Lấy danh sách các sổ tiết kiệm có thể tất toán của khách hàng
     */
    public String getClosableSavingAccounts(int customerId) {
        StringBuilder result = new StringBuilder();
        result.append("\n========================================\n");
        result.append("     DANH SÁCH SỔ TIẾT KIỆM CÓ THỂ TẤT TOÁN\n");
        result.append("========================================\n");

        var accounts = accountDao.getAllSavingAccountActive();
        var closableAccounts = accounts.stream()
                .filter(acc -> acc.getOwner().getUserId() == customerId && 
                       acc.getAccountStatus() != AccountStatus.CLOSED)
                .toList();

        if (closableAccounts.isEmpty()) {
            result.append("❌ Bạn không có sổ tiết kiệm nào để tất toán.\n");
        } else {
            LocalDate systemDate = systemDao.getTimeSystem();
            result.append(String.format("%-8s | %-12s | %-15s | %-15s | %-15s\n",
                    "ID", "Số dư", "Ngày hết hạn", "Còn lại", "Trạng thái"));
            result.append("--------+-------------+-----------------+-----------------+-----------\n");

            for (SavingAccount acc : closableAccounts) {
                long daysRemaining = ChronoUnit.DAYS.between(systemDate, acc.getMaturityDate());
                String daysStr = daysRemaining <= 0 ? "Đã hết hạn" : daysRemaining + " ngày";
                
                result.append(String.format("%-8d | %,12.2f | %15s | %15s | %-15s\n",
                        acc.getAccountId(),
                        acc.getBalance(),
                        acc.getMaturityDate(),
                        daysStr,
                        acc.getAccountStatus()));
            }
        }
        result.append("========================================\n");
        return result.toString();
    }
}



