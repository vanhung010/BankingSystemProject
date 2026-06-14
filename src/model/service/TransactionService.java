package model.service;

import model.data.AccountDao;
import model.data.TransactionDao;
import model.entity.Account;
import model.entity.Customer;
import model.entity.Transaction;

import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDao();
    private AccountDao accountDao = new AccountDao();

}
