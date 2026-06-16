package model.data;

import model.entity.Customer;
import model.entity.User;
import model.entity.enums.Role;

public class UserDao {

    private final DataCenter dataCenter = DataCenter.getInstance();

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
                return (Customer) user;
            }
        }
        return null;

    }

    public boolean checkEmailDuplicate(String email) {
        if (email == null) return false;
        for (User user : dataCenter.getUserList()) {
            if (email.equalsIgnoreCase(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public boolean registerUser(Customer customer) {
        if(checkEmailDuplicate(customer.getEmail())){
            throw new RuntimeException("Trùng email");
        }

        int maxId = 0;
        for (User u : dataCenter.getUserList()) {
            if (u.getUserId() > maxId) {
                maxId = u.getUserId();
            }
        }

        customer.setUserId(maxId + 1);
        customer.setRole(Role.CUSTOMER);

        return dataCenter.getUserList().add(customer);
    }
}
