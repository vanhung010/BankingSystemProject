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
            return new DataCenter();
        }
        else {
            return instance;
        }
    }

    public static void testMethod(){}
}
