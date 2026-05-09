package model.entity;

import model.entity.enums.AccountStatus;
import model.pattern.strategy.InterestStrategy;

import java.time.LocalDate;

public abstract class Account {
    private int accountId;
    private double balance;
    private AccountStatus accountStatus;
    private Customer owner;
    private LocalDate createdAt;
    private InterestStrategy interestStrategy;
}
