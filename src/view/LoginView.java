package view;

import model.entity.Customer;
import model.entity.Staff;
import model.entity.User;

import java.util.Scanner;

public class LoginView {
    Scanner scanner = new Scanner(System.in);
    public void display() {
        while (true) {
            System.out.println("\n=== HLK BANK ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
//                    User user = handleLogin();
//                    if (user instanceof Customer) {
//                        new CustomerView((Customer) user).run();
//                    } else if (user instanceof Staff) {
//                        new StaffView((Staff) user).run();
//                    } else {
//                        break;
//                    }
//                    break;
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

}
