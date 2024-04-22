package dev.hobie.gatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Bean // <1>
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(authorize -> authorize.anyExchange().authenticated()) // <2>
        .csrf(ServerHttpSecurity.CsrfSpec::disable) // <3>
        .oauth2Login(Customizer.withDefaults()) // <4>
        .oauth2Client(Customizer.withDefaults());
    return http.build();
  }
}
