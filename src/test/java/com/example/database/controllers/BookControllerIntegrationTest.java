package com.example.database.controllers;

import com.example.database.TestDataUtil;
import com.example.database.domain.dto.BookDto;
import com.example.database.domain.entities.BookEntity;
import com.example.database.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private BookService bookService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHttp201Created() throws Exception {

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void testThatFindAllBooksReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
          MockMvcResultMatchers.status().isOk()
        );
    }

    //commented by changed the findAll method to return a page
//    @Test
//    public void testThatFindAllBooksReturnsListOfBoks() throws Exception {
//        BookEntity testBookA = TestDataUtil.createTestBookA(null);
//        bookService.saveUpdate(testBookA.getIsbn(), testBookA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookA.getIsbn())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].title").value(testBookA.getTitle())
//        );
//    }

    @Test
    public void testThatFindBookReturnsHttp200OkWhenBookExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindBookReturnsHttp404WhenNoBookExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindBookReturnsBook() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHttp200OkWhenBookExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(savedBookEntity.getIsbn());
        String bookDtoJson = objectMapper.writeValueAsString(testBookDtoB);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookUpdates() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(savedBookEntity.getIsbn());
        String bookDtoJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoB.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDtoB.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHttp200OkWhenBookExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(savedBookEntity.getIsbn());
        String bookDtoJson = objectMapper.writeValueAsString(testBookDtoB);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(savedBookEntity.getIsbn());
        String bookDtoJson = objectMapper.writeValueAsString(testBookDtoB);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoB.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Book 2")
        );
    }

    @Test
    public void testThatDeleteReturnsHttp204NoContentWhenNoBookExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteReturnsHttp204NoContentWhenBookExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.saveUpdate(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/"+savedBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
