package model.service;

import model.data.AccountDao;
import model.data.SystemDao;
import model.data.TransactionDao;
import model.data.DataCenter;
import model.entity.SavingAccount;
import model.entity.Transaction;
import model.entity.enums.AccountStatus;
import model.entity.enums.TransactionType;
import model.pattern.observer.AccountStatusLogger;
import model.pattern.strategy.DemandInterestStrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SavingService {

    private AccountDao accountDao = new AccountDao();
    private SystemDao systemDao = new SystemDao();
    private TransactionDao transactionDao = new TransactionDao();

    public void checkSavingAccountExpried(){
        List<SavingAccount> savingAccountList = accountDao.getAllSavingAccountActive();
        for(SavingAccount savingAccount : savingAccountList){
            //Nếu đã lố ngày tất toán thực hiện gia hạn
            if(savingAccount.getMaturityDate().isBefore(systemDao.getTimeSystem())){
                //tạo lịch sử giao dịch
                //số tiền lãi cộng vào
                double amount = savingAccount.getInterestStrategy().calcInterest(savingAccount.getBalance(), savingAccount.getInterest(), savingAccount.getTerm());
                Transaction transaction = new Transaction(TransactionType.INTEREST_PAYMENT, amount, systemDao.getTimeSystem().atTime(LocalTime.now()), savingAccount.getAccountId(), null, "Cộng tiền lãi");
                transactionDao.addTransactionPlus(transaction);

                savingAccount.savingExtension();//thực hiện gia hạn, cập nhật số tiền, cập nhật ngày tháng
                //cập nhật soos dư
                accountDao.updateBalance(savingAccount.getAccountId(), savingAccount.getBalance());
                //cập nhật ngày tháng
                accountDao.updateDateSavingAccount(savingAccount);
            }
        }
    }

    /**
     * Tất toán sổ tiết kiệm (moved from SavingClosureService)
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

            double interestEarned;
            String closureType;

            if (daysBeforeMaturity <= 0) {
                // Đã hết hạn hoặc tất toán đúng hạn: dùng lãi kì hạn (TermInterestStrategy)
                // Lãi tính theo kì (term là số tháng)
                interestEarned = account.getInterestStrategy()
                        .calcInterest(account.getBalance(), account.getInterest(), account.getTerm());
                closureType = "Tất toán bình thường (đã hết hạn)";
            } else {
                // Tất toán sớm: dùng lãi không kì hạn (DemandInterestStrategy) theo số ngày đã gửi
                interestEarned = new DemandInterestStrategy()
                        .calcInterest(account.getBalance(), DataCenter.getInstance().getBankingSystem().getDemandInterestRate(), (int) daysPassed);
                closureType = "Tất toán sớm (còn " + daysBeforeMaturity + " ngày)";
            }

            // Tính số tiền thực nhận (gốc + lãi tính theo loại trên)
            double amountToReceive = account.getBalance() + interestEarned;

            // Cập nhật số dư tài khoản
            account.setBalance(amountToReceive);
            

            account.addObserver(new AccountStatusLogger());
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
     * Lấy danh sách các sổ tiết kiệm có thể tất toán của khách hàng (moved)
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
