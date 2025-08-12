import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
//Практическое задание - Concurrency - синхронизаторы
public class ComplexTask implements Runnable {

    private final CyclicBarrier barrier;

    public ComplexTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }


    @Override
    public void run() {
        try {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " выполняет свою часть задачи...");

            Thread.sleep((long) (Math.random() * 5000));
            System.out.println(threadName + " завершил свою часть и ожидает у барьера.");

            barrier.await();

            System.out.println(threadName + " прошел барьер и продолжает работу.");

        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            System.err.println("Задача для потока была прервана.");
        }
    }
}