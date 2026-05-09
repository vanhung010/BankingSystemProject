package model.entity;

import java.time.LocalDate;

public class LoanAccount extends Account{

    private Account accountOwner;
    private double pricipalAmount;
    private double interestRate;
    private LocalDate nextPaymentDate;
    private int loanTerm;
    private double amountPaidThisMonth; //số tiền đã trả trong tháng
    private double monthlyRequiredPayment; //số tiền phải trả tối thiểu trong tháng

}
