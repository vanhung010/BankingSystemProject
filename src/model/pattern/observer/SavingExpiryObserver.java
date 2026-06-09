package model.pattern.observer;

import model.service.SavingService;
// nếu đã đáo hạn thì tính lãi, cộng vào gốc (lãi mẹ đẻ lãi con), rồi tự động gia hạn sổ thêm 1 kỳ và cập nhật maturityDate mới vào DB.
public class SavingExpiryObserver implements MonthlyEventObserver {

    private SavingService savingService;
    public SavingExpiryObserver(SavingService s) { this.savingService = s; }

    @Override
    public void onMonthAdvanced() {
        savingService.checkSavingAccountExpried();
    }
}
