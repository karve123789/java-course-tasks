package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Включает поддержку Mockito в JUnit 5
class BookServiceTest {

    // Создаем "мок" (заглушку) для репозитория. Он не будет обращаться к реальной БД.
    @Mock
    private BookRepository bookRepository;

    // Внедряем моки в наш сервис.
    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        // Создаем тестовый объект для всех тестов
        book = new Book(1L, "Тестовая Книга", "Тестовый Автор", 2025);
    }

    @Test
    void whenFindAll_shouldReturnBookList() {
        // 1. Задаем поведение мока:
        // когда будет вызван bookRepository.findAll(), вернуть список с нашей книгой.
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        // 2. Вызываем тестируемый метод
        List<Book> books = bookService.findAll();

        // 3. Проверяем результат
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Тестовая Книга");
    }

    @Test
    void whenFindById_shouldReturnBook() {
        // Задаем поведение мока
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Вызываем метод
        Optional<Book> foundBook = bookService.findById(1L);

        // Проверяем результат
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getAuthor()).isEqualTo("Тестовый Автор");
    }

    @Test
    void whenFindById_withInvalidId_shouldReturnEmpty() {
        // Задаем поведение мока для несуществующего ID
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Вызываем метод
        Optional<Book> foundBook = bookService.findById(99L);

        // Проверяем, что результат пустой
        assertThat(foundBook).isNotPresent();
    }

    @Test
    void whenSaveBook_shouldCallRepositorySave() {
        // Вызываем метод
        bookService.save(book);

        // Проверяем, что метод save у репозитория был вызван ровно 1 раз
        // с нашим объектом книги. Это важно для проверки взаимодействия.
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void whenDeleteById_shouldCallRepositoryDelete() {
        // Вызываем метод
        bookService.deleteById(1L);

        // Проверяем, что метод deleteById у репозитория был вызван ровно 1 раз
        // с правильным ID.
        verify(bookRepository, times(1)).deleteById(1L);
    }
}