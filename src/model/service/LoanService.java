package model.service;

import model.data.AccountDao;
import model.data.LoanDao;
import model.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public class LoanService {

    LoanDao loanDao = new LoanDao();
    private AccountDao accountDao = new AccountDao();


    public void addLoanRequest(Customer customerOwner, double requestAmount, int term){
        //kiểm tra khoản vay, Hệ số 10
        if(requestAmount > customerOwner.getMonthlyIncome() * 10){
            throw new RuntimeException("Lỗi thu nhập không đạt yêu cầu vay");
        }
        LoanRequest loanRequest = new LoanRequest();

        loanRequest.setCustomerOwner(customerOwner);
        loanRequest.setRequestAmount(requestAmount);
        loanRequest.setLoanTerm(term);

        loanDao.addLoanRequest(loanRequest);

    }
    public LoanRequest getLoanRequestById(int id) {
        return loanDao.getLoanRequestById(id);
    }
    //Đồng ý khoản vay
    public void approvedLoanRequest(LoanRequest loanRequest, int idAccountReceived){


        Account account = accountDao.getAccountById(idAccountReceived);
        List<Account> listAccountOfCustomer = accountDao.getAllAccountOfCustomerDao(loanRequest.getCustomerOwner().getUserId());
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản thanh toán!");
        }

        else if (!(account instanceof CheckingAccount)) {
            throw new RuntimeException("tài khoản không phải tài khoản thanh toán!");
        } else if (!listAccountOfCustomer.contains(account)) {
            throw new RuntimeException("Tài khoản đã chọn không có trong danh sách tài khoản của khách hàng!");

        }
        //cập nhjat trạng thái
        loanDao.updateStatusLoanRequest(loanRequest.getLoanRequestId(), "APPROVED");
        //mở tài khoản
        accountDao.addLoanAccount(loanRequest.getCustomerOwner().getUserId(), loanRequest.getRequestAmount(), loanRequest.getLoanTerm());
        account.getAccountStatus().handle();
        //ép kiểu xuống
        CheckingAccount checkingAccount = (CheckingAccount) account;
        //thực hiện cộng tiền
        checkingAccount.deposit(loanRequest.getRequestAmount());
        //lưu giao dịch
        Transaction transaction = new Transaction(TransactionType.LOAN_DISBURSEMENT, loanRequest.getRequestAmount(), LocalDateTime.now(), idAccountReceived, null, "Nhận tiền từ tài khoản vay");
        transactionDao.addTransactionPlus(transaction);

        accountDao.updateBalance(account.getAccountId(), account.getBalance());
    }
}
