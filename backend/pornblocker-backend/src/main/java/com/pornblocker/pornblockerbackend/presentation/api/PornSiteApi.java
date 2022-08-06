package com.pornblocker.pornblockerbackend.presentation.api;

import com.pornblocker.pornblockerbackend.domain.model.PornSite;
import com.pornblocker.pornblockerbackend.domain.service.PornSiteService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private PornSiteService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PornSite> getPornSite() {
    LOGGER.info("getPornSite");
    var pornSites = service.getAllPornSites();
    return pornSites;
  }

  @GetMapping("/scraping")
  public void execScraping() {
    service.scrapingGoogle();
  }
}
