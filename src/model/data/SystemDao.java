package model.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import model.entity.BankingSystem;

public class SystemDao {

    private DataCenter dataCenter = DataCenter.getInstance();

    public LocalDate getTimeSystem() {
      return dataCenter.getBankingSystem().getSystemDate();
    }

    public void updateDateSystemPlus1Month() {
        dataCenter.getBankingSystem().setSystemDate( dataCenter.getBankingSystem().getSystemDate().plusMonths(1));
    }

    public void updateDateSystemPlusDays(int days) {
        dataCenter.getBankingSystem().setSystemDate( dataCenter.getBankingSystem().getSystemDate().plusDays(days));
    }

    public void updateDateSystemMinusDays(int days) {
        dataCenter.getBankingSystem().setSystemDate( dataCenter.getBankingSystem().getSystemDate().minusDays(days));
    }

    public double getInterestLoan() {
        return dataCenter.getBankingSystem().getBaseLoanInterestRate();
    }

    public double getDemandInterestRate() {
        return dataCenter.getBankingSystem().getDemandInterestRate();
    }

    public boolean updateConfigValue(String columnName, double newValue) {
        BankingSystem system = dataCenter.getBankingSystem();
        if (system == null) return false;

        switch (columnName) {
            case "min_checking_balance":
                system.setMinCheckingBalance(newValue);
                break;
            case "demand_interest_rate":
                system.setDemandInterestRate(newValue);
                break;
            case "interest_rate_1M":
                system.setInterestRate1M(newValue);
                break;
            case "interest_rate_6M":
                system.setInterestRate6M(newValue);
                break;
            case "interest_rate_12M":
                system.setInterestRate12M(newValue);
                break;
            case "base_loan_interest_rate":
                system.setBaseLoanInterestRate(newValue);
                break;
            case "min_saving_deposit":
                system.setMinSavingDeposit(newValue);
                break;
            default:
                return false; // Cột không hợp lệ
        }
        return true;
    }
}
