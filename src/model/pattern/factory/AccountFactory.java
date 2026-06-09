package model.pattern.factory;

import jdk.jfr.DataAmount;
import model.data.DataCenter;
import model.entity.BankingSystem;
import model.entity.Customer;
import model.entity.LoanAccount;
import model.entity.enums.AccountStatus;
import model.pattern.strategy.LoanInterestStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccountFactory {
    private static DataCenter dataCenter = DataCenter.getInstance();


    public static LoanAccount createLoanAccount(Customer customer,double balance, int loanTerm){
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

        return loanAccount;
    }
}
