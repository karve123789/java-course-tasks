package com.example.library.repository;
import com.example.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest // Настраивает H2 базу данных и JdbcTemplate для теста
@Import(JdbcBookRepository.class) // Указываем, какой репозиторий нужно создать для теста
class JdbcBookRepositoryTest {

    @Autowired
    private JdbcBookRepository bookRepository;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD) // Очищает БД после теста
    void save_and_findById_shouldWorkCorrectly() {
        // Создаем книгу
        Book book = new Book(null, "1984", "Джордж Оруэлл", 1949);

        // Сохраняем ее
        bookRepository.save(book);

        // Пытаемся найти все книги, чтобы найти ID
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(1);
        Long savedId = books.get(0).getId();

        // Находим по ID
        Optional<Book> foundBook = bookRepository.findById(savedId);

        // Проверяем, что книга нашлась и ее данные верны
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("1984");
        assertThat(foundBook.get().getAuthor()).isEqualTo("Джордж Оруэлл");
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        // Сохраняем две книги
        bookRepository.save(new Book(null, "Книга 1", "Автор 1", 2000));
        bookRepository.save(new Book(null, "Книга 2", "Автор 2", 2010));

        // Ищем все книги
        List<Book> books = bookRepository.findAll();

        // Проверяем, что нашлось ровно 2 книги
        assertThat(books).hasSize(2);
    }
}