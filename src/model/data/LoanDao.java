package model.data;

import model.entity.Account;
import model.entity.LoanAccount;
import model.entity.LoanRequest;
import model.entity.enums.LoanRequestStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanDao {

    DataCenter dataCenter = DataCenter.getInstance();

    public void addLoanRequest(LoanRequest loanRequest){
        //Tự động tăng id lên 1
        loanRequest.setLoanRequestId(dataCenter.getLoanRequestList().size()+1);
        dataCenter.getLoanRequestList().add(loanRequest);
    }

    //laays loanRequest
    public LoanRequest getLoanRequestById(int id){
        //Lấy yêu cầu vay từ id
        for(LoanRequest loanRequest : dataCenter.getLoanRequestList()){
            if(loanRequest.getLoanRequestId() == id){
                return loanRequest;
            }
        }
        return null;
    }

    //lấy danh sách những yêu cầu vay pending
    public List<LoanRequest> getAllLoanRequestPending(){
        List<LoanRequest> pendingLoanRequests = new ArrayList<>();

        for(LoanRequest loanRequest : dataCenter.getLoanRequestList()){
            if(loanRequest.getStatus() == LoanRequestStatus.PENDING){
                pendingLoanRequests.add(loanRequest);
            }
        }

        return pendingLoanRequests;
    }

    //Cập nhật trạng thái khoản vay
    public void updateStatusLoanRequest(int idLoanRequest, String status){
        LoanRequestStatus loanRequestStatus = LoanRequestStatus.valueOf(status);

        for(LoanRequest loanRequest : dataCenter.getLoanRequestList()){
            if(loanRequest.getLoanRequestId() == idLoanRequest){
                loanRequest.setStatus(loanRequestStatus);
                return;
            }
        }
    }

    public void updatePaidThisMonth(LoanAccount loanAccount) {
        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof LoanAccount && account.getAccountId() == loanAccount.getAccountId()) {
                ((LoanAccount) account).setAmountPaidThisMonth(0);
                return;
            }
        }
    }

    public void updateNextPaymentDate(LoanAccount loanAccount) {
        LocalDate systemDate = dataCenter.getBankingSystem().getSystemDate();
        LocalDate nextPaymentDate = systemDate.withDayOfMonth(1).plusDays(14);

        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof LoanAccount && account.getAccountId() == loanAccount.getAccountId()) {
                ((LoanAccount) account).setNextPaymentDate(nextPaymentDate);
                return;
            }
        }
    }

    public void updatePaidMonth(LoanAccount loanAccount) {
        double amount = (loanAccount.getPricipalAmount() / loanAccount.getLoanTerm())
                + (loanAccount.getPricipalAmount() * (loanAccount.getInterestRate() / 12));

        for (Account account : dataCenter.getAccountList()) {
            if (account instanceof LoanAccount && account.getAccountId() == loanAccount.getAccountId()) {
                ((LoanAccount) account).setMonthlyRequiredPayment(amount);
                return;
            }
        }
    }
}
