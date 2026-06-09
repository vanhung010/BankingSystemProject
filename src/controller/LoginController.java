package controller;

import model.entity.Customer;
import model.entity.Staff;
import model.entity.User;
import model.service.AuthService;
import view.CustomerView;
import view.StaffView;

public class LoginController {
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    public User login(String username, String password) {
        return authService.login(username, password);
    }
    public void handleLogin(String username, String password) {
        User user = authService.login(username, password);

        if (user != null) {
            System.out.println("Đăng nhập thành công! Xin chào, " + user.getFullName());


            if (user instanceof Customer) {
                new CustomerView((Customer) user).run();
            } else if (user instanceof Staff) {

                new StaffView((Staff) user).run();
            }


        } else {
            System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }
}
