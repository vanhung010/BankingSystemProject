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

    public String payLoanDebt(Customer customer, int loanAccountId, double amount) {
        // 1. Kiểm tra số tiền hợp lệ
        if (amount <= 0) {
            throw new RuntimeException("Số tiền thanh toán phải lớn hơn 0!");
        }

        // 2. Tìm tài khoản vay trực tiếp trong danh sách tài khoản của Customer truyền xuống
        LoanAccount loanAcc = null;
        if (customer.getAccountList() != null) {
            for (Account acc : customer.getAccountList()) {
                if (acc.getAccountId() == loanAccountId && acc instanceof LoanAccount) {
                    loanAcc = (LoanAccount) acc;
                    break;
                }
            }
        }

        if (loanAcc == null) {
            throw new RuntimeException("Không tìm thấy tài khoản Vay mang mã số này của bạn!");
        }

        // 3. Kiểm tra trạng thái tài khoản
        if (loanAcc.getAccountStatus() == model.entity.enums.AccountStatus.CLOSED) {
            throw new RuntimeException("Tài khoản vay này đã đóng (CLOSED) do đã tất toán trước đó!");
        }

        // 4. Số dư balance của tài khoản vay đại diện cho dư nợ hiện tại
        double currentDebt = loanAcc.getBalance();
        if (amount > currentDebt) {
            throw new RuntimeException(String.format("Số tiền nhập vào vượt quá tổng dư nợ hiện tại! Dư nợ tối đa cần trả: %,.2f VNĐ", currentDebt));
        }

        // 5. Cập nhật dữ liệu tài khoản vay
        double newBalance = currentDebt - amount;
        loanAcc.setBalance(newBalance);

        // Cập nhật số tiền lũy tiến đã trả trong tháng này
        double updatedPaidThisMonth = loanAcc.getAmountPaidThisMonth() + amount;
        loanAcc.setAmountPaidThisMonth(updatedPaidThisMonth);

        // Lưu cập nhật thông tin qua các DAO tương ứng trong hệ thống của bạn
        accountDao.updateBalance(loanAcc.getAccountId(), loanAcc.getBalance());
        loanDao.updatePaidThisMonth(loanAcc);

        // 6. Tạo lịch sử giao dịch (Khớp chính xác với Constructor 6 tham số của Transaction.java)
        Transaction transaction = new Transaction(
                TransactionType.LOAN_PAYMENT,
                amount,
                LocalDateTime.now(),
                loanAccountId,
                null, // Tham số Object ignored
                String.format("Thanh toan no khoan vay ID %d. So tien con lai: %,.2f VND", loanAccountId, newBalance)
        );
        transactionDao.addTransactionPlus(transaction);

        // 7. [XỬ LÝ MẪU OBSERVER] Nếu đã trả sạch nợ (newBalance == 0) -> Đổi trạng thái sang CLOSED để trigger Observer
        if (newBalance == 0) {
            // Đăng ký AccountStatusLogger lắng nghe sự kiện đổi trạng thái trước khi thực hiện đổi
            loanAcc.addObserver(new model.pattern.observer.AccountStatusLogger());

            // Hàm changeState kế thừa từ Account sẽ tự động kích hoạt notify đến AccountStatusLogger
            loanAcc.changeState(model.entity.enums.AccountStatus.CLOSED, "đã trả hết nợ");

            // Cập nhật trạng thái mới của tài khoản vào database
            accountDao.updateStatus(loanAcc.getAccountId(), model.entity.enums.AccountStatus.CLOSED);

            return String.format("🎉 Chúc mừng! Bạn đã tất toán thành công toàn bộ khoản vay mã số %d.", loanAccountId);
        }

        return String.format("✅ Thanh toán thành công %,.2f VNĐ vào tài khoản vay %d.\n   - Số dư nợ còn lại: %,.2f VNĐ\n   - Tổng tiền đã thanh toán trong tháng này: %,.2f VNĐ (Yêu cầu tối thiểu tháng: %,.2f VNĐ)",
                amount, loanAccountId, newBalance, updatedPaidThisMonth, loanAcc.getMonthlyRequiredPayment());
    }
}
