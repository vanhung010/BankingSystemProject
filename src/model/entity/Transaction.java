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

        public Transaction(
                TransactionType transactionType,
                double amount,
                LocalDateTime timestamp,
                int accountId,
                Object ignored,
                String description
        ) {
                this.transactionType = transactionType;
                this.amount = amount;
                this.timestamp = timestamp;
                this.accountId = accountId;
                this.description = description;
        }

        public Transaction() {

        }

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

        //hàm in biên lai giao dịch
        public void printReceipt() {
                // Định dạng thời gian<dễ nhìn>
                String timeString = "";
                if (this.timestamp != null) {
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        timeString = this.timestamp.format(formatter);
                }

                // căn
                System.out.printf("Mã GD: %-5d | Loại: %-20s | Số tiền: %15.2f VNĐ | Thời gian: %-20s | Nội dung: %s\n",
                        transactionId, transactionType, amount, timeString, description);
        }
}
