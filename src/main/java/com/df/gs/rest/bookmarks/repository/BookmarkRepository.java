package com.df.gs.rest.bookmarks.repository;

import com.df.gs.rest.bookmarks.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import com.df.gs.rest.bookmarks.model.Account;

import java.util.Collection;

/**
 * A repository to manage {@code Bookmark} entities.
 *
 * @author bilalwahla
 * @see Bookmark
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  /**
   * A custom finder-method, that dereferences the username property on the {@code Bookmark} entity's {@code Account}
   * relationship, ultimately requiring a join of some sort. The JPA query it generates is, roughly,
   * 'SELECT b from Bookmark b WHERE b.account.username = :username'.
   *
   * @param username  a named parameter for the query
   * @return the query result in the form of a collection of {@code Bookmark}s
   */
  Collection<Bookmark> findByAccountUsername(String username);
}
