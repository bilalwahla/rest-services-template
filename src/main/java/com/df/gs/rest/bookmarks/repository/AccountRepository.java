package com.df.gs.rest.bookmarks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.df.gs.rest.bookmarks.model.Account;

import java.util.Optional;

/**
 * This repository will manage {@code Account} entities.
 *
 * @author bilalwahla
 * @see Account
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
  /**
   * A custom finder-method, that will basically, create a JPA query of the form
   * 'select a from Account a where a.username = :username'.
   *
   * @param username  a named parameter for the query
   * @return the query result in the form of container object of type {@code Account} which may or may not contain a
   * non-null value
   */
  Optional<Account> findByUsername(String username);
}
