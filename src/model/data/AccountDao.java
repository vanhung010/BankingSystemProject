package model.data;

import model.entity.Account;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private List<Account> accountList;

    public AccountDao() {
        this.accountList = new ArrayList<>();
    }

    public void saveAccount(Account account) {
        accountList.add(account);
        //Phần ghi ra file account.txt làm sau
    }
}
