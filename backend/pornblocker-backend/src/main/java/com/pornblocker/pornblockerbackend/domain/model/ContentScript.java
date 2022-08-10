package com.pornblocker.pornblockerbackend.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ContentScript {
  private List<String> js;
  private List<String> matches;
}
