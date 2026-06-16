package model.service;

import model.data.SystemDao;
import model.pattern.observer.MonthlyEventObserver;
import model.pattern.observer.MonthlyEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class SystemService implements MonthlyEventPublisher {

    private List<MonthlyEventObserver> observers = new ArrayList<>();
    private SystemDao systemDao = new SystemDao();

    // --- implement MonthlyEventPublisher ---
    public void addObserver(MonthlyEventObserver o) { observers.add(o); }
    public void removeObserver(MonthlyEventObserver o) { observers.remove(o); }
    public void notifyObservers() {
        for (MonthlyEventObserver o : observers) o.onMonthAdvanced();
    }

    // --- sửa updateDateSystemPlus1Month ---
    public void updateDateSystemPlus1Month() {
        systemDao.updateDateSystemPlus1Month(); // tăng tháng
        notifyObservers();                    // báo cho tất cả
    }

    public void updateDateSystemPlusDays(int days) {
        systemDao.updateDateSystemPlusDays(days);
    }

    public void updateDateSystemMinusDays(int days) {
        systemDao.updateDateSystemMinusDays(days);
    }
}
