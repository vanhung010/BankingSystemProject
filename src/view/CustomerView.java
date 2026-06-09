package view;

import controller.CustomerController;
import model.entity.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
//                   handleOpenCheckingAccount(); Bảo
                    break;
                case "3":
//                    handleOpenSavingAccount();   Bảo
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
//                    handleViewTransactionHistory(); Bảo
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


}
