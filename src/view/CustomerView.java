package view;

import model.entity.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import controller.CustomerController;

public class CustomerView {
    Customer customer;
    Scanner scanner = new Scanner(System.in);



    // them cai nay
    CustomerController customerController;

    public CustomerView(Customer customer) {
        this.customer = customer;
// khoi tao
        this.customerController = new CustomerController();

    }
    public void run() {
        while (true) {
//            LocalDate currentDate = customerController.getDateSystem();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            String formattedDate = currentDate.format(formatter);

            System.out.println("===================================================");
            System.out.printf("Xin chào, %s | Role: CUSTOMER %n", customer.getUserName());
//            System.out.printf("Ngày hệ thống: %s %n", formattedDate);
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
            switch (choice){
                case "1":
//                    checkAllAccount(customer.getUserId());
                    break;
                case "2":
                    customerController.handleOpenCheckingAccount(customer);
                    break;
                case "3":
//                    handleOpenSavingAccount();
                    break;
                case "4":
//                    handleLoanRequest();
                    break;
                case "5":
//                    hanldeDeposite();
                    break;
                case "6":
//                    handleWithdraw();
                    break;
                case "7":
//                    handleTransfer();
                    break;
                case "8":
//                    handlePaymentLoan();
                    break;
                case "9":
//                    handClosedSavingAccount();
                    break;
                case "10":
//                    handleViewTransactionHistory();
                    break;
                case "11":
//                    handleViewAccountDetails();
                    break;
                case "0":
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}
