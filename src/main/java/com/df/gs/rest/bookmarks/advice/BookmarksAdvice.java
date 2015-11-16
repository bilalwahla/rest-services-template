package com.df.gs.rest.bookmarks.advice;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.df.gs.rest.bookmarks.exception.UserNotFoundException;

/**
 * This class uses Spring MVC's {@code @ControllerAdvice} annotation. These are a useful way to extricate the
 * configuration of common concerns - like error handling - into a separate place, away from any individual Spring MVC
 * controller.
 *
 * @author bilalwahla
 */
@ControllerAdvice
public class BookmarksAdvice {

  /**
   * Here, we're telling Spring MVC that any code that throws a UserNotFoundException, should eventually be
   * handled by the this method.
   *
   * @param userNotFoundException  exception
   * @return the exception wrapped in {@code VndErrors}
   */
  @ResponseBody
  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  VndErrors userNotFoundExceptionHandler(UserNotFoundException userNotFoundException) {
    return new VndErrors("error", userNotFoundException.getMessage());
  }
}
