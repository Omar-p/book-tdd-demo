package com.example.book;


import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTestIT {
  @LocalServerPort
  int randomPort;

  private TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    this.testRestTemplate = new TestRestTemplate();
  }

  @Test
  public void deleteBookWithKnownIDShouldReturn404AfterDeletion() throws Exception {
    long bookId = 1;
    String baseUri = "http://localhost:" + randomPort;

    final ResponseEntity<JsonNode> firstResult = this.testRestTemplate.getForEntity(
        baseUri + "/api/v1/books/" + bookId, JsonNode.class
    );

    BDDAssertions.then(firstResult.getStatusCode()).isEqualTo(HttpStatus.OK);

    this.testRestTemplate.delete(baseUri + "/api/v1/books/" + bookId);


    final ResponseEntity<JsonNode> secondResult = this.testRestTemplate.getForEntity(
        baseUri + "/api/v1/books/" + bookId, JsonNode.class
    );
    BDDAssertions.then(secondResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }



}
