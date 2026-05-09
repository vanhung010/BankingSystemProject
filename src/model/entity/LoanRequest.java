package model.entity;

import model.entity.enums.LoanRequestStatus;

import java.time.LocalDateTime;

public class LoanRequest {
    private int loanRequestId;
    private Customer customerOwner;
    private double requestAmount;
    private LoanRequestStatus status;
    private LocalDateTime requestDate;
    private int loanTerm;
}
