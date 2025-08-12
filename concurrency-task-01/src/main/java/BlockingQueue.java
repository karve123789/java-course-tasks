import java.util.LinkedList;
import java.util.Queue;
//Concurrency - блокирующая очередь
//Предположим, у вас есть пул потоков, и вы хотите реализовать блокирующую очередь для передачи задач между потоками.
//Создайте класс BlockingQueue, который будет обеспечивать безопасное добавление и извлечение элементов
//между производителями и потребителями в контексте пула потоков.
//
//Класс BlockingQueue должен содержать методы enqueue() для добавления элемента в очередь и dequeue() для извлечения элемента.
//Если очередь пуста, dequeue() должен блокировать вызывающий поток до появления нового элемента.
//
//очередь должна иметь фиксированный размер.
//
//Используйте механизмы wait() и notify() для координации между производителями и потребителями. Реализуйте метод size(),
//который возвращает текущий размер очереди.
public class BlockingQueue <E>{
    private final Queue<E> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Размер очереди должен быть положительным.");
        }
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }
    public synchronized void enqueue(E item) throws InterruptedException {
        while (this.queue.size() == this.capacity) {
            System.out.println("Очередь полна. Поток-производитель " + Thread.currentThread().getName() + " ожидает...");
            wait();
        }

        this.queue.add(item);
        System.out.println("Элемент '" + item + "' добавлен. Размер очереди: " + this.queue.size());

        notifyAll();
    }
    public synchronized E dequeue() throws InterruptedException {
        while (this.queue.isEmpty()) {
            System.out.println("Очередь пуста. Поток-потребитель " + Thread.currentThread().getName() + " ожидает...");
            wait();
        }

        E item = this.queue.poll();
        System.out.println("Элемент '" + item + "' извлечен. Размер очереди: " + this.queue.size());

        notifyAll();

        return item;
    }
    public synchronized int size() {
        return this.queue.size();
    }



    public static void main(String[] args) {
        final BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.enqueue(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer item = queue.dequeue();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        System.out.println("Запускаем производителя и потребителя...");
        Thread producerThread = new Thread(producer, "Producer-1");
        Thread consumerThread = new Thread(consumer, "Consumer-1");

        producerThread.start();
        consumerThread.start();
    }
    }

