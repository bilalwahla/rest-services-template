package com.df.gs.rest.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a representation of bookmarks account. Each account may have no, one, or many {@code Bookmark} entities. This is a 1:N
 * relationship.
 *
 * @author bilalwahla
 * @see Bookmark
 */
@Entity
public class Account {

  @OneToMany(mappedBy = "account")
  private Set<Bookmark> bookmarks = new HashSet<>();

  @Id
  @GeneratedValue
  private Long id;

  @JsonIgnore
  public String password;

  public String username;

  Account() {
  }

  public Account(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public Set<Bookmark> getBookmarks() {
    return bookmarks;
  }

  public Long getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }
}
