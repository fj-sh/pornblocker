package com.pornblocker.pornblockerbackend.presentation.api;

import com.pornblocker.pornblockerbackend.domain.model.ContentScript;
import com.pornblocker.pornblockerbackend.domain.service.ContentScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contentscript")
public class ContentScriptApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(ContentScriptApi.class);
  @Autowired
  private ContentScriptService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ContentScript getContentScript() {
    LOGGER.info("ContentScriptを返します。");
    var contentScript = service.createContentScript();
    return contentScript;
  }
}
