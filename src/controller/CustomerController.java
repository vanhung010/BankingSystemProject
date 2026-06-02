package controller;

import model.entity.Customer;
import model.service.AccountService;
import model.service.SavingService;

import java.util.Scanner;

public class CustomerController {
    private final AccountService accountService;
    private final Scanner scanner;
    SavingService savingService;

    public CustomerController() {
        this.accountService = new AccountService();
        this.scanner = new Scanner(System.in);
    }

    public void handleOpenCheckingAccount(Customer customer) {
        System.out.println("MỞ TÀI KHOẢN GIAO DỊCH");

        // 1. Nhập mã tài khoản mới
        System.out.print("Nhập mã số tài khoản mới: ");
        int accountId = scanner.nextInt();

        // 2. Nhập số tiền gửi ban đầu
        System.out.print("Nhập số tiền nộp vào ban đầu (VNĐ): ");
        double initialBalance = scanner.nextDouble();

        System.out.println("------------------------------------------");
        System.out.println("Đang xử lý yêu cầu mở tài khoản...");

        // 3. kiểm tra logic và lưu trữ
        boolean isSuccess = accountService.openCheckingAccount(accountId, customer, initialBalance);

        // 4. Thông báo kết quả phản hồi
        if (isSuccess) {
            System.out.println("Hệ thống thông báo: Hoàn tất quy trình mở tài khoản.");
        } else {
            System.out.println("Hệ thống thông báo: Vui lòng thử lại với thông tin hợp lệ.");
        }

    }
    public void handleOpenSavingAccount(Customer customer) {
        System.out.println("\n========= MỞ TÀI KHOẢN TIẾT KIỆM =========");

        System.out.print("Nhập mã sổ tiết kiệm mới (số nguyên): ");
        int accountId = scanner.nextInt();

        System.out.print("Nhập số tiền gửi (VNĐ): ");
        double initialBalance = scanner.nextDouble();

        System.out.print("Nhập kỳ hạn gửi (1, 6, hoặc 12 tháng): ");
        int term = scanner.nextInt();

        System.out.println("------------------------------------------");

        savingService.openSavingAccount(accountId, customer, initialBalance, term);

        System.out.println("==========================================\n");
    }

    public void handleCloseSavingAccount(Customer customer) {
        System.out.println("\n========= TẤT TOÁN SỔ TIẾT KIỆM =========");
        System.out.print("Nhập mã sổ tiết kiệm bạn muốn tất toán: ");
        int accountId = scanner.nextInt();


        scanner.nextLine();

        System.out.println("-----------------------------------------");
        System.out.println("Đang tính toán dữ liệu lãi suất...");

        // Gọi Service (đã khai báo ở chức năng trước) để xử lý
        savingService.closeSavingAccount(customer, accountId);

        System.out.println("=========================================\n");
    }
    public void handleChangeAccountStatus(Customer customer) {
        System.out.println("\n========= THAY ĐỔI TRẠNG THÁI TÀI KHOẢN =========");
        System.out.print("Nhập mã số tài khoản cần thay đổi: ");
        int accountId = scanner.nextInt();

        System.out.println("Chọn trạng thái mới:");
        System.out.println("1. ACTIVE (Hoạt động)");
        System.out.println("2. LOCKED (Khóa tạm thời)");
        System.out.println("3. CLOSED (Đóng vĩnh viễn)");
        System.out.print("Nhập lựa chọn (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        model.entity.enums.AccountStatus newStatus;
        if (choice == 1) {
            newStatus = model.entity.enums.AccountStatus.ACTIVE;
        } else if (choice == 2) {
            newStatus = model.entity.enums.AccountStatus.LOCKED;
        } else {
            newStatus = model.entity.enums.AccountStatus.CLOSED;
        }

        System.out.println("-------------------------------------------------");
        // Gọi Service xử lý
        accountService.changeAccountStatus(customer, accountId, newStatus);
        System.out.println("=================================================\n");
    }
}
