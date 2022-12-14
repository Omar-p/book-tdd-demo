package com.example.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false, unique = true)
  private String isbn;
  @Column(nullable = false)
  private String author;
}
