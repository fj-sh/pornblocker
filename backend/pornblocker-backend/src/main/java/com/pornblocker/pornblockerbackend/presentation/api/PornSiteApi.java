package com.pornblocker.pornblockerbackend.presentation.api;

import com.pornblocker.pornblockerbackend.domain.model.PornSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("porn")
@CrossOrigin(origins = "*")
public class PornSiteApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(PornSiteApi.class);

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PornSite getPornSite() {
    LOGGER.info("getPornSite");
    var pornSite = new PornSite();
    pornSite.setSiteUrl("https://www.google.com");
    return pornSite;
  }
}
