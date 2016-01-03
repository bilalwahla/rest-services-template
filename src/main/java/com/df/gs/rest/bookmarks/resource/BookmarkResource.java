package com.df.gs.rest.bookmarks.resource;

import com.df.gs.rest.bookmarks.controller.BookmarkRestController;
import com.df.gs.rest.bookmarks.model.Bookmark;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Wraps a {@code Bookmark} and provides a nice, centralized place to keep link-building logic. Spring HATEOAS provides
 * the convenient static {@code ControllerLinkBuilder.linkTo} and {@code ControllerLinkBuilder.methodOn methods} to
 * extract the URI from the controller metadata itself - a marked improvement and in keeping with the DRY (do not repeat
 * yourself) principle.
 *
 * @author bilalwahla
 * @see Bookmark
 */
public class BookmarkResource extends ResourceSupport {

  private final Bookmark bookmark;

  public BookmarkResource(Bookmark bookmark) {
    String username = bookmark.getAccount().getUsername();
    this.bookmark = bookmark;
    this.add(linkTo(BookmarkRestController.class, username).withRel("bookmarks"));
    this.add(linkTo(methodOn(BookmarkRestController.class, username).readBookmark(username, bookmark.getId())).withSelfRel());
  }

  public Bookmark getBookmark() {
    return bookmark;
  }
}
