package model.pattern.strategy;

/**
 * Strategy: Tất toán sổ tiết kiệm bình thường (đã hết hạn hoặc tất toán đúng hạn)
 */
public class NormalClosureStrategy implements ClosureStrategy {
    @Override
    public double calculateClosureAmount(double principal, double interestEarned, int daysBeforeMaturity) {
        // Trả lại gốc + toàn bộ lãi nếu tất toán đúng hạn (daysBeforeMaturity <= 0)
        // hoặc nếu đã hết hạn
        return principal + interestEarned;
    }
}

