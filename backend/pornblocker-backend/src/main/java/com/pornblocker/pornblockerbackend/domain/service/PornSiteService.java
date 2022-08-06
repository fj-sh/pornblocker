package com.pornblocker.pornblockerbackend.domain.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
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

  public void scrapingGoogle() {
    System.out.println("Execute scrapingGoogle.");
    try (Playwright playwright = Playwright.create()) {
      BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
      options.setHeadless(false);
      Browser browser = playwright.webkit().launch(options);
      Page page = browser.newPage();
      page.navigate("https://www.google.com/?hl=ja");
      String inputSelector = "body > div.L3eUgb > div.o3j99.ikrT4e.om7nvf > form > div:nth-child(1) > div.A8SBwf.emcav > div.RNNXgb > div > div.a4bIc > input";
      page.locator(inputSelector).fill("朝倉未来");
      page.press(inputSelector, "Enter");
      page.waitForTimeout(5000);
      page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
    }
  }
}
