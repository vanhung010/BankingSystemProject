package model.service;

import model.data.UserDao;
import model.entity.Customer;
import model.entity.Staff;
import model.entity.User;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String username, String password) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDao.getUserByUsernameAndPassword(username, password);
    }


    public boolean registerCustomer(String username, String password, String fullName, String email, double monthlyIncome){
        Customer user = new Customer();

        user.setUserName(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMonthlyIncome(monthlyIncome);

        return userDao.registerUser(user);
    }
}
