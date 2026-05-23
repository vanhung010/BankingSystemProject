package model.pattern.factory;

import model.entity.Account;
import model.entity.CheckingAccount;
import model.entity.Customer;
import model.entity.BankingSystem;
import java.util.ArrayList;

public class AccountFactory {
    public static Account createCheckingAccount(int accountId, Customer owner, double initialBalance) {
        CheckingAccount account = new CheckingAccount();

        // Gắn dữ liệu cho class cha (Account)
        account.setAccountId(accountId);
        account.setOwner(owner);
        account.setBalance(initialBalance);
        account.setCreatedAt(BankingSystem.getSystemDate()); // Lấy ngày hệ thống
        account.setTransactionList(new ArrayList<>());

        // Gắn dữ liệu cho class con (CheckingAccount)
        account.setMinBalance(BankingSystem.getMinCheckingBalance());

        return account;
    }
}
