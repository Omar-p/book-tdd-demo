package com.example.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class BookService {
  private final BookRepository bookRepository;

  public Long createNewBook(BookRequest bookRequest) {
    Book book = new Book(null, bookRequest.getTitle(), bookRequest.getIsbn(), bookRequest.getAuthor());
    book = this.bookRepository.save(book);
    return book.getId();
  }

  public List<Book> getAllBooks() {
    return Collections.emptyList();
  }

  public Book getBookById(Long id) {
    return this.bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));
  }
}
