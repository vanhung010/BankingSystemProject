package model.data;

import model.entity.Customer;
import model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataCenter dataCenter = DataCenter.getInstance();

    public User getUserByUsernameAndPassword(String username, String password) {
        for (User user : DataCenter.getInstance().getUserList()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Không tìm thấy hoặc sai thông tin
    }
    //Lấy khách hàng từ id
    public Customer getCustomerById(int id){

        for(User user : dataCenter.getUserList()){
            if(user.getUserId() == id){
                Customer customer = (Customer) user;
                return customer;
            }
        }
        return null;

    }
}
