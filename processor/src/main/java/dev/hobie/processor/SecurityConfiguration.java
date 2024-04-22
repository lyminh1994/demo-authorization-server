package dev.hobie.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

@Configuration
public class SecurityConfiguration {

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(JwtDecoder decoder) {
    return new JwtAuthenticationProvider(decoder);
  }

  @Bean
  public JwtDecoder jwtDecoder(
      @Value("${spring.security.oauth2.authorizationserver.issuer}") String issuerUri) {
    return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    return new JwtAuthenticationConverter();
  }
}
