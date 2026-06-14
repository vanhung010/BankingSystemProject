package view;

import controller.CustomerController;
import model.entity.*;
import model.entity.enums.AccountStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerView {
    Customer customer;

    CustomerController customerController = new CustomerController();
    private StaffView staffView = new StaffView();
    Scanner scanner = new Scanner(System.in);

    public CustomerView(Customer customer) {
        this.customer = customer;

    }

    public void run() {
        while (true) {
            LocalDate currentDate = customerController.getDateSystem();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);

            System.out.println("===================================================");
            System.out.printf("Xin chào, %s | Role: CUSTOMER %n", customer.getUserName());
            System.out.printf("Ngày hệ thống: %s %n", formattedDate);
            System.out.println("===================================================");
            System.out.println("--- QUẢN LÝ TÀI KHOẢN ---");
            System.out.println("1. Xem thông tin tài khoản & Số dư");
            System.out.println("2. Mở tài khoản Giao dịch (Checkings)");
            System.out.println("3. Mở tài khoản Tiết kiệm (Savings)");
            System.out.println("4. Yêu cầu tạo khoản Vay (Loan)");
            System.out.println("--- GIAO DỊCH ---");
            System.out.println("5. Nạp tiền (Deposit)");
            System.out.println("6. Rút tiền (Withdraw)");
            System.out.println("7. Chuyển khoản (Transfer)");
            System.out.println("8. Thanh toán nợ khoản vay");
            System.out.println("9. Tất toán sổ tiết kiệm");
            System.out.println("10. Xem lịch sử giao dịch (Transaction History)");
            System.out.println("11. Tra cứu chi tiết thông tin tài khoản");
            System.out.println("0. Đăng xuất");
            System.out.println("---------------------------------------------------");
            System.out.println("Nhập lựa chọn của bạn: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    getAllAccount(customer.getUserId());
                    break;
                case "2":
                    handleOpenCheckingAccount(); //BẢO
                    break;
                case "3":
                    handleOpenSavingAccount();   //Bảo
                    break;
                case "4":
                    handleLoanRequest();
                    break;
                case "5":
//                    hanldeDeposite(); //Đức An
                    break;
                case "6":
//                    handleWithdraw(); Đức An
                    break;
                case "7":
//                    handleTransfer();
                    break;
                case "8":
//                    handlePaymentLoan();   // Đức An
                    break;
                case "9":
//                    handClosedSavingAccount();  // Minh Anh
                    break;
                case "10":
                    handleViewTransactionHistory(); //Bảo
                    break;
                case "11":
//                    handleViewAccountDetails(); Băng
                    break;
                case "0":
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    public void getAllAccount(int userId) {
        System.out.println("\n=========================================================");
        System.out.println("               THÔNG TIN CÁ NHÂN & SỐ DƯ");
        System.out.println("=========================================================");
        System.out.println("👤 Khách hàng: " + customer.getFullName());
        System.out.println("📧 Email     : " + customer.getEmail());
        System.out.println("Thu nhập     : "+customer.getMonthlyIncome());

        System.out.println("---------------------------------------------------------");
        System.out.println("💳 DANH SÁCH TÀI KHOẢN:");


        List<Account> accounts = customerController.getAllAccountOfCustomer(customer);



        if (accounts == null || accounts.isEmpty()) {
            System.out.println("❌ Bạn chưa mở tài khoản nào tại hệ thống HKL Bank.");
        } else {

            System.out.printf("%-10s | %-15s | %-15s | %-10s\n",
                    "ID", "Loại tài khoản", "Số dư (VNĐ)", "Trạng thái");
            System.out.println("---------------------------------------------------------");

            // Duyệt qua từng tài khoản và in ra
            for (Account acc : accounts) {
                String accountType = getAccountTypeName(acc);

                // %-10d: In số nguyên ID
                // %15.2f: In số thập phân, căn phải (không có dấu -), lấy 2 số sau dấu phẩy
                System.out.printf("%-10d | %-15s | %15.2f | %-10s\n",
                        acc.getAccountId(),
                        accountType,
                        acc.getBalance(),
                        acc.getAccountStatus());
            }
        }
        System.out.println("=========================================================");
    }

    private String getAccountTypeName(Account account) {
        if (account instanceof CheckingAccount) return "Thanh toán";
        if (account instanceof SavingAccount) return "Tiết kiệm";
        if (account instanceof LoanAccount) return "Khoản vay";
        return "Chưa xác định";
    }

    //Tạo khoản vay
   public void handleLoanRequest(){
       System.out.println("Nhập số tiền muốn vay");
       String amountString = scanner.nextLine();
       System.out.println("Nhập kì hạn vay (1-6-12)");
       String termLoan = scanner.nextLine();

       // xử lí lỗi không đúng kì hạn vay
       if (!termLoan.equals("1") && !termLoan.equals("6") && !termLoan.equals("12")) {
           System.out.println("Lỗi: Kì hạn vay không đúng vui lòng nhập lại");
           return;
       }
       String mess = customerController.addLoanRequest(customer, amountString, termLoan);
       System.out.println(mess);

   }

    private void handleOpenCheckingAccount() {
        System.out.println("\n--- MỞ TÀI KHOẢN THANH TOÁN ---");
        System.out.print("Nhập số tiền nạp ban đầu (VNĐ): ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            String result = customerController.handleOpenCheckingAccount(customer, amount);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Số tiền không hợp lệ!");
        }
    }

    private void handleOpenSavingAccount() {
        System.out.println("\n--- MỞ SỔ TIẾT KIỆM ---");

        // 1) Lấy danh sách tài khoản THANH TOÁN đang ACTIVE
        List<Account> allAccounts = customerController.getAllAccountOfCustomer(customer);
        List<CheckingAccount> checkingAccounts = new ArrayList<>();
        if (allAccounts != null) {
            for (Account a : allAccounts) {
                if (a instanceof CheckingAccount
                        && a.getAccountStatus() == AccountStatus.ACTIVE) {
                    checkingAccounts.add((CheckingAccount) a);
                }
            }
        }

        if (checkingAccounts.isEmpty()) {
            System.out.println("Bạn chưa có tài khoản THANH TOÁN nào đang hoạt động.");
            System.out.println("Vui lòng mở tài khoản Thanh toán trước khi mở Sổ Tiết Kiệm.");
            return;
        }

        // 2) Hiển thị danh sách ddowx nhaamf
        System.out.println("---------------------------------------------------------");
        System.out.println("Danh sách tài khoản THANH TOÁN khả dụng:");
        System.out.printf("%-10s | %-18s | %-10s%n", "ID", "Số dư (VNĐ)", "Trạng thái");
        System.out.println("---------------------------------------------------------");
        for (CheckingAccount c : checkingAccounts) {
            System.out.printf("%-10d | %,18.2f | %-10s%n",
                    c.getAccountId(), c.getBalance(), c.getAccountStatus());
        }
        System.out.println("---------------------------------------------------------");

        // 3) Nhập dữ liệu
        try {
            System.out.print("Nhập Mã số tài khoản thanh toán muốn dùng: ");
            int checkingAccId = Integer.parseInt(scanner.nextLine());

            // Kiểm tra ID nhập
            boolean valid = false;
            for (CheckingAccount c : checkingAccounts) {
                if (c.getAccountId() == checkingAccId) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Lỗi: Mã tài khoản không nằm trong danh sách tài khoản Thanh toán của bạn.");
                return;
            }

            System.out.print("Nhập số tiền muốn gửi tiết kiệm (VNĐ): ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Nhập kỳ hạn gửi (1 - 6 - 12 tháng): ");
            int term = Integer.parseInt(scanner.nextLine());

            // Kiểm tra kỳ hạn hợp lệ
            if (term != 1 && term != 6 && term != 12) {
                System.out.println("Lỗi: Kỳ hạn gửi không hợp lệ! Chỉ chấp nhận 1,6 hoặc 12 tháng.");
                return;
            }

            String result = customerController.handleOpenSavingAccount(
                    customer, checkingAccId, amount, term);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Vui lòng nhập đúng định dạng số ");
        }
    }
    private void handleViewTransactionHistory() {
        System.out.println("\n--- XEM LỊCH SỬ GIAO DỊCH ---");
        System.out.print("Nhập mã số tài khoản cần xem sao kê: ");
        try {
            int accountId = Integer.parseInt(scanner.nextLine());

            List<Transaction> transactions =
                    customerController.handleViewTransactionHistory(customer, accountId);

            System.out.println("\n=========================================================================================================");
            System.out.println("                          SAO KÊ GIAO DỊCH TÀI KHOẢN: " + accountId);
            System.out.println("=========================================================================================================");
            if (transactions.isEmpty()) {
                System.out.println("   Tài khoản này chưa có giao dịch nào phát sinh.");
            } else {
                for (Transaction t : transactions) {
                    t.printReceipt();
                }
            }
            System.out.println("=========================================================================================================");

        } catch (NumberFormatException e) {
            System.out.println("=> Lỗi: Mã tài khoản phải là một số hợp lệ!");
        } catch (RuntimeException e) {
            System.out.println("=> Lỗi: " + e.getMessage());
        }
    }

}
