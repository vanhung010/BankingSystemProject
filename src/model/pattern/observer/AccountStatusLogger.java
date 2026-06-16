package model.pattern.observer;

import model.entity.Account;
import model.entity.enums.AccountStatus;

public class AccountStatusLogger implements AccountStatusObserver {

    @Override
    public void onStatusChanged(Account account, AccountStatus oldStatus, AccountStatus newStatus, String reason) {
        if (newStatus == AccountStatus.LOCKED) {
            System.out.println("[Cảnh báo] Tài khoản #" + account.getAccountId()
                    + " của khách hàng " + account.getOwner().getFullName()
                    + " đã bị khóa do "+reason);
        }
    }
}
