package model.pattern.observer;

import model.entity.Account;
import model.entity.enums.AccountStatus;

public class AccountStatusLogger implements AccountStatusObserver {

    @Override
    public void onStatusChanged(Account account, AccountStatus oldStatus, AccountStatus newStatus) {

    }
}
