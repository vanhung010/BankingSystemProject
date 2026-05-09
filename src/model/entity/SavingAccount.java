package model.entity;

import java.time.LocalDate;

public class SavingAccount extends Account {
    private Customer customerOwner;
    private int term;
    private LocalDate depositDate;
    private LocalDate maturityDate;
    private double interest; //% lãi mỗi năm
}