package dev.hobie.authorizationservice;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class UsersConfiguration {

  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  public ApplicationRunner usersRunner(
      PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager) {
    return args -> {
      var builder = User.builder().roles("USER").passwordEncoder(passwordEncoder::encode); // <1>
      var users = Map.of("jlong", "password", "rwinch", "p@ssw0rd"); // <2>
      users.forEach(
          (username, password) -> {
            if (!userDetailsManager.userExists(username)) {
              var user = builder.username(username).password(password).build();
              userDetailsManager.createUser(user);
            }
          });
    };
  }
}
