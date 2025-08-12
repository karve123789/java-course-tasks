import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {

    private final CyclicBarrier barrier;
    private final ExecutorService executorService;


    public ComplexTaskExecutor(int numberOfTasks) {
        Runnable barrierAction = () -> {
            System.out.println("\n>>> Все задачи выполнены и достигли барьера! Объединение результатов... <<<\n");
        };

        this.barrier = new CyclicBarrier(numberOfTasks, barrierAction);
        this.executorService = Executors.newFixedThreadPool(numberOfTasks);
    }


    public void executeTasks(int numberOfTasks) {
        System.out.println("Исполнитель запускает " + numberOfTasks + " задач...");
        for (int i = 0; i < numberOfTasks; i++) {

            executorService.submit(new ComplexTask(this.barrier));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

}