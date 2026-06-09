package model.entity;

import java.util.List;

public class Customer extends User {
    private double monthlyIncome;

    private List<Account> accountList;
    private List<LoanRequest> loanRequestList;

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
