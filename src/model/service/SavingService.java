package model.service;

import model.data.AccountDao;
import model.data.SystemDao;
import model.data.TransactionDao;
import model.entity.SavingAccount;
import model.entity.Transaction;
import model.entity.enums.TransactionType;

import java.time.LocalTime;
import java.util.List;

public class SavingService {

    private AccountDao accountDao = new AccountDao();
    private SystemDao systemDao = new SystemDao();
    private TransactionDao transactionDao = new TransactionDao();

    public void checkSavingAccountExpried(){
        List<SavingAccount> savingAccountList = accountDao.getAllSavingAccountActive();
        for(SavingAccount savingAccount : savingAccountList){
            //Nếu đã lố ngày tất toán thực hiện gia hạn
            if(savingAccount.getMaturityDate().isBefore(systemDao.getTimeSystem())){
                //tạo lịch sử giao dịch
                //số tiền lãi cộng vào
                double amount = savingAccount.getInterestStrategy().calcInterest(savingAccount.getBalance(), savingAccount.getInterest(), savingAccount.getTerm());
                Transaction transaction = new Transaction(TransactionType.INTEREST_PAYMENT, amount, systemDao.getTimeSystem().atTime(LocalTime.now()), savingAccount.getAccountId(), null, "Cộng tiền lãi");
                transactionDao.addTransactionPlus(transaction);

                savingAccount.savingExtension();//thực hiện gia hạn, cập nhật số tiền, cập nhật ngày tháng
                //cập nhật soos dư
                accountDao.updateBalance(savingAccount.getAccountId(), savingAccount.getBalance());
                //cập nhật ngày tháng
                accountDao.updateDateSavingAccount(savingAccount);
            }
        }
    }



}
