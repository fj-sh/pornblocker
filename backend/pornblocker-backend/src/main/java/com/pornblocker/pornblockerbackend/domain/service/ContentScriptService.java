package com.pornblocker.pornblockerbackend.domain.service;

import com.pornblocker.pornblockerbackend.domain.model.ContentScript;
import com.pornblocker.pornblockerbackend.persistence.entity.PornSiteEntity;
import com.pornblocker.pornblockerbackend.persistence.repository.PornSiteRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class ContentScriptService {

  private final Logger LOGGER = LoggerFactory.getLogger(ContentScriptService.class);
  private final PornSiteRepository pornSiteRepository;

  public ContentScriptService(PornSiteRepository pornSiteRepository) {
    this.pornSiteRepository = pornSiteRepository;
  }

  private ContentScript createContentScriptFromPornSite(List<PornSiteEntity> pornSiteEntities) {
    List<String> js = new ArrayList<>();
    js.add("src/content-script/redirect-from-porn.ts");
    List<String> matches =
        pornSiteEntities.stream().map(PornSiteEntity::getSiteUrl).toList();
    return new ContentScript(js,matches);
  }

  public ContentScript createContentScript() {
    var pornSiteEntities = pornSiteRepository.findAll();
    return createContentScriptFromPornSite(pornSiteEntities);
  }
}
