package model.data;

import model.entity.*;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {
    private static DataCenter instance;
    private BankingSystem bankingSystem;
    private List<Account> accountList;
    private List<User> userList;
    private List<LoanRequest> loanRequestList;
    private List<Transaction> transactionList;


    private DataCenter() {
        accountList = new ArrayList<>();
        userList = new ArrayList<>();
        loanRequestList = new ArrayList<>();
        transactionList = new ArrayList<>();
        bankingSystem = new BankingSystem();
    }
    public static DataCenter getInstance(){
        if(instance == null){
            instance = new DataCenter();
        }
        return instance;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<LoanRequest> getLoanRequestList() {
        return loanRequestList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public BankingSystem getBankingSystem() {
        return bankingSystem;
    }

    public static void testMethod(){}
}
