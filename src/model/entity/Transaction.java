package model.entity;

import model.entity.enums.TransactionType;

import java.time.LocalDateTime;

public class Transaction {

        private int transactionId;
        private TransactionType transactionType;
        private double amount;
        private LocalDateTime timestamp;
        private Integer accountId;
        private String description;
}
