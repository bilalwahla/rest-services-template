package com.df.gs.rest.bookmarks.exception;

/**
 * This should be used to throw an exception when a given user is not found.
 *
 * @author bilalwahla
 */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String username) {
    super("could not find user '" + username + "'.");
  }
}
