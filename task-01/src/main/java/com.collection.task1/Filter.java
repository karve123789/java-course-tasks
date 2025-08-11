package com.collection.task1;

@FunctionalInterface
public interface Filter<T> {

    T apply(T o);
}