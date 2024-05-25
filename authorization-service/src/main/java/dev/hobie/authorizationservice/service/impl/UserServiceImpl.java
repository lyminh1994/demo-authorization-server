package dev.hobie.authorizationservice.service.impl;

import dev.hobie.authorizationservice.dto.UserDTO;
import dev.hobie.authorizationservice.dto.UserPrinciple;
import dev.hobie.authorizationservice.exception.UserExistedException;
import dev.hobie.authorizationservice.exception.UserNotFoundException;
import dev.hobie.authorizationservice.model.Role;
import dev.hobie.authorizationservice.model.User;
import dev.hobie.authorizationservice.repository.AuthorityRepository;
import dev.hobie.authorizationservice.repository.UserRepository;
import dev.hobie.authorizationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthorityRepository authorityRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        userRepository
            .findById(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Username %s not found!".formatted(username)));

    return new UserPrinciple(
        user.getUsername(), user.getPassword(), user.isEnabled(), user.getAuthorities());
  }

  @Override
  public void createUser(UserDTO user) {
    if (userRepository.findById(user.username()).isPresent()) {
      throw new UserExistedException(
          "User with username: %s was existed!".formatted(user.username()));
    }

    var newUser = new User();
    newUser.setUsername(user.username());
    newUser.setPassword(passwordEncoder.encode(user.password()));
    newUser.setEnabled(true);
    var savedUser = userRepository.save(newUser);

    var authority = new Role();
    authority.setUsername(savedUser.getUsername());
    authority.setAuthority("ROLE_USER");
    authorityRepository.save(authority);
  }

  @Override
  public void updateUser(UserDTO user) {
    userRepository
        .findById(user.username())
        .ifPresentOrElse(
            oldUser -> {
              oldUser.setEnabled(false);
              userRepository.save(oldUser);
            },
            () -> {
              throw new UserNotFoundException(
                  "User with username: %s not found!".formatted(user.username()));
            });
  }

  @Override
  public void deleteUser(String username) {
    userRepository
        .findById(username)
        .ifPresentOrElse(
            userRepository::delete,
            () -> {
              throw new UserNotFoundException(
                  "User with username: %s not found!".formatted(username));
            });
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var username = authentication.getName();
    userRepository
        .findById(username)
        .ifPresentOrElse(
            user -> {
              if (!passwordEncoder.encode(oldPassword).equals(user.getPassword())) {
                throw new BadCredentialsException("Old password does not match!");
              }

              user.setPassword(passwordEncoder.encode(newPassword));
              userRepository.save(user);
            },
            () -> {
              throw new AuthenticationCredentialsNotFoundException("User not found!");
            });
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.findById(username).isPresent();
  }
}
