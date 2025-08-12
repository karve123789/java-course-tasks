public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);

        System.out.println("Начальный общий баланс: " + bank.getTotalBalance());
        System.out.println("---------------------------------------------");

        // Перевод между счетами в разных потоках
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));

        transferThread1.start();
        transferThread2.start();

        try {
            transferThread1.join();
            transferThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("---------------------------------------------");
        // Вывод общего баланса
        System.out.println("Итоговый баланс счета " + account1.getId() + ": " + account1.getBalance());
        System.out.println("Итоговый баланс счета " + account2.getId() + ": " + account2.getBalance());
        System.out.println("Итоговый общий баланс: " + bank.getTotalBalance());
    }
}