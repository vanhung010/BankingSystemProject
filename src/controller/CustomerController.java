package controller;

import model.entity.Account;
import model.entity.Customer;
import model.service.AccountService;
import model.service.LoanService;
import model.service.TimeService;
import model.service.TransactionService;
import util.ParseNumber;
import java.util.Scanner;

import java.time.LocalDate;
import java.util.List;

public class CustomerController {

    private LoanService loanService = new LoanService();
    private AccountService accountService = new AccountService();
    private TimeService timeService = new TimeService();
    private Scanner scanner = new Scanner(System.in);
    private TransactionService transactionService = new TransactionService();



    public String addLoanRequest(Customer customer, String amountString, String termString) {
        String mess = "Thông báo: Yêu cầu tạo khoản vay thành công! Vui lòng chờ nhân viên giải quyết!";
        double amount = 0;
        int term = 0;
        try {
            amount = ParseNumber.parseDouble(amountString);
            term = ParseNumber.parseint(termString);
        } catch (RuntimeException e) {
            mess = e.getMessage();
            return mess;
        }

        try {
            loanService.addLoanRequest(customer, amount, term);
        } catch (RuntimeException e) {
            mess = e.getMessage();
            return mess;
        }
        return mess;
    }

    public List<Account> getAllAccountOfCustomer(Customer customer) {
        return accountService.getAllAccount(customer.getUserId());
    }

    public LocalDate getDateSystem() {
        return timeService.getSystemDate();
    }


    public void handleOpenCheckingAccount(Customer customer) {
        System.out.println("\n--- MỞ TÀI KHOẢN THANH TOÁN ---");
        System.out.print("Nhập số tiền nạp ban đầu (VNĐ): ");
        double amount = Double.parseDouble(scanner.nextLine());

        String result = accountService.openCheckingAccount(customer, amount);
        System.out.println(result);
    }

    public void handleOpenSavingAccount(Customer customer) {
        System.out.println("\n--- MỞ SỔ TIẾT KIỆM ---");
        System.out.print("Nhập Mã số tài khoản thanh toán: ");
        int checkingAccId = Integer.parseInt(scanner.nextLine());

        System.out.print("Nhập số tiền muốn gửi tiết kiệm: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Nhập kỳ hạn gửi: ");
        int term = Integer.parseInt(scanner.nextLine());

        String result = accountService.openSavingAccount(customer, checkingAccId, amount, term);
        System.out.println(result);
    }

    public void handleViewTransactionHistory(Customer customer) {
        System.out.println("\n--- XEM LỊCH SỬ GIAO DỊCH ---");
        System.out.print("Nhập mã số tài khoản cần xem sao kê: ");

        java.util.Scanner inputScanner = new java.util.Scanner(System.in);
        try {
            int accountId = Integer.parseInt(inputScanner.nextLine());
            transactionService.viewTransactionHistory(customer, accountId);
        } catch (NumberFormatException e) {
            System.out.println("=> Lỗi: Mã tài khoản phải là một số hợp lệ!");
        }
    }
}
