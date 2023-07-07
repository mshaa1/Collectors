package it.collectors.controller;

public interface DataInitializable<T> {
    default void initializeData(T data) {
    }
}
