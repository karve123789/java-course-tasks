import java.util.concurrent.atomic.AtomicInteger;

public class BankAccount {

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    private final int id;
    private double balance;

    public BankAccount(double initialBalance) {
        this.id = idCounter.incrementAndGet(); // Присваиваем уникальный ID
        this.balance = initialBalance;
    }

    public synchronized void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public synchronized void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Недостаточно средств на счете " + id + " для снятия " + amount);
        }
    }

    public synchronized double getBalance() {
        return balance;
    }
    public int getId() {
        return id;
    }
}