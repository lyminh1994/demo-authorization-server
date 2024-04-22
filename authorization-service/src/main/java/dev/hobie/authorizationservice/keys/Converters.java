package dev.hobie.authorizationservice.keys;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class Converters {

  @Bean
  public RsaPublicKeyConverter rsaPublicKeyConverter(TextEncryptor textEncryptor) {
    return new RsaPublicKeyConverter(textEncryptor);
  }

  @Bean
  public RsaPrivateKeyConverter rsaPrivateKeyConverter(TextEncryptor textEncryptor) {
    return new RsaPrivateKeyConverter(textEncryptor);
  }
}
