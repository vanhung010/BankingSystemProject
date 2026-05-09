package view;

import model.entity.Staff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class StaffView {

    private Staff staff;
    Scanner scanner = new Scanner(System.in);

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
//                    handleLoanRequest();
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
}
