package model.service;

import model.data.AccountDao;
import model.data.LoanDao;
import model.data.SystemDao;
import model.data.TransactionDao;
import model.entity.*;
import model.entity.enums.LoanRequestStatus;
import model.entity.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class LoanService {

    LoanDao loanDao = new LoanDao();
    private AccountDao accountDao = new AccountDao();
    private TransactionDao transactionDao = new TransactionDao();
    private SystemDao systemDao = new SystemDao();


    public void addLoanRequest(Customer customer, double amount, int term) {
        //kiểm tra khoản vay, Hệ số 10
        if(amount > customer.getMonthlyIncome() * 10){
            throw new RuntimeException("Lỗi thu nhập không đạt yêu cầu vay");
        }
        //Kiểm tra kỳ hạn
        if(term != 1 && term != 6 && term != 12 ){
            throw new RuntimeException("Kì hạn vay không hợp lệ");
        }
        if(amount <= 0 ){
            throw new RuntimeException("Số tiền vay không được là số âm");
        }
        LoanRequest loanRequest = new LoanRequest();

        loanRequest.setCustomerOwner(customer);
        loanRequest.setRequestAmount(amount);
        loanRequest.setLoanTerm(term);
        loanRequest.setStatus(LoanRequestStatus.PENDING);
        loanRequest.setRequestDate(LocalDateTime.now());


        loanDao.addLoanRequest(loanRequest);
    }

    //lấy danh sách tất cả tài khoản vay đang chờ phê duyệt
    public List<LoanRequest> getAllLoanRequestPending() throws RuntimeException{
        List<LoanRequest> loanRequestList = loanDao.getAllLoanRequestPending();
        if(loanRequestList.isEmpty()){
            throw new RuntimeException("Danh sách đang trống");
        }
        return loanRequestList;
    }

    public LoanRequest getLoanRequestById(int id) {
        return loanDao.getLoanRequestById(id);
    }

    //Đồng ý khoản vay
    public void approvedLoanRequest(LoanRequest loanRequest, int idAccountReceived){

        // 1. Request phải tồn tại
        if (loanRequest == null) {
            throw new RuntimeException("Không tìm thấy yêu cầu vay!");
        }

        // 2. Request chỉ được approve khi đang PENDING
        if (loanRequest.getStatus() != LoanRequestStatus.PENDING) {
            throw new RuntimeException("Yêu cầu vay đã được xử lý, không thể phê duyệt lại!");
        }

        // 3. Customer của request phải tồn tại
        Customer owner = loanRequest.getCustomerOwner();
        if (owner == null) {
            throw new RuntimeException("Yêu cầu vay không có thông tin khách hàng!");
        }

        // 4. Dữ liệu khoản vay phải còn hợp lệ tại thời điểm approve
        if (loanRequest.getRequestAmount() <= 0) {
            throw new RuntimeException("Số tiền vay không hợp lệ!");
        }

        int term = loanRequest.getLoanTerm();
        if (term != 1 && term != 6 && term != 12) {
            throw new RuntimeException("Kỳ hạn vay không hợp lệ!");
        }

        // 5. Account nhận tiền phải tồn tại
        Account account = accountDao.getAccountById(idAccountReceived);
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản thanh toán!");
        }

        // 6. Account nhận tiền phải là CheckingAccount
        if (!(account instanceof CheckingAccount)) {
            throw new RuntimeException("Tài khoản nhận tiền không phải tài khoản thanh toán!");
        }

        // 7. Account phải thuộc đúng khách hàng của loan request
        if (account.getOwner() == null ||
                account.getOwner().getUserId() != owner.getUserId()) {
            throw new RuntimeException("Tài khoản đã chọn không thuộc khách hàng vay!");
        }



        List<Account> listAccountOfCustomer = accountDao.getAllAccountOfCustomerDao(owner.getUserId());

        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản thanh toán!");
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

    public void rejectLoanRequest(LoanRequest loanRequest){
        if(loanRequest == null){
            throw new RuntimeException("Không tìm thấy yêu cầu vay");
        }
        if(loanRequest.getStatus() != LoanRequestStatus.PENDING){
            throw new RuntimeException("Lỗi nghiệp vụ");
        }
        loanDao.updateStatusLoanRequest(loanRequest.getLoanRequestId(), "REJECTED");
    }

    //kiểm tra xem đã trả đủ hàng tháng chưa
    public void lockLoanAccountMonthly(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount: loanAccountList){
            //Nếu chưa trả đủ
            if(!loanAccount.checkPaid()){
                accountDao.lockAccount(loanAccount);
            }
        }
    }

    public void checkLockLoanAccount(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount : loanAccountList){
            try{
                loanAccount.getAccountStatus().handle();
            } catch (RuntimeException e) {
                System.out.println("Tài khoản của bạn do không thanh toán đủ tháng trước nên đã bị khóa, vui lòng liên hệ nhân viên để thực hiện mở khóa");
                System.out.println(e.getMessage());
            }
        }
    }


    //tự động cập nhật tiền phải trả của vay mỗi tháng
    public void autoUpdateMonthlyRequiredPayment(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount : loanAccountList){
            //thực hiện cập nhật số tiền
            accountDao.updateMonlyRequiredPayment(loanAccount);
        }
    }

    //cập nhật số tiền đã trả hàng tháng của mỗi tài khoản về 0
    public void updateAmountPaidMonthly(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount : loanAccountList){
            //cập nhật về 0
            loanDao.updatePaidThisMonth(loanAccount);
        }
    }

    //cập nhật ngày trả nợ mỗi tháng
    public void updateDatePaidMonthly(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for (LoanAccount loanAccount : loanAccountList){
            loanDao.updateNextPaymentDate(loanAccount);
        }
    }

    //cập nhật số tiền phải trả của mỗi tháng
    public void updateAmountMustPaidMonthly(){
        List<LoanAccount> loanAccountList = accountDao.getAllLoanAccountActive();
        for(LoanAccount loanAccount : loanAccountList){
            loanDao.updatePaidMonth(loanAccount);
        }

    }
}
