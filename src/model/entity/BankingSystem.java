package model.entity;

import java.time.LocalDate;

public class BankingSystem {

    private String bankName;
    private  double minCheckingBalance;
    private  LocalDate systemDate;
    private double demandInterestRate;
    private  double interestRate1M;
    private  double interestRate6M;
    private  double interestRate12M;
    private  double baseLoanInterestRate;
    private  double minSavingDeposit;

    public  double getMinCheckingBalance() {
        return minCheckingBalance;
    }

    public  void setMinCheckingBalance(double minCheckingBalance) {
        this.minCheckingBalance = minCheckingBalance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public  LocalDate getSystemDate() {
        return systemDate;
    }

    public  void setSystemDate(LocalDate systemDate) {
        this.systemDate = systemDate;
    }

    public  double getDemandInterestRate() {
        return demandInterestRate;
    }

    public  void setDemandInterestRate(double demandInterestRate) {
        this.demandInterestRate = demandInterestRate;
    }

    public double getInterestRate1M() {
        return interestRate1M;
    }

    public  void setInterestRate1M(double interestRate1M) {
        this.interestRate1M = interestRate1M;
    }

    public  double getInterestRate6M() {
        return interestRate6M;
    }

    public  void setInterestRate6M(double interestRate6M) {
        this.interestRate6M = interestRate6M;
    }

    public double getInterestRate12M() {
        return interestRate12M;
    }

    public  void setInterestRate12M(double interestRate12M) {
        this.interestRate12M = interestRate12M;
    }

    public  double getBaseLoanInterestRate() {
        return baseLoanInterestRate;
    }

    public  void setBaseLoanInterestRate(double baseLoanInterestRate) {
        this.baseLoanInterestRate = baseLoanInterestRate;
    }

    public double getMinSavingDeposit() {
        return minSavingDeposit;
    }

    public void setMinSavingDeposit(double minSavingDeposit) {
        this.minSavingDeposit = minSavingDeposit;
    }
}
