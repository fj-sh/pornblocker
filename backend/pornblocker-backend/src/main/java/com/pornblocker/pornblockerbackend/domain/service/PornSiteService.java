package com.pornblocker.pornblockerbackend.domain.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pornblocker.pornblockerbackend.domain.model.PornSite;
import com.pornblocker.pornblockerbackend.persistence.repository.PornSiteRepository;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class PornSiteService {
  private final PornSiteRepository pornSiteRepository;

  public PornSiteService(PornSiteRepository pornSiteRepository) {
    this.pornSiteRepository = pornSiteRepository;
  }

  public List<PornSite> getAllPornSites() {
    var pornSiteEntities = pornSiteRepository.findAll();
    var pornSiteList = new ArrayList<PornSite>();
    pornSiteEntities.forEach(entity -> pornSiteList.add(new PornSite(entity)));
    return pornSiteList;
  }

  public void scrapingPornUrlFromGoogle(String keyword) {
    System.out.println("Execute scrapingGoogle.");
    try (Playwright playwright = Playwright.create()) {
      BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
      options.setHeadless(false);
      Browser browser = playwright.webkit().launch(options);
      Page page = browser.newPage();
      page.navigate("https://www.google.co.jp/videohp?hl=ja");
      page.waitForTimeout(1000);
      String inputSelector = "#lst-ib";
      page.locator(inputSelector).fill(keyword);
      page.locator(inputSelector).press("Enter");
      page.waitForTimeout(1000);
      page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
      String aTagWrapperClassName = "#rso > div:nth-child(n) > div > video-voyager > div > div > div > div.ct3b9e";
      List<ElementHandle> aTagElements = page.querySelectorAll(aTagWrapperClassName);
      for (ElementHandle aTagElement: aTagElements) {
        System.out.println(aTagElement.querySelector("a").getAttribute("href"));
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
