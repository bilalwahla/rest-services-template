package com.df.gs.rest.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * This is an entity representing a bookmark.
 *
 * @author bilalwahla
 * @see Account
 */
@Entity
public class Bookmark {

  @JsonIgnore
  @ManyToOne
  private Account account;

  @Id
  @GeneratedValue
  private Long id;

  public String uri;
  public String description;

  public Bookmark() {

  }

  public Bookmark(Account account, String uri, String description) {
    this.account = account;
    this.uri = uri;
    this.description = description;
  }

  public Account getAccount() {
    return account;
  }

  public Long getId() {
    return id;
  }

  public String getUri() {
    return uri;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "uri: " + this.getUri() + " description: " + this.getDescription();
  }
}
