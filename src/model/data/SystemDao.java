package model.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SystemDao {

    private DataCenter dataCenter = DataCenter.getInstance();

    public LocalDate getTimeSystem() {
      return dataCenter.getBankingSystem().getSystemDate();
    }

    public void updateDateSystemPlus1Month() {
        dataCenter.getBankingSystem().setSystemDate( dataCenter.getBankingSystem().getSystemDate().plusMonths(1));
    }

    public double getInterestLoan() {
        return dataCenter.getBankingSystem().getBaseLoanInterestRate();
    }
}
