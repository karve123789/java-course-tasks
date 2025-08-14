import java.util.concurrent.RecursiveTask;

    public class FactorialTask extends RecursiveTask<Long> {

        private static final int THRESHOLD = 5;

        private final long from;
        private final long to;

        public FactorialTask(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            // Если диапазон достаточно мал (меньше или равен порогу), вычисляем его напрямую.
            // Это базовый случай рекурсии.
            if ((to - from) <= THRESHOLD) {
                long result = 1L;
                for (long i = from; i <= to; i++) {
                    result *= i;
                }
                return result;
            }

            // Если диапазон большой, разделяем его на две подзадачи.
            long mid = (from + to) / 2;
            System.out.println("Разделение задачи: " + from + "-" + to + " -> " + from + "-" + mid + " и " + (mid + 1) + "-" + to);

            FactorialTask leftTask = new FactorialTask(from, mid);
            FactorialTask rightTask = new FactorialTask(mid + 1, to);

            // Асинхронно запускаем левую подзадачу (она будет выполняться в другом потоке).
            leftTask.fork();

            // Синхронно выполняем правую подзадачу в текущем потоке.
            long rightResult = rightTask.compute();

            // Ожидаем завершения левой подзадачи и получаем ее результат.
            long leftResult = leftTask.join();

            // Комбинируем результаты двух подзадач.
            return leftResult * rightResult;
        }
}
