package model.data;

import model.entity.Account;
import java.util.ArrayList;
import java.util.List;

public class SavingDao {
    private List<Account> savingAccountList;

    public SavingDao() {
        this.savingAccountList = new ArrayList<>();
    }

    public void saveAccount(Account account) {
        savingAccountList.add(account);
    }
}
