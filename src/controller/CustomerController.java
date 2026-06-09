package controller;

import model.entity.Account;
import model.entity.Customer;
import model.service.AccountService;
import model.service.LoanService;
import model.service.TimeService;
import util.ParseNumber;

import java.time.LocalDate;
import java.util.List;

public class CustomerController {
    private final AccountService accountService;
    private final Scanner scanner;

    public CustomerController() {
        this.accountService = new AccountService();
        this.scanner = new Scanner(System.in);
    }

    private LoanService loanService = new LoanService();
    private AccountService accountService = new AccountService();
    private TimeService timeService = new TimeService();


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
        return mess;
    }

    public List<Account> getAllAccountOfCustomer(Customer customer) {
        return accountService.getAllAccount(customer.getUserId());
    }

    public LocalDate getDateSystem() {
        return timeService.getSystemDate();
    }
}
        }
