package controller;

import model.entity.Customer;
import model.service.AccountService;
import java.util.Scanner;

public class CustomerController {
    private final AccountService accountService;
    private final Scanner scanner;

    public CustomerController() {
        this.accountService = new AccountService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Hàm điều khiển chức năng mở tài khoản giao dịch
     * @param customer Khách hàng hiện tại đang đăng nhập hệ thống
     */
    public void handleOpenCheckingAccount(Customer customer) {
        System.out.println("\n========= MỞ TÀI KHOẢN GIAO DỊCH =========");

        // 1. Nhập mã tài khoản mới
        System.out.print("Nhập mã số tài khoản mới (số nguyên): ");
        int accountId = scanner.nextInt();

        // 2. Nhập số tiền gửi ban đầu
        System.out.print("Nhập số tiền nộp vào ban đầu (VNĐ): ");
        double initialBalance = scanner.nextDouble();

        System.out.println("------------------------------------------");
        System.out.println("Đang xử lý yêu cầu mở tài khoản...");

        // 3. Gọi sang tầng Service để kiểm tra logic và lưu trữ
        boolean isSuccess = accountService.openCheckingAccount(accountId, customer, initialBalance);

        // 4. Thông báo kết quả phản hồi giao diện
        if (isSuccess) {
            System.out.println("Hệ thống thông báo: Hoàn tất quy trình mở tài khoản.");
        } else {
            System.out.println("Hệ thống thông báo: Vui lòng thử lại với thông tin hợp lệ.");
        }
        System.out.println("==========================================\n");
    }
}
