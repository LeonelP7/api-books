package com.example.database.controllers;

import com.example.database.domain.dto.BookDto;
import com.example.database.domain.entities.BookEntity;
import com.example.database.mappers.Mapper;
import com.example.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDto> bookMapper;

    private BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper,BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> saveUpdate(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean existsBook = bookService.existsByid(isbn);
        BookEntity savedBookEntity = bookService.saveUpdate(isbn,bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);
        if (existsBook){
            return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/books")
    public Page<BookDto> listAll(Pageable pageable){
        Page<BookEntity> books = bookService.listAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> findOne(@PathVariable String isbn){
        Optional<BookEntity> foundBook = bookService.findById(isbn);

        return foundBook.map(BookEntity -> {
            BookDto bookDto = bookMapper.mapTo(BookEntity);
            return new ResponseEntity(bookDto, HttpStatus.OK);
        } ).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdate(@PathVariable String isbn, @RequestBody BookDto bookDto){

        if(!bookService.existsByid(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);

        return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable String isbn){
        bookService.deleteById(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
