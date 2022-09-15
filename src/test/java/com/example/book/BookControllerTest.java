package com.example.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BookService bookService;

  @Captor
  private ArgumentCaptor<BookRequest> argumentCaptor;

  @Test
  @DisplayName("post a book then save to DB")
  public void postingANewBookShouldCreateANewBookInTheDatabase() throws Exception {
    BookRequest bookRequest = new BookRequest();
    bookRequest.setTitle("JAVA SE");
    bookRequest.setAuthor("Jack");
    bookRequest.setIsbn("12345");

    when(bookService.createNewBook(argumentCaptor.capture())).thenReturn(1L);

    this.mockMvc.perform(post("/api/v1/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(bookRequest)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "http://localhost/api/v1/books/1"));

    assertThat(argumentCaptor.getValue().getAuthor(), is("Jack"));
    assertThat(argumentCaptor.getValue().getIsbn(), is("12345"));
    assertThat(argumentCaptor.getValue().getTitle(), is("JAVA SE"));
  }

}