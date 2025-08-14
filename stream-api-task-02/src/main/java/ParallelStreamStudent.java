import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//Практическое задачние - Stream API - агрегация и объединение результатов
//Создайте коллекцию студентов, где каждый студент содержит информацию о предметах,
//которые он изучает, и его оценках по этим предметам.
//Используйте Parallel Stream для обработки данных и создания Map,
//где ключ - предмет, а значение - средняя оценка по всем студентам.
//Выведите результат: общую Map с средними оценками по всем предметам.
public class ParallelStreamStudent {

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Student1", Map.of("geometry", 5, "algebra", 4)),
                new Student("Student2", Map.of("history", 4, "algebra", 3)),
                new Student("Student3", Map.of("history", 5, "algebra", 2)),
                new Student("Student4", Map.of("physics", 3, "algebra", 5)),
                new Student("Student5", Map.of("physics", 4, "algebra", 3))
        );

        // 2. Использование Parallel Stream для агрегации данных
        Map<String, Double> averageGrades = students.parallelStream()
                // Преобразуем поток студентов в плоский поток всех записей <Предмет, Оценка>
                .flatMap(student -> student.getGrades().entrySet().stream())
                // Группируем записи по предмету (ключу) и вычисляем среднюю оценку (значение)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.averagingDouble(Map.Entry::getValue)
                ));

        // 3. Вывод результата
        System.out.println("Средние оценки по всем предметам:");
        averageGrades.forEach((subject, avgGrade) ->
                System.out.println(subject + ": " + avgGrade)
        );
    }
}
