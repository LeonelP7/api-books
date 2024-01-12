package com.example.database.services;

import com.example.database.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookEntity saveUpdate(String isbn, BookEntity book);

    List<BookEntity> listAll();

    Page<BookEntity> listAll(Pageable pageable);

    Optional<BookEntity> findById(String isbn);

    boolean existsByid(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    void deleteById(String isbn);
}
