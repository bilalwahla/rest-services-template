package com.df.gs.rest.controller;

import com.df.gs.rest.RestServicesTemplateApplication;
import com.df.gs.rest.bookmarks.model.Account;
import com.df.gs.rest.bookmarks.model.Bookmark;
import com.df.gs.rest.bookmarks.repository.AccountRepository;
import com.df.gs.rest.bookmarks.repository.BookmarkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestServicesTemplateApplication.class)
@WebAppConfiguration
public class BookmarkRestControllerTest {

  private MediaType contentType = new MediaType(MediaTypes.HAL_JSON.getType(), MediaTypes.HAL_JSON.getSubtype());
  private MockMvc mockMvc;
  private StringWriter bookmarkJson = new StringWriter();
  private String username = "mohammadali";
  private Account account;
  private List<Bookmark> bookmarkList = new ArrayList<>();

  @Rule
  public
  final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets/bookmarks");

  @Autowired
  private BookmarkRepository bookmarkRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    RestDocumentationResultHandler restDocumentationResultHandler = document(
            "{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
    );

    this.mockMvc = webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(this.restDocumentation))
        .alwaysDo(restDocumentationResultHandler)
        .build();

    this.bookmarkRepository.deleteAllInBatch();
    this.accountRepository.deleteAllInBatch();

    String password = "password";
    this.account = accountRepository.save(new Account(username, password));
    this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + username, "A description 1")));
    this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + username, "A description 2")));
  }

  @Test
  public void userNotFound() throws Exception {
    new ObjectMapper().writeValue(bookmarkJson, new Bookmark());
    mockMvc.perform(get("/george/bookmarks/"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void readBookmark() throws Exception {
    mockMvc.perform(get("/" + username + "/bookmarks/" + this.bookmarkList.get(0).getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.bookmark.uri", is("http://bookmark.com/1/" + username)))
        .andExpect(jsonPath("$.bookmark.description", is("A description 1")));
  }

  @Test
  public void readBookmarks() throws Exception {
    mockMvc.perform(get("/" + username + "/bookmarks"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$._embedded.bookmarkResources", hasSize(2)))
        .andExpect(jsonPath("$._embedded.bookmarkResources[0].bookmark.uri", is("http://bookmark.com/1/" + username)))
        .andExpect(jsonPath("$._embedded.bookmarkResources[0].bookmark.description", is("A description 1")))
        .andExpect(jsonPath("$._embedded.bookmarkResources[1].bookmark.uri", is("http://bookmark.com/2/" + username)))
        .andExpect(jsonPath("$._embedded.bookmarkResources[1].bookmark.description", is("A description 2")));
  }

  @Test
  public void addBookmark() throws Exception {
    new ObjectMapper().writeValue(bookmarkJson, new Bookmark(this.account, "http://www.abc.com", "some information"));
    this.mockMvc.perform(post("/" + username + "/bookmarks").contentType(contentType).content(bookmarkJson.toString()))
        .andExpect(status().isCreated());
  }
}
