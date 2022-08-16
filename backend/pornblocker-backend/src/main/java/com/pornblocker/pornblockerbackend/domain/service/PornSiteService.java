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
import com.pornblocker.pornblockerbackend.presentation.api.PornSiteApi;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.common.collect.Streams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class PornSiteService {
  private final PornSiteRepository pornSiteRepository;
  private final KeywordsRepository keywordsRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(PornSiteService.class);

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
    List<String> flatUrls = new ArrayList<>();
    try {
      generateBrowserPage();
      Keywords keywords = keywordsRepository.getKeywords();
      Stream<List> urls = keywords.getKeywords().map(this::getSearchResultUrlsByKeyword);
      List<String> videoUrls = (List<String>) urls.flatMap(Collection::stream)
          .collect(Collectors.toList());
      flatUrls.addAll(videoUrls);
      System.out.println("動画の取得が完了しました。");

      Keywords keywords2 = keywordsRepository.getKeywords();
      Stream<List> imageUrls = keywords2.getKeywords().map(this::getImageSearchResultUrlsByKeyword);
      List<String> flatImageUrls = (List<String>) imageUrls.flatMap(Collection::stream).toList();
      for (String imageUrl: flatImageUrls) {
        System.out.println("画像：" + imageUrl);
      }
      flatUrls.addAll(flatImageUrls);
      this.browserPage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<String> uniquePronUrls = new ArrayList<>(new HashSet<>(flatUrls));
    List<String> sortedUniquePronUrls = uniquePronUrls.stream().sorted().toList();
    return sortedUniquePronUrls;
  }

  private Page browserPage;
  private void generateBrowserPage() {
    Playwright playwright = Playwright.create();
    BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);
    Browser browser = playwright.webkit().launch(options);
    this.browserPage = browser.newPage();
  }

  private Page getNavigatedPage(String url) {
    browserPage.navigate(url);
    browserPage.waitForTimeout(1000);
    return browserPage;
  }

  public List<String> getSearchResultUrlsByKeyword(String keyword) {
    List<String> pornUrls = new ArrayList<String>();
    try {
      String videoSearchUrl = "https://www.google.co.jp/videohp?hl=ja";
      Page page = getNavigatedPage(videoSearchUrl);
      String inputSelector = "#lst-ib";
      page.locator(inputSelector).fill(keyword);
      page.locator(inputSelector).press("Enter");
      page.waitForTimeout(1000);

      String nextLinkSelector = "#pnnext > span:nth-child(2)";
      for (int i = 1; i <= 3; i++) {
        page.locator(nextLinkSelector).click();
        page.waitForTimeout(1000);
        List<String> pornUrlsFromPage = getPornUrlsFromPage(page);
        pornUrls.addAll(pornUrlsFromPage);
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return pornUrls;
  }

  public List<String> getImageSearchResultUrlsByKeyword(String keyword) {
    System.out.println("画像を取得します。");
    String imageSearchUrl = "https://www.google.com/imghp?hl=ja_JP";
    List<String> imageResultUrls = new ArrayList<>();
    try {
      Page page = getNavigatedPage(imageSearchUrl);
      String inputSelector = "#sbtc > div > div.a4bIc > input";
      page.locator(inputSelector).fill(keyword);
      page.locator(inputSelector).press("Enter");
      page.waitForTimeout(1000);
      List<String> pornUrls = new ArrayList<>();
      String imageATagSelector = "#islrg > div.islrc > div:nth-child(n) > a.VFACy.kGQAp.sMi44c.d0NI4c.lNHeqe.WGvvNb";

      List<ElementHandle> imageATagElements = page.querySelectorAll(imageATagSelector);

      int index = 0;
      for (ElementHandle element : imageATagElements) {
        String href = element.getAttribute("href");

        String baseUrl = new URL(new URL(href), "/").toString();
        imageResultUrls.add(baseUrl);
        index++;
        if (index == 25) {
          break;
        }
      }
    } catch (Exception e) {
      LOGGER.error("[Exception]getImageSearchResultUrlsByKeyword" + e);
    }

    return imageResultUrls;
  }

  public void insertPornUrls(List<String> pornUrls) {
    List<PornSiteEntity> entities = pornSiteRepository.findAll();
    List<String> pornSiteUrlsByDB = entities.stream().map(PornSiteEntity::getSiteUrl).toList();
    List<String> allUrls = new ArrayList<>();
    allUrls.addAll(List.copyOf(pornUrls));
    allUrls.addAll(List.copyOf(pornSiteUrlsByDB));
    List<String> allUniqueUrls = new ArrayList<>(new HashSet<>(allUrls));


    List<PornSiteEntity> allPornSiteEntities = Streams.mapWithIndex(
        allUniqueUrls.stream(),(str, index) -> new PornSiteEntity(index, str)
    ).toList();

    System.out.println("以下のURLを格納します。");
    for (PornSiteEntity entity: allPornSiteEntities) {
      System.out.println(entity.getSiteUrl());
    }

    pornSiteRepository.deleteAll();
    pornSiteRepository.saveAll(allPornSiteEntities);

  }
}
