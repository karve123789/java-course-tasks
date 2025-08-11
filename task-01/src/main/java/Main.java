import com.collection.task1.ArrayUtils;
import com.collection.task1.Filter;

import java.util.Arrays;
//Напишите метод filter, который принимает на вход массив любого типа, вторым арументом метод должен принимать класс,
// реализующий интерфейс Filter, в котором один метод - T apply(T o) (параметризованный).
//
//Метод должен быть реализован так чтобы возвращать новый массив, к каждому элементу которого была применена функция apply
public class Main {
    public static void main(String[] args) {
        Integer[] numbers = {1, 2, 3, 4, 5};
        Filter<Integer> squareOperation = (n) -> n * n;
        Integer[] squaredNumbers = ArrayUtils.filter(numbers, squareOperation);

        System.out.println("Исходный массив чисел: " + Arrays.toString(numbers));
        System.out.println("Массив после применения функции (возведение в квадрат): " + Arrays.toString(squaredNumbers));
        System.out.println("--------------------");

        String[] words = {"привет", "мир", "java"};
        Filter<String> toUpperCaseOperation = (s) -> s.toUpperCase();
        String[] upperCaseWords = ArrayUtils.filter(words, toUpperCaseOperation);

        System.out.println("Исходный массив строк: " + Arrays.toString(words));
        System.out.println("Массив после применения функции (верхний регистр): " + Arrays.toString(upperCaseWords));
        System.out.println("--------------------");



    }
}
