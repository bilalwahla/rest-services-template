package com.df.gs.rest;

import com.df.gs.rest.bookmarks.model.Account;
import com.df.gs.rest.bookmarks.model.Bookmark;
import com.df.gs.rest.bookmarks.repository.AccountRepository;
import com.df.gs.rest.bookmarks.repository.BookmarkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class RestServicesTemplateApplication {

  /**
   * This simply sets up small amount of data in the connected database for quick testing.
   *
   * @param accountRepository account repository
   * @param bookmarkRepository bookmark repository
   * @return command line runner
   */
  @Bean
  CommandLineRunner init(AccountRepository accountRepository,
                         BookmarkRepository bookmarkRepository) {
    return (evt) -> Arrays.asList(
        "mohammadali,bilalwahla".split(","))
        .forEach(
            a -> {
              Account account = accountRepository.save(new Account(a,
                  "password"));
              bookmarkRepository.save(new Bookmark(account,
                  "http://www.google.com", "Google search"));
              bookmarkRepository.save(new Bookmark(account,
                  "http://www.youtube.com", "YouTube"));
            }
        );
  }

  public static void main(String[] args) {
    SpringApplication.run(RestServicesTemplateApplication.class, args);
  }
}
