package com.df.gs.rest.bookmarks.controller;

import com.df.gs.rest.bookmarks.model.Bookmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.df.gs.rest.bookmarks.exception.UserNotFoundException;
import com.df.gs.rest.bookmarks.repository.AccountRepository;
import com.df.gs.rest.bookmarks.repository.BookmarkRepository;
import com.df.gs.rest.bookmarks.resource.BookmarkResource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This REST controller handles all requests that start with a <code>username</code> (e.g. bob) followed by
 * <code>/bookmarks</code>.
 *
 * @author bilalwahla
 * @see AccountRepository
 * @see BookmarkRepository
 */
@RestController
@RequestMapping("/{username}/bookmarks")
public class BookmarkRestController {
  private static final Logger log = LoggerFactory.getLogger(BookmarkRestController.class);

  private final BookmarkRepository bookmarkRepository;

  private final AccountRepository accountRepository;

  @Autowired
  BookmarkRestController(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
    this.bookmarkRepository = bookmarkRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * The add method accepts incoming HTTP requests and saves them sending back a response with a status code of 201
   * (CREATED) and a header (Location) that the client can consult to learn how the newly created record is
   * referenceable.
   *
   * @param username  account the {@code Bookmark} is being added to
   * @param input     {@code Bookmark} being added
   * @return wrapper for a response, optionally, HTTP headers and status code
   */
  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<?> add(@PathVariable String username, @RequestBody Bookmark input) {
    log.info("Adding a new bookmark (" + input.toString() + ") to account " + username);
    this.accountRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

    return this.accountRepository.findByUsername(username).map(
        account -> {
          Bookmark bookmark = bookmarkRepository.save(new Bookmark(account, input.uri, input.description));
          log.debug("Bookmark uri: " + input.uri + " has been saved successfully with id: " + bookmark.getId());

          HttpHeaders httpHeaders = new HttpHeaders();
          Link forOneBookmark = new BookmarkResource(bookmark).getLink("self");
          httpHeaders.setLocation(URI.create(forOneBookmark.getHref()));

          return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
        }
    ).get();
  }

  /**
   * Gets a {@code Bookmark} record.
   *
   * @param username    account to get the {@code Bookmark} from
   * @param bookmarkId  id of the {@code Bookmark} to get
   * @return a {@code BookmarkResource} for the retrieved {@code Bookmark}
   */
  @RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
  public BookmarkResource readBookmark(@PathVariable String username, @PathVariable Long bookmarkId) {
    this.accountRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

    return new BookmarkResource(this.bookmarkRepository.findOne(bookmarkId));
  }

  /**
   * Get all bookmarks for an {@code Account}.
   *
   * @param username  username of the {@code Account} to get the bookmarks for
   * @return a collection of {@code BookmarkResource} resources
   */
  @RequestMapping(method = RequestMethod.GET)
  Resources<BookmarkResource> readBookmarks(@PathVariable String username) {
    this.accountRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

    List<BookmarkResource> bookmarkResourceList = bookmarkRepository.findByAccountUsername(username)
        .stream()
        .map(BookmarkResource::new)
        .collect(Collectors.toList());

    return new Resources<>(bookmarkResourceList);
  }
}
