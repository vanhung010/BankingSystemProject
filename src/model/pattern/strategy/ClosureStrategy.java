package model.pattern.strategy;

/**
 * Strategy pattern cho xử lý tất toán sổ tiết kiệm
 */
public interface ClosureStrategy {
    /**
     * Tính số tiền thực nhận sau khi tất toán
     * @param principal số tiền gốc
     * @param interestEarned tiền lãi đã kiếm được
     * @param daysBeforeMaturity số ngày còn lại trước khi hết hạn
     * @return số tiền thực nhận
     */
    double calculateClosureAmount(double principal, double interestEarned, int daysBeforeMaturity);
}

