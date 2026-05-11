package model.data;

import model.entity.User;

public class UserDao {
    public User getUserByUsernameAndPassword(String username, String password) {
        for (User user : DataCenter.getInstance().getUserList()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Không tìm thấy hoặc sai thông tin
    }
}
