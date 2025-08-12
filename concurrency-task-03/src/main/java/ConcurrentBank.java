import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


public class ConcurrentBank {

    private final Map<Integer, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(double initialBalance) {
        BankAccount account = new BankAccount(initialBalance);
        accounts.put(account.getId(), account);
        System.out.println("Создан счет ID: " + account.getId() + ", Баланс: " + initialBalance);
        return account;
    }

    public void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        if (fromAccount.getId() == toAccount.getId()) {
            System.out.println("Перевод на тот же счет невозможен.");
            return;
        }

        BankAccount lock1 = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        BankAccount lock2 = fromAccount.getId() < toAccount.getId() ? toAccount : fromAccount;

        synchronized (lock1) {
            synchronized (lock2) {
                System.out.println("Попытка перевода " + amount + " со счета " + fromAccount.getId() + " на счет " + toAccount.getId());
                if (fromAccount.getBalance() >= amount) {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                    System.out.println("Перевод успешен. Баланс счета " + fromAccount.getId() + ": " + fromAccount.getBalance() +
                            ", Баланс счета " + toAccount.getId() + ": " + toAccount.getBalance());
                } else {
                    System.out.println("Недостаточно средств для перевода со счета " + fromAccount.getId());
                }
            }
        }
    }

    public double getTotalBalance() {
        return accounts.values().stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }
}