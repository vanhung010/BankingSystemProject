package model.pattern.observer;

import model.entity.Account;
import model.entity.enums.AccountStatus;

public class AccountStatusLogger implements AccountStatusObserver {
    @Override
    public void onStatusChanged(Account account, AccountStatus oldStatus,
                                AccountStatus newStatus, String reason) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("📋 SỰ KIỆN: THAY ĐỔI TRẠNG THÁI TÀI KHOẢN");
            System.out.println("=".repeat(60));
            System.out.println("👤 Khách hàng    : " + account.getOwner().getFullName());
            System.out.println("🏦 ID Tài khoản  : " + account.getAccountId());
            System.out.println("💰 Số dư hiện tại: " + String.format("%,.2f VNĐ",
                    account.getBalance()));
            System.out.println("📊 Trạng thái cũ : " + oldStatus);
            System.out.println("✅ Trạng thái mới: " + newStatus);
            System.out.println("📝 Lý do         : " + reason);
            System.out.println("=".repeat(60) + "\n");
        }
}
