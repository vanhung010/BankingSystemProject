package view;

import model.entity.Customer;
import model.entity.Staff;
import model.entity.User;
import controller.LoginController;

import java.util.Scanner;

public class LoginView {
    Scanner scanner = new Scanner(System.in);
    LoginController loginController = new LoginController();

    public void display() {
        while (true) {
            System.out.println("\n===BANK ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                  promptLogin();
                    break;
                case "2":
//                    handleRegister();
                    break;
                case "0":
                    System.out.println("Cảm ơn bạn đã sử dụng dịch vụ!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại!");
            }
        }
    }


    private void promptLogin() {
        System.out.print("Nhập Username: ");
        String username = scanner.nextLine();
        System.out.print("Nhập Password: ");
        String password = scanner.nextLine();


        loginController.handleLogin(username, password);
    }

}
