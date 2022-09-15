package com.example.book;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
@Slf4j
public class BookInitializer implements CommandLineRunner {
  private final BookRepository bookRepository;

  @Override
  public void run(String... args) throws Exception {
    Faker faker = new Faker();
    log.info("Starting to initialize sample data....");
    IntStream.range(0, 10)
        .forEach(x -> {
          Book book = new Book();
          book.setTitle(faker.book().title());
          book.setAuthor(faker.book().author());
          book.setIsbn(UUID.randomUUID().toString());
          this.bookRepository.save(book);
        });
    log.info("........finished with data initialization");
  }
}
