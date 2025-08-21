package com.example.library_pagination_demo.controller;

import com.example.library_pagination_demo.model.Author;
import com.example.library_pagination_demo.model.Book;
import com.example.library_pagination_demo.repository.AuthorRepository;
import com.example.library_pagination_demo.repository.BookRepository;
import com.example.library_pagination_demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;


    private Author author1;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        author1 = new Author("Лев Толстой");
        author1.setId(1L);

        book1 = new Book("Война и мир", 1869, author1);
        book1.setId(1L);

        book2 = new Book("Анна Каренина", 1877, author1);
        book2.setId(2L);
    }

    @Test
    void whenGetAllBooks_shouldReturnPageOfBooks() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> bookPage = new PageImpl<>(List.of(book1, book2), pageable, 2);

        given(bookService.getAllBooks(any(Pageable.class))).willReturn(bookPage);

        // Act & Assert
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "title,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is("Война и мир")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void whenGetBookById_shouldReturnBook() throws Exception {
        // Arrange
        given(bookService.getBookById(1L)).willReturn(book1);

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Война и мир")))
                .andExpect(jsonPath("$.author.name", is("Лев Толстой")));
    }

    @Test
    void whenGetBookByInvalidId_shouldReturnNotFound() throws Exception {

        given(bookService.getBookById(eq(99L))).willThrow(new com.example.library_pagination_demo.exception.ResourceNotFoundException(""));

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateBook_shouldReturnCreatedBook() throws Exception {
        // Arrange
        given(bookService.createBook(any(Book.class))).willReturn(book1);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Война и мир")));
    }

    @Test
    void whenUpdateBook_shouldReturnUpdatedBook() throws Exception {
        // Arrange
        Book updatedBookDetails = new Book("Война и Мир (Новое издание)", 1869, author1);
        given(bookService.updateBook(eq(1L), any(Book.class))).willReturn(updatedBookDetails);

        // Act & Assert
        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBookDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Война и Мир (Новое издание)")));
    }

    @Test
    void whenDeleteBook_shouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}