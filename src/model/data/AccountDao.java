package model.data;

import model.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private DataCenter dataCenter = DataCenter.getInstance();

    public List<Account> getAllAccountOfCustomerDao(int idCustomer) {
        List<Account> accountList = new ArrayList<>();

        for (Account account : dataCenter.getAccountList()) {
            if (account.getOwner() != null && account.getOwner().getUserId() == idCustomer) {
                accountList.add(account);
            }
        }

        return accountList;
    }

    public Account getAccountById(int idAccount) {
        // Danh sách account đã được nạp vào DataCenter, nên chỉ cần duyệt dữ liệu trong bộ nhớ.
        for (Account account : dataCenter.getAccountList()) {
            // So sánh accountId cần tìm với accountId của từng account trong DataCenter.
            if (account.getAccountId() == idAccount) {
                return account;
            }
        }

        // Giữ nguyên output cũ: nếu không tìm thấy account thì trả về null.
        return null;
    }

}
