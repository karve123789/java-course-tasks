package com.example.library.repository;

import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    int save(Book book);
    int update(Book book);
    Optional<Book> findById(Long id);
    int deleteById(Long id);
    List<Book> findAll();
}