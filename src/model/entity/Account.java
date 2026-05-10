package model.entity;

import model.entity.enums.AccountStatus;
import model.pattern.strategy.InterestStrategy;

import java.time.LocalDate;
import java.util.List;

public abstract class Account {
    private int accountId;
    private double balance;
    private AccountStatus accountStatus;
    private Customer owner;
    private LocalDate createdAt;
    private InterestStrategy interestStrategy;

    private List<Transaction> transactionList;
}
