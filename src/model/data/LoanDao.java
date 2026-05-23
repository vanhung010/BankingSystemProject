package model.data;

import model.entity.LoanRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoanDao {

    DataCenter dataCenter = DataCenter.getInstance();

    public void addLoanRequest(LoanRequest loanRequest){
//        String query = "INSERT INTO loan_request (user_id, request_amount, status, request_date, loan_term) " +
//                "VALUES (?, ?, ?, ?, ?)";
//        try(Connection connection = DBConnect.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(query)){
//            //set
//            preparedStatement.setInt(1, loanRequest.getCustomerOwner().getUserId());
//            preparedStatement.setDouble(2, loanRequest.getRequestAmount());
//            preparedStatement.setString(3, loanRequest.getStatus().name());
//            preparedStatement.setObject(4, loanRequest.getRequestDate());
//            preparedStatement.setInt(5, loanRequest.getLoanTerm());
//
//            preparedStatement.executeUpdate();
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//        }
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
}
