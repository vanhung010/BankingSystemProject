package model.pattern.strategy;

/**
 * Strategy: Tất toán sổ tiết kiệm sớm (trước khi hết hạn)
 * Khi tất toán sớm, chỉ nhận 50% tiền lãi
 */
public class EarlyClosureStrategy implements ClosureStrategy {
    private static final double EARLY_CLOSURE_INTEREST_RATE = 0.5; // Chỉ nhận 50% lãi

    @Override
    public double calculateClosureAmount(double principal, double interestEarned, int daysBeforeMaturity) {
        // Nếu tất toán sớm, chỉ nhận 50% tiền lãi
        double adjustedInterest = interestEarned * EARLY_CLOSURE_INTEREST_RATE;
        return principal + adjustedInterest;
    }
}

