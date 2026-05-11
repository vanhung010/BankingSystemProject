package model.service;

import model.data.UserDao;
import model.entity.User;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String username, String password) {
        // Thực hiện các logic xác thực, kiểm tra chuỗi rỗng...
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDao.getUserByUsernameAndPassword(username, password);
    }
}
