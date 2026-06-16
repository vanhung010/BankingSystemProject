package controller;

import model.entity.Customer;
import model.entity.LoanRequest;
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
}
