package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Book book) {
        return jdbcTemplate.update("INSERT INTO book (title, author, publication_year) VALUES(?,?,?)",
                book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    @Override
    public int update(Book book) {
        return jdbcTemplate.update("UPDATE book SET title = ?, author = ?, publication_year = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
    }

    @Override
    public Optional<Book> findById(Long id) {
        try {
            Book book = jdbcTemplate.queryForObject("SELECT * FROM book WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Book.class), id);
            return Optional.ofNullable(book);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * from book", BeanPropertyRowMapper.newInstance(Book.class));
    }
}