package com.collection.task2;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/*Напишите метод, который получает на вход массив элементов и возвращает Map ключи в котором - элементы,
 а значения - сколько раз встретился этот элемент*/
public class CollectionUtilsStreams {
    public static <T> Map<T, Long> countElementsWithStream(T[] array) {

        if (array == null || array.length == 0) {
            return new HashMap<>();
        }
        return Arrays.stream(array)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static void main(String[] args) {

            Integer[] numbers = {1, 3, 4, 5, 1, 5, 4, 3, 5, 5};

            Map<Integer, Long> numberCountStream = CollectionUtilsStreams.countElementsWithStream(numbers);
            System.out.println("Метод со Stream API: " + numberCountStream); // Ожидаемый вывод: {1=2, 3=2, 4=2, 5=4}
    }
}
