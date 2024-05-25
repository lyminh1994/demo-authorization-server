package dev.hobie.authorizationservice.service;

import dev.hobie.authorizationservice.dto.UserDTO;

public interface UserService {

  /** Create a new user with the supplied details. */
  void createUser(UserDTO user);

  /** Update the specified user. */
  void updateUser(UserDTO user);

  /** Remove the user with the given login name from the system. */
  void deleteUser(String username);

  /**
   * Modify the current user's password. This should change the user's password in the persistent
   * user repository (database, LDAP etc).
   *
   * @param oldPassword current password (for re-authentication if required)
   * @param newPassword the password to change to
   */
  void changePassword(String oldPassword, String newPassword);

  /** Check if a user with the supplied login name exists in the system. */
  boolean userExists(String username);
}
