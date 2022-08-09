package com.pornblocker.pornblockerbackend.persistence.repository;

import com.pornblocker.pornblockerbackend.domain.model.Keywords;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class KeywordsRepository {
  public Keywords getKeywords() throws IOException {
    String fileName = "keywords.txt";
    Stream<String> keywords =  Files.readAllLines(Path.of(fileName)).stream();
    return new Keywords(keywords);
  }
}
