package model.entity;

import java.time.LocalDate;

public class SavingAccount extends Account {
    private Customer customerOwner;
    private int term;
    private LocalDate depositDate;
    private LocalDate maturityDate;
    private double interest; //% lãi mỗi năm

    public Customer getCustomerOwner() {
        return customerOwner;
    }

    public void setCustomerOwner(Customer customerOwner) {
        this.customerOwner = customerOwner;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}