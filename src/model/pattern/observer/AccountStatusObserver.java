package model.pattern.observer;

import model.entity.Account;
import model.entity.enums.AccountStatus;

public interface AccountStatusObserver {
    void onStatusChanged(Account account,
                         AccountStatus oldStatus,
                         AccountStatus newStatus, String reason);
}
