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

    public StaffView(Staff staff) {


        this.staff = staff;

    }
    public void run(){
        while(true){
//            LocalDate currentDate = systemDao.getTimeSystem();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            String formattedDate = currentDate.format(formatter);
            System.out.println("===================================================");
            System.out.printf("Xin chào, %s | Role: STAFF %n", staff.getUserName());
//            System.out.printf("Ngày hệ thống: %s %n", formattedDate);
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
//                    showInformationCutomer();
                    break;
                case "2":
//                    changeAccountStatus();
                    break;
                case "3":
//                    handleCheckAllLoanRequestPending();
                    break;
                case "4":
                    handleLoanRequest();
                    break;
                case "5":
//                    displayBankSettings();
                    break;
                case "6":
//                    handleUpdateBankSettings();
                    break;
                case "7":
//                    handleUpdateTime();
                    break;
                case "8":
//                    handlePlusDaySystem();
                    break;
                case "9":
//                    handleMinusDaySystem();
                    break;
                case "0":
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    public void handleLoanRequest(){
        handleCheckAllLoanRequestPending(); //show danh sách những khoản vay đang chờ xét duyệt
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
            CustomerView customerUI = new CustomerView(customer);
            // using directly customerUI method is fine or we should have an option, but keep logic
            customerUI.checkAllAccount(loanRequest.getCustomerOwner().getUserId());
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
    public void handleCheckAllLoanRequestPending(){

        try{
            List<LoanRequest> loanRequestList = staffController.getAllLoanRequestPending();
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
}
