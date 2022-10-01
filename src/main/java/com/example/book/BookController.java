package com.example.book;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

  private final BookService bookService;



  @PostMapping
  public ResponseEntity<Void> creatNewBook(@Valid @RequestBody BookRequest bookRequest, UriComponentsBuilder uriComponentsBuilder) {
    Long newBookID = this.bookService.createNewBook(bookRequest);

    final UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/books/{id}").buildAndExpand(newBookID);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uriComponents.toUri());

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks() {
    return ResponseEntity.ok(this.bookService.getAllBooks());
  }

  @GetMapping("/{id}")
  public Book getBook(@PathVariable("id") Long id) {
    return this.bookService.getBookById(id);
  }
}
