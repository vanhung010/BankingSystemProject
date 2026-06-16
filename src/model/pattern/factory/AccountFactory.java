package model.pattern.factory;

import model.data.DataCenter;
import model.entity.*;
import model.entity.enums.AccountStatus;
import model.pattern.observer.AccountStatusLogger;
import model.pattern.strategy.LoanInterestStrategy;
import model.pattern.strategy.TermInterestStrategy;

import java.time.LocalDate;


public class AccountFactory {
    private static DataCenter dataCenter = DataCenter.getInstance();


    public static LoanAccount createLoanAccount(Customer customer, double balance, int loanTerm) {
        LoanAccount loanAccount = new LoanAccount();

        loanAccount.setAccountId(dataCenter.getAccountList().size() + 1);
        loanAccount.setBalance(balance);
        loanAccount.setAccountStatus(AccountStatus.ACTIVE);
        loanAccount.setOwner(customer);
        loanAccount.setCreatedAt(LocalDate.now());
        loanAccount.setPricipalAmount(balance);
        loanAccount.setInterestRate(dataCenter.getBankingSystem().getBaseLoanInterestRate());
        loanAccount.setNextPaymentDate(LocalDate.now().plusMonths(1));
        loanAccount.setLoanTerm(loanTerm);
        loanAccount.setAmountPaidThisMonth(0);
        loanAccount.setInterestStrategy(new LoanInterestStrategy());
        loanAccount.setMonthlyRequiredPayment(balance / loanTerm);

        loanAccount.addObserver(new AccountStatusLogger());

        return loanAccount;
    }

    // Tạo tài khoản thanh toán (CheckingAccount)
    public static CheckingAccount createCheckingAccount(Customer customer, double initialBalance) {
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountId(dataCenter.getAccountList().size() + 1);
        checkingAccount.setBalance(initialBalance);
        checkingAccount.setAccountStatus(AccountStatus.ACTIVE);
        checkingAccount.setOwner(customer);
        checkingAccount.setCreatedAt(LocalDate.now());

        // Lấy số dư tối thiểu từ ngân hàng
        if (dataCenter.getBankingSystem() != null) {
            checkingAccount.setMinBalance(dataCenter.getBankingSystem().getMinCheckingBalance());
        }

        return checkingAccount;
    }

    // Tạo sổ tiết kiệm (SavingAccount)
    public static SavingAccount createSavingAccount(Customer customer, double initialBalance, int term) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setAccountId(dataCenter.getAccountList().size() + 1);
        savingAccount.setBalance(initialBalance);
        savingAccount.setAccountStatus(AccountStatus.ACTIVE);
        savingAccount.setOwner(customer);
        savingAccount.setCreatedAt(LocalDate.now());

        // Thiết lập các kì hạnh
        savingAccount.setTerm(term);
        savingAccount.setDepositDate(LocalDate.now());
        savingAccount.setMaturityDate(LocalDate.now().plusMonths(term));
        //set chiên lược
        savingAccount.setInterestStrategy(new TermInterestStrategy());
        //set lãi
        if (dataCenter.getBankingSystem() != null) {
            if (term == 1) {
                savingAccount.setInterest(dataCenter.getBankingSystem().getInterestRate1M());
            } else if (term == 6) {
                savingAccount.setInterest(dataCenter.getBankingSystem().getInterestRate6M());
            } else if (term == 12) {
                savingAccount.setInterest(dataCenter.getBankingSystem().getInterestRate12M());
            }

        }
        return savingAccount;
    }
}
