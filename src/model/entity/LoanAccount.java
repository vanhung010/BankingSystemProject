package model.entity;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanAccount that = (LoanAccount) o;
        return Double.compare(pricipalAmount, that.pricipalAmount) == 0 && Double.compare(interestRate, that.interestRate) == 0 && loanTerm == that.loanTerm && Double.compare(amountPaidThisMonth, that.amountPaidThisMonth) == 0 && Double.compare(monthlyRequiredPayment, that.monthlyRequiredPayment) == 0 && Objects.equals(accountOwner, that.accountOwner) && Objects.equals(nextPaymentDate, that.nextPaymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountOwner, pricipalAmount, interestRate, nextPaymentDate, loanTerm, amountPaidThisMonth, monthlyRequiredPayment);
    }

    //kiểm tra trả đủ chưa
    public boolean checkPaid(){
        return this.amountPaidThisMonth >= this.monthlyRequiredPayment;
    }

    @Override
    public void withdraw(double amount) {
        throw new RuntimeException("Không thực hiện được");
    }

    @Override
    public void deposit(double amount) {
        throw new RuntimeException("không thực hiện");
    }
}
