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

        public int getTransactionId() {
                return transactionId;
        }

        public void setTransactionId(int transactionId) {
                this.transactionId = transactionId;
        }

        public TransactionType getTransactionType() {
                return transactionType;
        }

        public void setTransactionType(TransactionType transactionType) {
                this.transactionType = transactionType;
        }

        public double getAmount() {
                return amount;
        }

        public void setAmount(double amount) {
                this.amount = amount;
        }

        public LocalDateTime getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp;
        }

        public Integer getAccountId() {
                return accountId;
        }

        public void setAccountId(Integer accountId) {
                this.accountId = accountId;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }
}
