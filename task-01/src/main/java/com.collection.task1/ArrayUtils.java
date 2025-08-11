package com.collection.task1;
import java.lang.reflect.Array;

public class ArrayUtils {
    private ArrayUtils() {}

    public static <T> T[] filter(T[] array, Filter<T> filter) {
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);


        for (int i = 0; i < array.length; i++) {
            newArray[i] = filter.apply(array[i]);
        }

        return newArray;
    }

}
