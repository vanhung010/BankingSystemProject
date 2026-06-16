package model.service;

import model.data.UserDao;
import model.entity.Account;
import model.entity.Customer;
import model.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Service để tìm kiếm và xem thông tin khách hàng
 * Chỉ Staff có thể sử dụng chức năng này
 */
public class CustomerSearchService {
    private static CustomerSearchService instance;
    private UserDao userDao;

    private CustomerSearchService() {
        this.userDao = new UserDao();
    }

    public static CustomerSearchService getInstance() {
        if (instance == null) {
            instance = new CustomerSearchService();
        }
        return instance;
    }

    /**
     * Tìm khách hàng theo ID
     */
    public String searchCustomerById(int customerId) {
        try {
            Customer customer = userDao.getCustomerById(customerId);
            if (customer == null) {
                return "❌ Không tìm thấy khách hàng với ID: " + customerId;
            }
            return formatCustomerInfo(customer);
        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Tìm khách hàng theo tên đăng nhập
     */
    public String searchCustomerByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return "❌ Tên đăng nhập không được để trống!";
            }

            Customer foundCustomer = null;
            for (User user : userDao.getAllCustomers()) {
                if (user instanceof Customer && user.getUserName().equals(username)) {
                    foundCustomer = (Customer) user;
                    break;
                }
            }

            if (foundCustomer == null) {
                return "❌ Không tìm thấy khách hàng với tên đăng nhập: " + username;
            }

            return formatCustomerInfo(foundCustomer);
        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Tìm khách hàng theo email
     */
    public String searchCustomerByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return "❌ Email không được để trống!";
            }

            Customer foundCustomer = null;
            for (User user : userDao.getAllCustomers()) {
                if (user instanceof Customer && user.getEmail().equalsIgnoreCase(email)) {
                    foundCustomer = (Customer) user;
                    break;
                }
            }

            if (foundCustomer == null) {
                return "❌ Không tìm thấy khách hàng với email: " + email;
            }

            return formatCustomerInfo(foundCustomer);
        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Tìm khách hàng theo tên đầy đủ
     */
    public String searchCustomerByFullName(String fullName) {
        try {
            if (fullName == null || fullName.trim().isEmpty()) {
                return "❌ Tên đầy đủ không được để trống!";
            }

            List<Customer> foundCustomers = new ArrayList<>();
            for (User user : userDao.getAllCustomers()) {
                if (user instanceof Customer && 
                    user.getFullName().toLowerCase().contains(fullName.toLowerCase())) {
                    foundCustomers.add((Customer) user);
                }
            }

            if (foundCustomers.isEmpty()) {
                return "❌ Không tìm thấy khách hàng với tên: " + fullName;
            }

            if (foundCustomers.size() == 1) {
                return formatCustomerInfo(foundCustomers.get(0));
            } else {
                return formatCustomerListInfo(foundCustomers);
            }
        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Xem danh sách tất cả khách hàng
     */
    public String viewAllCustomers() {
        try {
            List<Customer> customers = new ArrayList<>();
            for (User user : userDao.getAllCustomers()) {
                if (user instanceof Customer) {
                    customers.add((Customer) user);
                }
            }

            if (customers.isEmpty()) {
                return "❌ Hệ thống không có khách hàng nào!";
            }

            return formatCustomerListInfo(customers);
        } catch (Exception e) {
            return "❌ Lỗi: " + e.getMessage();
        }
    }

    /**
     * Định dạng thông tin 1 khách hàng
     */
    private String formatCustomerInfo(Customer customer) {
        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("                   THÔNG TIN KHÁCH HÀNG\n");
        result.append("============================================================\n");
        result.append("ID Khách hàng    : ").append(customer.getUserId()).append("\n");
        result.append("Tên đăng nhập    : ").append(customer.getUserName()).append("\n");
        result.append("Tên đầy đủ       : ").append(customer.getFullName()).append("\n");
        result.append("Email            : ").append(customer.getEmail()).append("\n");
        result.append("Thu nhập hàng tháng: ").append(String.format("%.2f VNĐ", customer.getMonthlyIncome())).append("\n");

        // Hiển thị danh sách tài khoản
        List<Account> accounts = customer.getAccountList();
        if (accounts != null && !accounts.isEmpty()) {
            result.append("---------------------------------------------------------\n");
            result.append("📊 DANH SÁCH TÀI KHOẢN:\n");
            result.append("---------------------------------------------------------\n");
            result.append(String.format("%-8s | %-15s | %-15s | %-15s\n",
                    "ID TK", "Loại", "Số dư (VNĐ)", "Trạng thái"));
            result.append("---------------------------------------------------------\n");

            for (Account acc : accounts) {
                String accountType = getAccountType(acc);
                result.append(String.format("%-8d | %-15s | %,15.2f | %-15s\n",
                        acc.getAccountId(),
                        accountType,
                        acc.getBalance(),
                        acc.getAccountStatus()));
            }
        } else {
            result.append("---------------------------------------------------------\n");
            result.append("❌ Khách hàng chưa mở tài khoản nào.\n");
        }

        result.append("============================================================\n");
        return result.toString();
    }

    /**
     * Định dạng danh sách khách hàng
     */
    private String formatCustomerListInfo(List<Customer> customers) {
        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("              DANH SÁCH KHÁCH HÀNG (Tìm thấy: ")
               .append(customers.size()).append(")\n");
        result.append("============================================================\n");
        result.append(String.format("%-8s | %-20s | %-20s | %-20s | %-15s\n",
                "ID", "Tên đăng nhập", "Tên đầy đủ", "Email", "Thu nhập"));
        result.append("--------+--------------------+--------------------+--------------------+-----------\n");

        for (Customer c : customers) {
            result.append(String.format("%-8d | %-20s | %-20s | %-20s | %,15.2f\n",
                    c.getUserId(),
                    c.getUserName(),
                    c.getFullName(),
                    c.getEmail(),
                    c.getMonthlyIncome()));
        }

        result.append("============================================================\n");
        return result.toString();
    }

    private String getAccountType(Account account) {
        if (account.getClass().getSimpleName().equals("CheckingAccount")) {
            return "Thanh toán";
        } else if (account.getClass().getSimpleName().equals("SavingAccount")) {
            return "Tiết kiệm";
        } else if (account.getClass().getSimpleName().equals("LoanAccount")) {
            return "Khoản vay";
        }
        return "Chưa xác định";
    }
}

