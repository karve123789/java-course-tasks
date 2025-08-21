package com.example.library_pagination_demo;

import com.example.library_pagination_demo.model.Author;
import com.example.library_pagination_demo.model.Book;
import com.example.library_pagination_demo.repository.AuthorRepository;
import com.example.library_pagination_demo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LibraryPaginationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryPaginationDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(AuthorRepository authorRepository, BookRepository bookRepository) {
		return args -> {

			Author author1 = authorRepository.save(new Author("Лев Толстой"));
			Author author2 = authorRepository.save(new Author("Федор Достоевский"));
			Author author3 = authorRepository.save(new Author("Антон Чехов"));


			bookRepository.saveAll(List.of(
					new Book("Война и мир", 1869, author1),
					new Book("Анна Каренина", 1877, author1),
					new Book("Преступление и наказание", 1866, author2),
					new Book("Идиот", 1869, author2),
					new Book("Братья Карамазовы", 1880, author2),
					new Book("Вишневый сад", 1904, author3),
					new Book("Дядя Ваня", 1897, author3),
					new Book("Чайка", 1896, author3),
					new Book("Дама с собачкой", 1899, author3),
					new Book("Смерть Ивана Ильича", 1886, author1),
					new Book("Бесы", 1872, author2),
					new Book("Палата №6", 1892, author3)
			));
		};
	}
}