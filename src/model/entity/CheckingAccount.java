package model.entity;
public class CheckingAccount extends Account {
    private double minBalance;
    public double getMinBalance() { return minBalance; }
    public void setMinBalance(double minBalance) { this.minBalance = minBalance; }

    public void deposit(double amount) {
        this.setBalance(this.getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if(this.getBalance() < amount){
            throw new RuntimeException("Không đủ số dư để thực hiện!");
        }
        else{
            this.setBalance(this.getBalance() - amount);
        }
    }
}
