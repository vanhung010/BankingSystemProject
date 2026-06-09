package model.pattern.observer;

import model.service.InterestService;
//đồng hồ lãi suất — mỗi tháng dư nợ tự động phình lên nếu khách chưa trả hết.
public class InterestObserver implements MonthlyEventObserver {

    private InterestService interestService;
    public InterestObserver(InterestService i) { this.interestService = i; }

    @Override
    public void onMonthAdvanced() {
        interestService.autoUpdateInterestLoanMonthly();
    }
}
