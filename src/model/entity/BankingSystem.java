package model.entity;

import java.time.LocalDate;

public class BankingSystem {

    private static double minCheckingBalance;
    private static LocalDate systemDate;
    private static double demandInterestRate;
    private static double interestRate1M;
    private static double interestRate6M;
    private static double interestRate12M;
    private static double baseLoanInterestRate;
    private static double minSavingDeposit;

    public static double getMinCheckingBalance() {
        return minCheckingBalance;
    }

    public static void setMinCheckingBalance(double minCheckingBalance) {
        BankingSystem.minCheckingBalance = minCheckingBalance;
    }

    public static LocalDate getSystemDate() {
        return systemDate;
    }

    public static void setSystemDate(LocalDate systemDate) {
        BankingSystem.systemDate = systemDate;
    }

    public static double getDemandInterestRate() {
        return demandInterestRate;
    }

    public static void setDemandInterestRate(double demandInterestRate) {
        BankingSystem.demandInterestRate = demandInterestRate;
    }

    public static double getInterestRate1M() {
        return interestRate1M;
    }

    public static void setInterestRate1M(double interestRate1M) {
        BankingSystem.interestRate1M = interestRate1M;
    }

    public static double getInterestRate6M() {
        return interestRate6M;
    }

    public static void setInterestRate6M(double interestRate6M) {
        BankingSystem.interestRate6M = interestRate6M;
    }

    public static double getInterestRate12M() {
        return interestRate12M;
    }

    public static void setInterestRate12M(double interestRate12M) {
        BankingSystem.interestRate12M = interestRate12M;
    }

    public static double getBaseLoanInterestRate() {
        return baseLoanInterestRate;
    }

    public static void setBaseLoanInterestRate(double baseLoanInterestRate) {
        BankingSystem.baseLoanInterestRate = baseLoanInterestRate;
    }

    public static double getMinSavingDeposit() {
        return minSavingDeposit;
    }

    public static void setMinSavingDeposit(double minSavingDeposit) {
        BankingSystem.minSavingDeposit = minSavingDeposit;
    }
}
