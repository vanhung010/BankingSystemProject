package controller;

import model.entity.Account;
import model.entity.Customer;
import model.entity.LoanRequest;
import model.entity.enums.AccountStatus;
import model.pattern.observer.InterestObserver;
import model.pattern.observer.LoanMonthlyObserver;
import model.pattern.observer.SavingExpiryObserver;
import model.service.*;
import util.ParseNumber;

import java.time.LocalDate;
import java.util.List;

public class StaffController {

    private AccountService accountService = new AccountService();
    private LoanService loanService = new LoanService();
    private SystemService systemService = new SystemService();
    private TimeService timeService = new TimeService();
    private UserService customerSearchService = new UserService();

    public StaffController() {
        // Đăng ký Observer 1 lần khi khởi tạo
        systemService.addObserver(new SavingExpiryObserver(new SavingService()));
        systemService.addObserver(new LoanMonthlyObserver(new LoanService()));
        systemService.addObserver(new InterestObserver(new InterestService()));
    }

    public List<LoanRequest> getAllLoanRequestPending() throws RuntimeException {
        return loanService.getAllLoanRequestPending();
    }

    public LoanRequest getLoanRequestById(String idString) {
        try {
            int id = ParseNumber.parseint(idString);
            return loanService.getLoanRequestById(id);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Customer getCustomerById(String idString){
        try {
            int id = ParseNumber.parseint(idString);
            return accountService.getCustomerbyId(id);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void showAllAccountsInSystem() {
        List<model.entity.Account> allAccounts = accountService.getAllAccountsInSystem();
        if (allAccounts == null || allAccounts.isEmpty()) {
            System.out.println("Danh sách tài khoản trống.");
            return;
        }

        System.out.println("\n--- DANH SÁCH TẤT CẢ TÀI KHOẢN TRONG HỆ THỐNG ---");
        System.out.printf("%-10s | %-20s | %-15s | %-15s | %-10s%n", "ID TK", "Chủ tài khoản", "Loại TK", "Trạng thái", "Số dư/Nợ");
        System.out.println("--------------------------------------------------------------------------------------");
        for (Account acc : allAccounts) {
            String accType = "N/A";
            if (acc instanceof model.entity.CheckingAccount) accType = "Thanh toán";
            else if (acc instanceof model.entity.SavingAccount) accType = "Tiết kiệm";
            else if (acc instanceof model.entity.LoanAccount) accType = "Khoản vay";

            String ownerName = acc.getOwner() != null ? acc.getOwner().getFullName() : "N/A";

            System.out.printf("%-10d | %-20s | %-15s | %-15s | %,10.0f%n",
                    acc.getAccountId(),
                    ownerName,
                    accType,
                    acc.getAccountStatus(),
                    acc.getBalance());
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

    public void changeAccountStatus(String accountIdStr, String newStatusStr, String reason) {
        try {
            int accountId = ParseNumber.parseint(accountIdStr);
            AccountStatus newStatus = AccountStatus.valueOf(newStatusStr);
            accountService.changeAccountStatus(accountId, newStatus, reason);
            System.out.println("Thay đổi trạng thái tài khoản thành công!");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void approveLoanRequest(LoanRequest loanRequest, String idAccountString) {
        try {
            int idAccount = ParseNumber.parseint(idAccountString);
            loanService.approvedLoanRequest(loanRequest, idAccount);
            System.out.println("Phê duyệt khoản vay thành công");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rejectLoanRequest(LoanRequest loanRequest) {
        loanService.rejectLoanRequest(loanRequest);
        System.out.println("Từ chối khoản vay thành công");
    }

    public void handleUpdateTime(){

        systemService.updateDateSystemPlus1Month();
    }

    public void plusDaySystem(int days) {
        systemService.updateDateSystemPlusDays(days);
    }

    public void minusDaySystem(int days) {
        systemService.updateDateSystemMinusDays(days);
    }

    public LocalDate getSystemTime() {
         return timeService.getSystemDate();
     }

     // --- Customer Search Methods ---
     public String searchCustomerById(int customerId) {
         return customerSearchService.searchCustomerById(customerId);
     }

     public String searchCustomerByUsername(String username) {
         return customerSearchService.searchCustomerByUsername(username);
     }

     public String searchCustomerByEmail(String email) {
         return customerSearchService.searchCustomerByEmail(email);
     }

     public String searchCustomerByFullName(String fullName) {
         return customerSearchService.searchCustomerByFullName(fullName);
     }

     public String viewAllCustomers() {
         return customerSearchService.viewAllCustomers();
     }

      // --- Bank / System Methods ---
      public String viewBankConfig() {
          return systemService.viewBankConfig();
      }

      public String viewInterestRates() {
          return systemService.viewInterestRates();
      }

      public String viewAccountRequirements() {
          return systemService.viewAccountRequirements();
      }

      public String viewSystemDate() {
          return systemService.viewSystemDate();
      }

    public String updateConfigValue(String thongSo, String valueString){
        try {
            systemService.updateConfigValue(thongSo, valueString);
            return "Cập nhật thông số thành công";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
