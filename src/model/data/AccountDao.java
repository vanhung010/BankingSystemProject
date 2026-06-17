package model.data;

import model.entity.*;
import model.entity.enums.AccountStatus;
import model.pattern.factory.AccountFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private DataCenter dataCenter = DataCenter.getInstance();

    public List<Account> getAllAccountOfCustomerDao(int idCustomer) {
        List<Account> accountList = new ArrayList<>();

        for (Account account : dataCenter.getAccountList()) {
            if (account.getOwner() != null && account.getOwner().getUserId() == idCustomer) {
                accountList.add(account);
            }
        }

        return accountList;
    }

    public Account getAccountById(int idAccount) {
         // Danh sách account đã được nạp vào DataCenter, nên chỉ cần duyệt dữ liệu trong bộ nhớ.
         for (Account account : dataCenter.getAccountList()) {
             // So sánh accountId cần tìm với accountId của từng account trong DataCenter.
             if (account.getAccountId() == idAccount) {
                 return account;
             }
         }

         // Giữ nguyên output cũ: nếu không tìm thấy account thì trả về null.
         return null;
     }

     public SavingAccount getSavingAccountById(int idAccount) {
         for (Account account : dataCenter.getAccountList()) {
             if (account instanceof SavingAccount && account.getAccountId() == idAccount) {
                 return (SavingAccount) account;
             }
         }
         return null;
     }

    public void addLoanAccount(int userId, double requestAmount, int loanTerm) {
        if (loanTerm <= 0) {
            throw new RuntimeException("Kỳ hạn vay không hợp lệ!");
        }

        Customer owner = null;
        for (User user : dataCenter.getUserList()) {
            if (user instanceof Customer && user.getUserId() == userId) {
                owner = (Customer) user;
                break;
            }
        }

        if (owner == null) {
            throw new RuntimeException("Không tìm thấy khách hàng!");
        }

        LocalDate createdAt = dataCenter.getBankingSystem().getSystemDate();
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }

        LoanAccount loanAccount = AccountFactory.createLoanAccount(owner, requestAmount, loanTerm);

        dataCenter.getAccountList().add(loanAccount);

        if (owner.getAccountList() == null) {
            owner.setAccountList(new ArrayList<>());
        }
        owner.getAccountList().add(loanAccount);
    }

    public void updateBalance(int accountId, double balance) {
        for (Account account : dataCenter.getAccountList()) {
            if (account.getAccountId() == accountId) {
                account.setBalance(balance);
                return;
            }
        }
    }

    public List<LoanAccount> getAllLoanAccountActive() {
        List<LoanAccount> loanAccountList = new ArrayList<>();

        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof LoanAccount && account.getAccountStatus() == AccountStatus.ACTIVE) {
                loanAccountList.add((LoanAccount) account);
            }
        }

        return loanAccountList;
    }

    public void lockAccount(LoanAccount loanAccount) {
       loanAccount.changeState(AccountStatus.LOCKED,  "Không thanh toán đủ nợ tháng này");
    }

    public void updateMonlyRequiredPayment(LoanAccount loanAccount) {
        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof LoanAccount && account.getAccountId() == loanAccount.getAccountId()) {
                ((LoanAccount) account).setMonthlyRequiredPayment(loanAccount.getMonthlyRequiredPayment());
                return;
            }
        }
    }

    public List<SavingAccount> getAllSavingAccountActive() {
        List<SavingAccount> savingAccountList = new ArrayList<>();

        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof SavingAccount && account.getAccountStatus() == AccountStatus.ACTIVE) {
                savingAccountList.add((SavingAccount) account);
            }
        }

        return savingAccountList;
    }

    public List<Account> getAllAccounts() {
        return dataCenter.getAccountList();
    }

    public void updateDateSavingAccount(SavingAccount savingAccount) {
        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof SavingAccount && account.getAccountId() == savingAccount.getAccountId()) {
                SavingAccount accountUpdated = (SavingAccount) account;
                accountUpdated.setDepositDate(savingAccount.getDepositDate());
                accountUpdated.setMaturityDate(savingAccount.getMaturityDate());
                return;
            }
        }
    }
    // Thêm hàm này vào để chuẩn hóa việc lưu tài khoản mới qua tầng DAO
    public void addNewAccount(Account account) {
        // 1. Lưu vào kho chung của hệ thống
        dataCenter.getAccountList().add(account);

        // 2. Cập nhật vào danh sách tài khoản cá nhân của khách hàng
        Customer owner = (Customer) account.getOwner();
        if (owner != null) {
            if (owner.getAccountList() == null) {
                owner.setAccountList(new java.util.ArrayList<>());
            }
            owner.getAccountList().add(account);
        }
    }

    public void updateStatus(int accountId, model.entity.enums.AccountStatus newStatus) {
        for (model.entity.Account acc : dataCenter.getAccountList()) {
            if (acc.getAccountId() == accountId) {
                acc.setAccountStatus(newStatus);
                break;
            }
        }
    }
}
