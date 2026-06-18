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

    public void depositCheckingAccount(int idCustomer, int idAccount, double amount) {
        Account account = accountDao.getAccountById(idAccount);
        List<Account> listAccountOfCustomer = accountDao.getAllAccountOfCustomerDao(idCustomer);
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản thanh toán!");
        }

        else if (!(account instanceof CheckingAccount)) {
            throw new RuntimeException("tài khoản không phải tài khoản thanh toán!");
        } else if (!listAccountOfCustomer.contains(account)) {
            throw new RuntimeException("Tài khoản đã chọn không có trong danh sách tài khoản của khách hàng!");

        }
        account.getAccountStatus().handle();
        //ép kiểu xuống
        CheckingAccount checkingAccount = (CheckingAccount) account;
        //thực hiện cộng tiền
        checkingAccount.deposit(amount);
        //lưu giao dịch
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, LocalDateTime.now(), idAccount, null, "Nạp tiền");
        transactionDao.addTransactionPlus(transaction);

        accountDao.updateBalance(account.getAccountId(), account.getBalance());
    }

    public void withdrawCheckingAccount(int idCustomer, int idAccount, double amount) {
        Account account = accountDao.getAccountById(idAccount);
        List<Account> listAccountOfCustomer = accountDao.getAllAccountOfCustomerDao(idCustomer);
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản thanh toán!");
        }
        else if (!(account instanceof CheckingAccount)) {
            throw new RuntimeException("tài khoản không phải tài khoản thanh toán!");
        } else if (!listAccountOfCustomer.contains(account)) {
            throw new RuntimeException("Tài khoản đã chọn không có trong danh sách tài khoản của khách hàng!");
        }
        account.getAccountStatus().handle();
        //ép kiểu xuống
        CheckingAccount checkingAccount = (CheckingAccount) account;
        checkingAccount.withdraw(amount);
        //lưu giao dịch
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, -amount, LocalDateTime.now(), idAccount, null, "Rút tiền");
        transactionDao.addTransactionPlus(transaction);

        accountDao.updateBalance(account.getAccountId(), account.getBalance());
    }
}
