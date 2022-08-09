package com.pornblocker.pornblockerbackend.domain.model;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Keywords {
  Stream<String> keywords;
}
