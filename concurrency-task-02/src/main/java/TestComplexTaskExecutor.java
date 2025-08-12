public class TestComplexTaskExecutor {

    public static void main(String[] args) {
        // Создаем исполнитель, который будет ждать 5 задач/потоков у барьера.
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5);

        Runnable testRunnable = () -> {
            System.out.println(">> " + Thread.currentThread().getName() + " начал тест.");
            taskExecutor.executeTasks(5);
            System.out.println(">> " + Thread.currentThread().getName() + " завершил вызов executeTasks.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        taskExecutor.shutdown();
    }
}