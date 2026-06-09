package model.service;

import model.data.AccountDao;
import model.data.SystemDao;
import model.data.TransactionDao;
import model.entity.LoanAccount;
import model.entity.Transaction;
import model.entity.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class InterestService {

    private AccountDao accountDao = new AccountDao();
    private SystemDao systemDao = new SystemDao();
    private TransactionDao transactionDao = new TransactionDao();

    public void autoUpdateInterestLoanMonthly(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount: loanAccountList){
            double interestLoan = systemDao.getInterestLoan();
            //số tiên lãi phải trả trong tháng của tài khoản vay
            double interestLoanAccountInMonth = loanAccount.getInterestStrategy().calcInterest(loanAccount.getBalance(), interestLoan, loanAccount.getLoanTerm());
            //số tiền nợ ban đầu
            double balanceBefore = loanAccount.getBalance();
            //thực hiện cộng tiền
            loanAccount.setBalance(balanceBefore + interestLoanAccountInMonth);
            //cập nật số dư
            accountDao.updateBalance(loanAccount.getAccountId(), loanAccount.getBalance());
            //lưu giao dịch
            Transaction transaction = new Transaction(TransactionType.INTEREST_PAYMENT, interestLoanAccountInMonth, LocalDateTime.now(), loanAccount.getAccountId(), null, "Cộng tiền lãi hàng tháng");
            transactionDao.addTransactionPlus(transaction);
        }

    }


}
