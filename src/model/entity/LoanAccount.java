package model.entity;

import java.time.LocalDate;

public class LoanAccount extends Account{

    private Account accountOwner;
    private double pricipalAmount;
    private double interestRate;
    private LocalDate nextPaymentDate;
    private int loanTerm;
    private double amountPaidThisMonth; //số tiền đã trả trong tháng
    private double monthlyRequiredPayment; //số tiền phải trả tối thiểu trong tháng

    public Account getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(Account accountOwner) {
        this.accountOwner = accountOwner;
    }

    public double getPricipalAmount() {
        return pricipalAmount;
    }

    public void setPricipalAmount(double pricipalAmount) {
        this.pricipalAmount = pricipalAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public double getAmountPaidThisMonth() {
        return amountPaidThisMonth;
    }

    public void setAmountPaidThisMonth(double amountPaidThisMonth) {
        this.amountPaidThisMonth = amountPaidThisMonth;
    }

    public double getMonthlyRequiredPayment() {
        return monthlyRequiredPayment;
    }

    public void setMonthlyRequiredPayment(double monthlyRequiredPayment) {
        this.monthlyRequiredPayment = monthlyRequiredPayment;
    }
}
