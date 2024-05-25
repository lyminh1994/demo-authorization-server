package dev.hobie.authorizationservice.exception;

public class UserExistedException extends RuntimeException {

  public UserExistedException(String message) {
    super(message);
  }
}
