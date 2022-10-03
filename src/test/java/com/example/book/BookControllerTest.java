package com.example.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

  @Captor
  private ArgumentCaptor<BookRequest> bookCaptor;

  @Captor
  private ArgumentCaptor<Long> idCaptor;

  @Test
  @DisplayName("post a book then save to DB")
  public void postingANewBookShouldCreateANewBookInTheDatabase() throws Exception {
    BookRequest bookRequest = new BookRequest();
    bookRequest.setTitle("JAVA SE");
    bookRequest.setAuthor("Jack");
    bookRequest.setIsbn("12345");

    when(bookService.createNewBook(bookCaptor.capture())).thenReturn(1L);

    this.mockMvc.perform(post("/api/v1/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(bookRequest)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "http://localhost/api/v1/books/1"));

    assertThat(bookCaptor.getValue().getAuthor(), is("Jack"));
    assertThat(bookCaptor.getValue().getIsbn(), is("12345"));
    assertThat(bookCaptor.getValue().getTitle(), is("JAVA SE"));
  }

  @Test
  public void allBooksEndpointShouldReturnTwoBooks() throws Exception {
    when(bookService.getAllBooks()).thenReturn(List.of(createBook(1L, "Spring", "Josh Long", "1111"),
        createBook(1L, "Reactive Spring", "Josh Long", "1122")));

    this.mockMvc.perform(get("/api/v1/books")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title", is("Spring")))
        .andExpect(jsonPath("$[0].isbn", is("1111")))
        .andExpect(jsonPath("$[0].author", is("Josh Long")));

  }

  @Test
  public void givenAnIdOfExistingBookShouldReturnTheBook() throws Exception {
    when(bookService.getBookById(idCaptor.capture())).thenReturn(createBook(1L, "Spring", "Josh Long", "1111"));

    this.mockMvc.perform(get("/api/v1/books/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Spring")))
        .andExpect(jsonPath("$.isbn", is("1111")))
        .andExpect(jsonPath("$.author", is("Josh Long")));
  }


  @Test
  public void givenAnIdOfNotExistingBookShouldReturn404() throws Exception {
    when(bookService.getBookById(idCaptor.capture())).thenThrow(new BookNotFoundException(55L));

    this.mockMvc.perform(get("/api/v1/books/55")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    assertThat(idCaptor.getValue(), is(55L));
  }


  @Test
  public void updateBookWithKnownIDShouldReturn204() throws Exception {
    BookRequest bookRequest = new BookRequest();
    bookRequest.setTitle("JAVA SE");
    bookRequest.setAuthor("Jack");
    bookRequest.setIsbn("12345");

    when(this.bookService.updateBook(eq(1L), bookCaptor.capture()))
        .thenReturn(createBook(1L, "JAVA SE", "Jack", "12345"));

    this.mockMvc.perform(put("/api/v1/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsBytes(bookRequest)))
        .andExpect(status().isNoContent());

    BDDAssertions.then(bookCaptor.getValue().getTitle()).isEqualTo(bookRequest.getTitle());
    BDDAssertions.then(bookCaptor.getValue().getAuthor()).isEqualTo(bookRequest.getAuthor());
    BDDAssertions.then(bookCaptor.getValue().getIsbn()).isEqualTo(bookRequest.getIsbn());
  }

  @Test
  public void updateBookWithUnknownIDShouldThrowNotFoundException() throws Exception {
    BookRequest bookRequest = new BookRequest();
    bookRequest.setTitle("JAVA SE");
    bookRequest.setAuthor("Jack");
    bookRequest.setIsbn("12345");

    when(this.bookService.updateBook(eq(1L), bookCaptor.capture()))
        .thenThrow(new BookNotFoundException(1L));

    this.mockMvc.perform(put("/api/v1/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsBytes(bookRequest)))
        .andExpect(status().isNotFound());

    BDDAssertions.then(bookCaptor.getValue().getTitle()).isEqualTo(bookRequest.getTitle());
    BDDAssertions.then(bookCaptor.getValue().getAuthor()).isEqualTo(bookRequest.getAuthor());
    BDDAssertions.then(bookCaptor.getValue().getIsbn()).isEqualTo(bookRequest.getIsbn());
  }

  @Test
  public void deleteBookWithKnownIDShouldReturn204() throws Exception {
    doNothing().when(this.bookService).deleteBook(1L);

    this.mockMvc.perform(delete("/api/v1/books/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  public void deleteBookWithUnknownIDShouldReturn404() throws Exception {
    doThrow(new BookNotFoundException(1L))
        .when(this.bookService)
        .deleteBook(1L);

    this.mockMvc.perform(delete("/api/v1/books/1"))
        .andExpect(status().isNotFound());
  }

  private  Book createBook(long id, String title, String author, String isbn) {
   return new Book(id, title, isbn, author);
  }
}