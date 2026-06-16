package model.service;

import model.data.AccountDao;
import model.data.TransactionDao;
import model.entity.Account;
import model.entity.CheckingAccount;
import model.entity.Customer;
import model.entity.Transaction;
import model.entity.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDao();
    private AccountDao accountDao = new AccountDao();

    public void tranfer(double amount, int idAccontSource, int idAccountTarget, String description){
        Account accountSource = accountDao.getAccountById(idAccontSource);
        Account accountTarget = accountDao.getAccountById(idAccountTarget);

        if(accountSource == null || accountTarget == null){
            throw new RuntimeException("Tài khoản nguồn hoặc tài khoản nhận không hợp lệ!");
        }
        else if(!(accountSource instanceof CheckingAccount) || !(accountTarget instanceof CheckingAccount)){
            throw new RuntimeException("Tài khoản nhận hoặc tài khoản nguồn không phải tài khoản thanh toán!");
        }
        else if(accountSource.getBalance() < amount){
            throw new RuntimeException("Số dư không đủ mày nghèo quá!");
        }
        else {
            //trừ tiền
            accountSource.withdraw(amount);
            //cộng tiền
            accountTarget.deposit(amount);
            //tạo giao dichj
            Transaction transactionplus = new Transaction(TransactionType.TRANSFER, amount, LocalDateTime.now(), idAccountTarget, idAccontSource, description);
            Transaction transactionminus = new Transaction(TransactionType.TRANSFER, -amount, LocalDateTime.now(), idAccountTarget, idAccontSource, description);
            //lưu giao dịch
            transactionDao.addTransactionPlus(transactionplus);
            transactionDao.addTransactionPlus(transactionminus);
            //update tài khoản
            accountDao.updateBalance(idAccontSource, accountSource.getBalance());
            accountDao.updateBalance(idAccountTarget, accountTarget.getBalance());
        }
    }
}
