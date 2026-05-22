package controller;

import model.entity.Customer;
import model.entity.LoanRequest;
import model.service.AccountService;
import model.service.LoanService;
import util.ParseNumber;

public class StaffController {

    private AccountService accountService = new AccountService();
    private LoanService loanService = new LoanService();


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
}

