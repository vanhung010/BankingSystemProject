package model.entity;

import model.entity.enums.AccountStatus;
import model.pattern.observer.AccountStatusObserver;
import model.pattern.strategy.InterestStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private int accountId;
    private double balance;
    private AccountStatus accountStatus;
    private Customer owner;
    private LocalDate createdAt;
    private InterestStrategy interestStrategy;
    private List<AccountStatusObserver> observers = new ArrayList<>();

    public void addObserver(AccountStatusObserver o) { observers.add(o); }

    // Thêm mới: changeState thay thế setAccountStatus trực tiếp
    public void changeState(AccountStatus newStatus, String reason) {
        AccountStatus old = this.accountStatus;
        // Chặn CLOSED → bất kỳ (không mở lại được)
        if (old == AccountStatus.CLOSED) {
            throw new RuntimeException("Tài khoản đã đóng, không thể thay đổi trạng thái");
        }
        this.accountStatus = newStatus;
        // Notify tất cả observer
        for (AccountStatusObserver o : observers)
            o.onStatusChanged(this, old, newStatus, reason);
    }

    private List<Transaction> transactionList;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public InterestStrategy getInterestStrategy() {
        return interestStrategy;
    }

    public void setInterestStrategy(InterestStrategy interestStrategy) {
        this.interestStrategy = interestStrategy;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public abstract void withdraw(double amount);

    public abstract void deposit(double amount);
}
