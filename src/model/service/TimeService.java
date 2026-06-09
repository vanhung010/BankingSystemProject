package model.service;

import model.data.SystemDao;

import java.time.LocalDate;

public class TimeService {
    private SystemDao systemDao = new SystemDao();

    public LocalDate getSystemDate() {
        return systemDao.getTimeSystem();
    }
}
