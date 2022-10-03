package com.example.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

  @Transactional
  public Book updateBook(long id, BookRequest bookRequestToUpdate) {
    final Book retrievedBook = this.bookRepository
        .findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));

    retrievedBook.setIsbn(bookRequestToUpdate.getIsbn());
    retrievedBook.setAuthor(bookRequestToUpdate.getAuthor());
    retrievedBook.setTitle(bookRequestToUpdate.getTitle());

    return retrievedBook;
  }

  public void deleteBook(Long id) {
    final Book retrievedBook = this.bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));
    this.bookRepository.delete(retrievedBook);
  }
}
