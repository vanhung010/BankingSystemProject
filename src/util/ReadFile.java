package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.entity.*;
import model.data.DataCenter;
import model.entity.enums.Role;
import model.entity.enums.AccountStatus;
import model.entity.enums.TransactionType;
import model.entity.enums.LoanRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReadFile {
    public static List<String> readLinesFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return lines;
    }

    public static void loadDataToDataCenter() {
        String basePath = "src/data/";

        // 1. Read Users
        List<String> userLines = readLinesFromFile(basePath + "user.txt");
        for (String line : userLines) {
            String[] parts = line.split(",");
            if (parts.length < 7) continue;

            String userType = parts[0];
            User user = null;
            if (userType.equals("CUSTOMER")) {
                Customer customer = new Customer();
                if (parts.length >= 8) {
                    customer.setMonthlyIncome(Double.parseDouble(parts[7]));
                }
                user = customer;
            } else if (userType.equals("STAFF")) {
                user = new Staff();
            }

            if (user != null) {
                user.setUserId(Integer.parseInt(parts[1]));
                user.setUserName(parts[2]);
                user.setPassword(parts[3]);
                user.setFullName(parts[4]);
                user.setRole(Role.valueOf(parts[5]));
                user.setEmail(parts[6]);

                DataCenter.getInstance().getUserList().add(user);
            }
        }

        // 2. Read Accounts
        List<String> accountLines = readLinesFromFile(basePath + "account.txt");
        for (String line : accountLines) {
            String[] parts = line.split(",");
            if (parts.length < 7) continue;
            
            String accType = parts[0];
            Account account = null;
            if (accType.equals("CHECKING") && parts.length >= 8) {
                CheckingAccount ca = new CheckingAccount();
                ca.setMinBalance(Double.parseDouble(parts[7]));
                account = ca;
            } else if (accType.equals("SAVING") && parts.length >= 11) {
                SavingAccount sa = new SavingAccount();
                sa.setTerm(Integer.parseInt(parts[7]));
                sa.setDepositDate(LocalDate.parse(parts[8]));
                sa.setMaturityDate(LocalDate.parse(parts[9]));
                sa.setInterest(Double.parseDouble(parts[10]));
                account = sa;
            } else if (accType.equals("LOAN") && parts.length >= 13) {
                LoanAccount la = new LoanAccount();
                la.setPricipalAmount(Double.parseDouble(parts[7]));
                la.setInterestRate(Double.parseDouble(parts[8]));
                la.setNextPaymentDate(LocalDate.parse(parts[9]));
                la.setLoanTerm(Integer.parseInt(parts[10]));
                la.setAmountPaidThisMonth(Double.parseDouble(parts[11]));
                la.setMonthlyRequiredPayment(Double.parseDouble(parts[12]));
                account = la;
            }
            
            if (account != null) {
                account.setAccountId(Integer.parseInt(parts[1]));
                account.setBalance(Double.parseDouble(parts[2]));
                account.setAccountStatus(AccountStatus.valueOf(parts[3]));
                account.setCreatedAt(LocalDate.parse(parts[5]));
                // Note: parts[4] is ownerId, handle mapping logic elsewhere or add transient ownerId field
                DataCenter.getInstance().getAccountList().add(account);
            }
        }

        // 3. Read Transactions
        List<String> txLines = readLinesFromFile(basePath + "transaction.txt");
        for (String line : txLines) {
            String[] parts = line.split(",");
            if (parts.length < 6) continue;
            Transaction tx = new Transaction();
            tx.setTransactionId(Integer.parseInt(parts[0]));
            tx.setTransactionType(TransactionType.valueOf(parts[1]));
            tx.setAmount(Double.parseDouble(parts[2]));
            tx.setTimestamp(LocalDateTime.parse(parts[3]));
            tx.setAccountId(Integer.parseInt(parts[4]));
            tx.setDescription(parts[5]);
            DataCenter.getInstance().getTransactionList().add(tx);
        }

        // 4. Read Loan Requests
        List<String> loanLines = readLinesFromFile(basePath + "loanRequest.txt");
        for (String line : loanLines) {
            String[] parts = line.split(",");
            if (parts.length < 6) continue;
            LoanRequest lr = new LoanRequest();
            lr.setLoanRequestId(Integer.parseInt(parts[0]));
            lr.setRequestAmount(Double.parseDouble(parts[2]));
            lr.setStatus(LoanRequestStatus.valueOf(parts[3]));
            lr.setRequestDate(LocalDateTime.parse(parts[4]));
            lr.setLoanTerm(Integer.parseInt(parts[5]));
            DataCenter.getInstance().getLoanRequestList().add(lr);
        }

        // 5. Read System Data
        List<String> sysLines = readLinesFromFile(basePath + "system.txt");
        for (String line : sysLines) {
            String[] parts = line.split(":");
            if (parts.length < 2) continue;

            String key = parts[0];
            String value = parts[1];

            switch (key) {
                case "SystemDate":
                    BankingSystem.setSystemDate(LocalDate.parse(value));
                    break;
                case "minCheckingBalance":
                    BankingSystem.setMinCheckingBalance(Double.parseDouble(value));
                    break;
                case "demandInterestRate":
                    BankingSystem.setDemandInterestRate(Double.parseDouble(value));
                    break;
                case "interestRate1M":
                    BankingSystem.setInterestRate1M(Double.parseDouble(value));
                    break;
                case "interestRate6M":
                    BankingSystem.setInterestRate6M(Double.parseDouble(value));
                    break;
                case "interestRate12M":
                    BankingSystem.setInterestRate12M(Double.parseDouble(value));
                    break;
                case "baseLoanInterestRate":
                    BankingSystem.setBaseLoanInterestRate(Double.parseDouble(value));
                    break;
                case "minSavingDeposit":
                    BankingSystem.setMinSavingDeposit(Double.parseDouble(value));
                    break;
            }
        }

        System.out.println("Data loaded successfully from files.");
    }
}
