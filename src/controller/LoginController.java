package controller;

import model.entity.User;
import model.service.AuthService;

public class LoginController {
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    public User login(String username, String password) {
        return authService.login(username, password);
    }
}
