package model.pattern.observer;

import model.service.LoanService;
//(1) Duyệt tất cả loan ACTIVE, tài khoản nào amountPaidThisMonth < monthlyRequiredPayment → khóa lại (LOCKED)
//(2) In thông báo ra màn hình cho từng tài khoản bị khóa
//(3) Tính lại số tiền phải trả tháng mới theo công thức dư nợ giảm dần
//(4) Reset amountPaidThisMonth = 0 — bắt đầu chu kỳ trả nợ mới
//(5) Cập nhật nextPaymentDate lên tháng tiếp theo
//(6) Lưu monthlyRequiredPayment vào DB để tháng sau đối chiếu
public class LoanMonthlyObserver implements MonthlyEventObserver {

    private LoanService loanService;


    public LoanMonthlyObserver(LoanService l) { this.loanService = l; }

    @Override
    public void onMonthAdvanced() {
        loanService.lockLoanAccountMonthly();

        loanService.checkLockLoanAccount();
        loanService.autoUpdateMonthlyRequiredPayment();
        loanService.updateAmountPaidMonthly();
        loanService.updateDatePaidMonthly();
        loanService.updateAmountMustPaidMonthly();
    }
}