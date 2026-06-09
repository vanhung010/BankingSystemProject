package model.pattern.observer;

public interface MonthlyEventPublisher {
    void addObserver(MonthlyEventObserver observer);
    void removeObserver(MonthlyEventObserver observer);
    void notifyObservers();
}
