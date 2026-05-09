package model.pattern.strategy;

public class DemandInterestStrategy implements InterestStrategy{
    @Override
    public double calcInterest(double principal, double rate, int time) {
        return 0;
    }
}
