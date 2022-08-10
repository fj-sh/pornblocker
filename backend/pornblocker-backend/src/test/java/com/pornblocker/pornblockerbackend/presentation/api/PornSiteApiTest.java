package com.pornblocker.pornblockerbackend.presentation.api;



import static org.mockito.BDDMockito.given;

import com.pornblocker.pornblockerbackend.domain.model.PornSite;
import com.pornblocker.pornblockerbackend.domain.service.PornSiteService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PornSiteApi.class)
public class PornSiteApiTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PornSiteService service;

  @Test
  public void getAllPornSites_whenGetMethod() throws Exception {
    PornSite pornSite = new PornSite();
    pornSite.setPornSiteId(1L);
    pornSite.setSiteUrl("https://google.com");

    List<PornSite> allSites = Arrays.asList(pornSite);
    given(service.getAllPornSites()).willReturn(allSites);

    mvc.perform(get("/porn")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(1)))
        .andExpect(jsonPath("$[0].siteUrl", is(pornSite.getSiteUrl())));
  }

}
