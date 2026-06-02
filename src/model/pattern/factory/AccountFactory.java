package model.pattern.factory;

import model.entity.Account;
import model.entity.CheckingAccount;
import model.entity.Customer;
import model.entity.BankingSystem;
import java.util.ArrayList;

public class AccountFactory {
    public static Account createCheckingAccount(int accountId, Customer owner, double initialBalance) {
        CheckingAccount account = new CheckingAccount();

        // Gắn dữ liệu cho class Account
        account.setAccountId(accountId);
        account.setOwner(owner);
        account.setBalance(initialBalance);
        account.setCreatedAt(BankingSystem.getSystemDate()); // Lấy ngày hệ thống
        account.setTransactionList(new ArrayList<>());

        // Gắn dữ liệu cho class CheckingAccount
        account.setMinBalance(BankingSystem.getMinCheckingBalance());

        return account;
    }
    // Thêm hàm này vào dưới hàm createCheckingAccount
    public static Account createSavingAccount(int accountId, Customer owner, double initialBalance, int term, double interestRate) {
        model.entity.SavingAccount account = new model.entity.SavingAccount();

        // Gắn dữ liệu cho class cha (Account)
        account.setAccountId(accountId);
        account.setOwner(owner);
        account.setBalance(initialBalance);
        account.setCreatedAt(BankingSystem.getSystemDate());


        // Gắn dữ liệu đặc thù cho class con (SavingAccount)
        account.setTerm(term);
        account.setDepositDate(BankingSystem.getSystemDate());

        // Ngày đáo hạn = Ngày gửi + số tháng kỳ hạn
        if (BankingSystem.getSystemDate() != null) {
            account.setMaturityDate(BankingSystem.getSystemDate().plusMonths(term));
        }
        account.setInterest(interestRate);

        return account;
    }
}
