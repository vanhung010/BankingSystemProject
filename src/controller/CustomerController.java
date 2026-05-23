package controller;

import model.entity.Account;
import model.entity.Customer;
import model.service.AccountService;
import model.service.LoanService;
import util.ParseNumber;

import java.util.List;

public class CustomerController {

    private LoanService loanService = new LoanService();
    private AccountService accountService = new AccountService();


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
}
