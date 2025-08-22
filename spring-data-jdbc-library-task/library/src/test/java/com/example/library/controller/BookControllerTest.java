package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class) // Тестируем только веб-слой для BookController
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc; // Инструмент для отправки HTTP-запросов

    @MockBean // Создает мок BookService и добавляет его в Spring Context
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper; // Для преобразования объектов в JSON

    @Test
    void getAllBooks_shouldReturnListOfBooks() throws Exception {
        Book book = new Book(1L, "Книга 1", "Автор 1", 2020);
        when(bookService.findAll()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Книга 1"));
    }

    @Test
    void getBookById_whenBookExists_shouldReturnBook() throws Exception {
        Book book = new Book(1L, "Книга 1", "Автор 1", 2020);
        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("Автор 1"));
    }

    @Test
    void getBookById_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {
        when(bookService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_shouldReturnCreatedBook() throws Exception {
        Book newBook = new Book(null, "Новая Книга", "Новый Автор", 2025);
        Book savedBook = new Book(1L, "Новая Книга", "Новый Автор", 2025);

        // Используем any(Book.class), так как объект, приходящий в сервис,
        // будет отличаться от newBook (у него может не быть ID)
        when(bookService.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
