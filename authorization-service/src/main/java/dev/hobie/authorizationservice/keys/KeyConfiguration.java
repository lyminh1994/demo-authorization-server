package dev.hobie.authorizationservice.keys;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.*;

@Configuration
public class KeyConfiguration {

  @Bean
  public TextEncryptor textEncryptor(
      @Value("${jwt.persistence.password}") String pw,
      @Value("${jwt.persistence.salt}") String salt) {
    return Encryptors.text(pw, salt);
  }

  @Bean
  public OAuth2TokenGenerator<OAuth2Token> delegatingOAuth2TokenGenerator(
      JwtEncoder encoder, OAuth2TokenCustomizer<JwtEncodingContext> customizer) {
    var generator = new JwtGenerator(encoder);
    generator.setJwtCustomizer(customizer);
    return new DelegatingOAuth2TokenGenerator(
        generator, new OAuth2AccessTokenGenerator(), new OAuth2RefreshTokenGenerator());
  }

  @Bean
  public NimbusJwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
  }
}
