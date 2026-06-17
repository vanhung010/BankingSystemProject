package view;

import controller.StaffController;
import model.entity.Customer;
import model.entity.LoanRequest;
import model.entity.Staff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class StaffView {

    private Staff staff;
    Scanner scanner = new Scanner(System.in);
    private StaffController staffController = new StaffController();
    CustomerView customerUI;


    public StaffView(Staff staff) {
        this.staff = staff;
    }

    public StaffView() {

    }

    public void run(){
        while(true){
            LocalDate currentDate = staffController.getSystemTime();;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);
            System.out.println("===================================================");
            System.out.printf("Xin chào, %s | Role: STAFF %n", staff.getUserName());
            System.out.printf("Ngày hệ thống: %s %n", formattedDate);
            System.out.println("===================================================");
            System.out.println("--- QUẢN LÝ KHÁCH HÀNG & TÀI KHOẢN ---");
            System.out.println("1. Tìm kiếm thông tin Khách hàng");
            System.out.println("2. Thay đổi trạng thái tài khoản (Khóa/Mở/Đóng)");
            System.out.println("--- NGHIỆP VỤ TÍN DỤNG ---");
            System.out.println("3. Xem danh sách Yêu cầu vay chờ duyệt (Pending)");
            System.out.println("4. Thẩm định & Quyết định giải ngân khoản vay");
            System.out.println("--- QUẢN TRỊ HỆ THỐNG ---");
            System.out.println("5. Xem cấu hình & Lãi suất ngân hàng");
            System.out.println("6. Cập nhật cấu hình hệ thống (Bank Settings)");
            System.out.println("--- QUẢN LÍ THỜI GIAN HỆ THỐNG ---");
            System.out.println("7. Tăng 1 tháng thời gian hệ thống");
            System.out.println("8. Tăng ngày thời gian hệ thống");
            System.out.println("9. Giảm thời gian hệ thống");
            System.out.println("--- KIỂM SOÁT GIAO DỊCH ---");
            System.out.println("10. Tra cứu lịch sử giao dịch toàn hệ thống");
            System.out.println("0. Đăng xuất");
            System.out.println("---------------------------------------------------");
            System.out.print("Nhập lựa chọn của bạn: ");
            String choice = scanner.nextLine();

            switch (choice){
                 case "1":
                     handleSearchCustomer();
                     break;
                 case "2":
                     changeAccountStatus();
                     break;
                 case "3":
                     showAllLoanRequestPending();
                     break;
                 case "4":
                     handleLoanRequest();
                     break;
                 case "5":
                     handleViewBankConfig();
                     break;
                case "6":
                    handleUpdateBankSettings();
                    break;
                case "7":
                    handleUpdateTime();
                    break;
                case "8":
                    handlePlusDaySystem();
                    break;
                case "9":
                    handleMinusDaySystem();
                    break;
                case "0":
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }


    private void changeAccountStatus() {
        System.out.println("\n--- THAY ĐỔI TRẠNG THÁI TÀI KHOẢN ---");
        staffController.showAllAccountsInSystem();

        System.out.print("Nhập ID tài khoản cần thay đổi: ");
        String accountIdStr = scanner.nextLine();

        System.out.println("Chọn trạng thái mới:");
        System.out.println("1. Khóa (LOCKED)");
        System.out.println("2. Mở (ACTIVE)");
        System.out.println("3. Đóng (CLOSED)");
        System.out.print("Chọn: ");
        String statusChoice = scanner.nextLine();
        String newStatusStr = "";
        if (statusChoice.equals("1")) newStatusStr = "LOCKED";
        else if (statusChoice.equals("2")) newStatusStr = "ACTIVE";
        else if (statusChoice.equals("3")) newStatusStr = "CLOSED";
        else {
            System.out.println("Lựa chọn trạng thái không hợp lệ.");
            return;
        }

        System.out.print("Nhập lý do thay đổi: ");
        String reason = scanner.nextLine();

        staffController.changeAccountStatus(accountIdStr, newStatusStr, reason);
    }

    public void handleUpdateBankSettings() {
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("             CẬP NHẬT CẤU HÌNH NGÂN HÀNG               ");
            System.out.println("=======================================================");
            System.out.println("1. Cập nhật số tiền khởi tạo tối thiểu (Checking)");
            System.out.println("2. Cập nhật số tiền gửi tiết kiệm tối thiểu");
            System.out.println("3. Cập nhật lãi suất vay cơ sở");
            System.out.println("4. Cập nhật lãi suất tiền gửi không kỳ hạn");
            System.out.println("5. Cập nhật lãi suất tiết kiệm 1 Tháng");
            System.out.println("6. Cập nhật lãi suất tiết kiệm 6 Tháng");
            System.out.println("7. Cập nhật lãi suất tiết kiệm 12 Tháng");
            System.out.println("0. Kết thúc và dời đi");
            System.out.println("-------------------------------------------------------");
            System.out.print("Nhập lựa chọn của bạn: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("Đã kết thúc quá trình cập nhật cấu hình.");
                break;
            }

            System.out.print("Nhập giá trị mới cần cập nhật (Nhập số thập phân, vd: 0.05 cho 5% hoặc 50000 cho VNĐ): ");
            String valueStr = scanner.nextLine();


            boolean isSuccess = false;
            switch (choice) {
                case "1":
                    System.out.println(staffController.updateConfigValue("min_checking_balance", valueStr));
                    break;
                case "2":
                    System.out.println(staffController.updateConfigValue("min_saving_deposit", valueStr));
                    break;
                case "3":
                    System.out.println(staffController.updateConfigValue("base_loan_interest_rate", valueStr));
                    break;
                case "4":
                    System.out.println(staffController.updateConfigValue("demand_interest_rate", valueStr));
                    break;
                case "5":
                    System.out.println(staffController.updateConfigValue("interest_rate_1M", valueStr));
                    break;
                case "6":
                    System.out.println(staffController.updateConfigValue("interest_rate_6M", valueStr));
                    break;
                case "7":
                    System.out.println(staffController.updateConfigValue("interest_rate_12M", valueStr));
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
                    continue; // Bỏ qua cập nhật nếu chọn sai
            }

            System.out.println(staffController.viewBankConfig());
        }
    }


    private void handleUpdateTime() {
        staffController.handleUpdateTime();
    }

    private void handlePlusDaySystem() {
        System.out.print("Nhập số ngày muốn tăng: ");
        String daysString = scanner.nextLine();
        try {
            int days = Integer.parseInt(daysString);
            if (days <= 0) {
                System.out.println("Số ngày phải lớn hơn 0!");
                return;
            }
            LocalDate currentDate = staffController.getSystemTime();
            LocalDate newDate = currentDate.plusDays(days);
            if (newDate.getMonthValue() != currentDate.getMonthValue() || newDate.getYear() != currentDate.getYear()) {
                System.out.println("Cộng ngày vượt qua tháng mới. Vui lòng chọn tính năng tăng 1 tháng!");
            } else {
                staffController.plusDaySystem(days);
                System.out.println("Đã tăng thời gian hệ thống thêm " + days + " ngày.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Số ngày không hợp lệ.");
        }
    }

    private void handleMinusDaySystem() {
        System.out.print("Nhập số ngày muốn giảm: ");
        String daysString = scanner.nextLine();
        try {
            int days = Integer.parseInt(daysString);
            if (days <= 0) {
                System.out.println("Số ngày phải lớn hơn 0!");
                return;
            }
            LocalDate currentDate = staffController.getSystemTime();
            LocalDate newDate = currentDate.minusDays(days);
            if (newDate.getMonthValue() != currentDate.getMonthValue() || newDate.getYear() != currentDate.getYear()) {
                System.out.println("Giảm ngày vượt qua tháng cũ. Không hợp lệ!");
            } else {
                staffController.minusDaySystem(days);
                System.out.println("Đã giảm thời gian hệ thống đi " + days + " ngày.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Số ngày không hợp lệ.");
        }
    }

    private void handleLoanRequest() {
        showAllLoanRequestPending(); //show danh sách những khoản vay đang chờ xét duyệt
        System.out.println("Nhập id của khoản vay muốn xử lí");
        String idLoanRequestString = scanner.nextLine();

        LoanRequest loanRequest = staffController.getLoanRequestById(idLoanRequestString);

        if(loanRequest == null){
            System.out.println("Không tìm thấy khoản vay");
            return;
        }

        System.out.println("1. Phê duyệt.");
        System.out.println("2. Từ chối.");
        String choice = scanner.nextLine();
        if(choice.equals("1")){
            Customer customer = staffController.getCustomerById(String.valueOf(loanRequest.getCustomerOwner().getUserId()));

            customerUI = new CustomerView(customer);

            customerUI.getAllAccount(loanRequest.getCustomerOwner().getUserId());
            System.out.println("Nhập id tài khoản thanh toán nhận tiền");
            String idAccountString = scanner.nextLine();

            staffController.approveLoanRequest(loanRequest, idAccountString);

        }
        else if(choice.equals("2")){
            staffController.rejectLoanRequest(loanRequest);
        }
        else {
            System.out.println("Vui lòng chọn đúng lựa chọn");
            return;
        }
    }

    public void showAllLoanRequestPending(){

        try{
            List<LoanRequest> loanRequestList = staffController.getAllLoanRequestPending();
            if(loanRequestList == null || loanRequestList.size() == 0){
                System.out.println("Danh sách yêu cầu vay đang trống");
                return;
            }
            System.out.println("\n===============================================================================");
            System.out.println("                 DANH SÁCH YÊU CẦU VAY CHỜ DUYỆT (PENDING)");
            System.out.println("===============================================================================");
            System.out.printf("%-5s | %-10s | %-18s | %-12s | %-20s\n",
                    "ID", "Mã Khách", "Số tiền vay (VNĐ)", "Kỳ hạn (Tháng)", "Ngày gửi yêu cầu");
            System.out.println("-------------------------------------------------------------------------------");
            for (LoanRequest req : loanRequestList) {
                System.out.printf("%-5d | %-10d | %,18.0f | %-14d | %-20s\n",
//                        req.getRequestId(),
                        req.getLoanRequestId(),
                        req.getCustomerOwner().getUserId(),
                        req.getRequestAmount(),
                        req.getLoanTerm(),
                        req.getRequestDate().toString());
            }
            System.out.println("===============================================================================");
        }
        catch(RuntimeException e){
             System.out.println(e.getMessage());
             return;
         }
     }

     private void handleSearchCustomer() {
         System.out.println("\n--- TÌM KIẾM THÔNG TIN KHÁCH HÀNG ---");
         System.out.println("1. Tìm theo ID khách hàng");
         System.out.println("2. Tìm theo tên đăng nhập");
         System.out.println("3. Tìm theo email");
         System.out.println("4. Tìm theo tên đầy đủ");
         System.out.println("5. Xem danh sách tất cả khách hàng");
         System.out.println("0. Quay lại");
         System.out.print("Chọn: ");
         String choice = scanner.nextLine();

         switch (choice) {
             case "1":
                 System.out.print("Nhập ID khách hàng: ");
                 try {
                     int id = Integer.parseInt(scanner.nextLine());
                     String result = staffController.searchCustomerById(id);
                     System.out.println(result);
                 } catch (NumberFormatException e) {
                     System.out.println("❌ Lỗi: ID phải là một số!");
                 }
                 break;
             case "2":
                 System.out.print("Nhập tên đăng nhập: ");
                 String username = scanner.nextLine();
                 String result = staffController.searchCustomerByUsername(username);
                 System.out.println(result);
                 break;
             case "3":
                 System.out.print("Nhập email: ");
                 String email = scanner.nextLine();
                 result = staffController.searchCustomerByEmail(email);
                 System.out.println(result);
                 break;
             case "4":
                 System.out.print("Nhập tên đầy đủ: ");
                 String fullName = scanner.nextLine();
                 result = staffController.searchCustomerByFullName(fullName);
                 System.out.println(result);
                 break;
             case "5":
                 result = staffController.viewAllCustomers();
                 System.out.println(result);
                 break;
             case "0":
                 break;
             default:
                 System.out.println("❌ Lựa chọn không hợp lệ!");
         }
     }

     private void handleViewBankConfig() {
         System.out.println("\n--- CẤU HÌNH & LÃI SUẤT NGÂN HÀNG ---");
         System.out.println("1. Xem tất cả cấu hình");
         System.out.println("2. Xem bảng lãi suất");
         System.out.println("3. Xem yêu cầu tối thiểu");
         System.out.println("4. Xem ngày hệ thống");
         System.out.println("0. Quay lại");
         System.out.print("Chọn: ");
         String choice = scanner.nextLine();

         String result = "";
         switch (choice) {
             case "1":
                 result = staffController.viewBankConfig();
                 break;
             case "2":
                 result = staffController.viewInterestRates();
                 break;
             case "3":
                 result = staffController.viewAccountRequirements();
                 break;
             case "4":
                 result = staffController.viewSystemDate();
                 break;
             case "0":
                 break;
             default:
                 result = "❌ Lựa chọn không hợp lệ!";
         }

         if (!result.isEmpty()) {
             System.out.println(result);
         }
     }
 }
