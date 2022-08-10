package com.pornblocker.pornblockerbackend.domain.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pornblocker.pornblockerbackend.domain.model.Keywords;
import com.pornblocker.pornblockerbackend.domain.model.PornSite;
import com.pornblocker.pornblockerbackend.persistence.entity.PornSiteEntity;
import com.pornblocker.pornblockerbackend.persistence.repository.KeywordsRepository;
import com.pornblocker.pornblockerbackend.persistence.repository.PornSiteRepository;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.common.collect.Streams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Controller;

@Controller
public class PornSiteService {
  private final PornSiteRepository pornSiteRepository;
  private final KeywordsRepository keywordsRepository;

  public PornSiteService(PornSiteRepository pornSiteRepository,
                         KeywordsRepository keywordsRepository) {
    this.pornSiteRepository = pornSiteRepository;
    this.keywordsRepository = keywordsRepository;
  }

  public List<PornSite> getAllPornSites() {
    var pornSiteEntities = pornSiteRepository.findAll();
    var pornSiteList = new ArrayList<PornSite>();
    pornSiteEntities.forEach(entity -> pornSiteList.add(new PornSite(entity)));
    return pornSiteList;
  }

  private List<String> getPornUrlsFromPage(Page page) throws MalformedURLException {
    String aTagWrapperClassName = "#rso > div:nth-child(n) > div > video-voyager > div > div > div > div.ct3b9e";
    List<ElementHandle> aTagElements = page.querySelectorAll(aTagWrapperClassName);

    List<String> pornUrls = new ArrayList<String>();
    for (ElementHandle aTagElement: aTagElements) {
      String href = aTagElement.querySelector("a").getAttribute("href");
      String baseUrl = new URL(new URL(href), "/").toString();
      pornUrls.add(baseUrl);
    }
    return pornUrls;
  }

  public List<String> getSearchResultUrls() {
    try {
      Keywords keywords = keywordsRepository.getKeywords();
      Stream<List> urls = keywords.getKeywords().map(this::getSearchResultUrlsByKeyword);
      List<String> flatUrls = (List<String>) urls.flatMap(Collection::stream)
          .collect(Collectors.toList());
      return flatUrls;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  public List<String> getSearchResultUrlsByKeyword(String keyword) {
    try (Playwright playwright = Playwright.create()) {
      BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
//      options.setHeadless(false);
      Browser browser = playwright.webkit().launch(options);
      Page page = browser.newPage();
      page.navigate("https://www.google.co.jp/videohp?hl=ja");
      page.waitForTimeout(1000);
      String inputSelector = "#lst-ib";
      page.locator(inputSelector).fill(keyword);
      page.locator(inputSelector).press("Enter");
      page.waitForTimeout(1000);
      List<String> pornUrls = new ArrayList<String>();
      String nextLinkSelector = "#pnnext > span:nth-child(2)";
      for (int i = 0; i <= 5; i++) {
        page.locator(nextLinkSelector).click();
        page.waitForTimeout(1000);
        List<String> pornUrlsFromPage = getPornUrlsFromPage(page);
        pornUrls.addAll(pornUrlsFromPage);
      }

      List<String> uniquePronUrls = new ArrayList<>(new HashSet<>(pornUrls));
      List<String> sortedUniquePronUrls = uniquePronUrls.stream().sorted().toList();
      return sortedUniquePronUrls;

    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  public void insertPornUrls (List<String> pornUrls) {
    List<PornSiteEntity> entities = pornSiteRepository.findAll();
    List<String> pornSiteUrlsByDB = entities.stream().map(PornSiteEntity::getSiteUrl).toList();
    List<String> allUrls = new ArrayList<>();
    allUrls.addAll(List.copyOf(pornUrls));
    allUrls.addAll(List.copyOf(pornSiteUrlsByDB));
    List<String> allUniqueUrls = new ArrayList<>(new HashSet<>(allUrls));


    List<PornSiteEntity> allPornSiteEntities = Streams.mapWithIndex(
        allUniqueUrls.stream(),(str, index) -> new PornSiteEntity(index, str)
    ).toList();

    pornSiteRepository.deleteAll();

    for (var entity: allPornSiteEntities) {
      System.out.println(entity.getSiteUrl());
    }
    pornSiteRepository.saveAll(allPornSiteEntities);

  }
}
