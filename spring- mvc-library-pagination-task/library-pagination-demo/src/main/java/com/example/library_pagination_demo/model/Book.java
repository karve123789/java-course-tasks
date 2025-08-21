package com.example.library_pagination_demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int publicationYear;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER для простоты, чтобы автор всегда загружался с книгой
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(String title, int publicationYear, Author author) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.author = author;
    }
}