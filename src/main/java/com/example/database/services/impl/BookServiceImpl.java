package com.example.database.services.impl;

import com.example.database.domain.entities.BookEntity;
import com.example.database.repositories.BookRepository;
import com.example.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookEntity saveUpdate(String isbn, BookEntity book) {
        book.setIsbn(isbn);
        return repository.save(book);
    }

    @Override
    public List<BookEntity> listAll() {
        return StreamSupport.stream(repository.findAll()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findById(String isbn) {
        return repository.findById(isbn);
    }

    @Override
    public boolean existsByid(String isbn) {
        return repository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);

        return repository.findById(isbn).map(existingBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthorEntity()).ifPresent(existingBook::setAuthorEntity);
            return repository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exists"));
    }

    @Override
    public void deleteById(String isbn) {
        repository.deleteById(isbn);
    }
}
