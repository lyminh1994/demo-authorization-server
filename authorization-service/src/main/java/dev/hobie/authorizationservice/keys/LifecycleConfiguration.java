package dev.hobie.authorizationservice.keys;

import dev.hobie.authorizationservice.repository.RsaKeyPairRepository;
import dev.hobie.authorizationservice.utils.KeyUtils;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleConfiguration {

  @Bean
  public ApplicationListener<RsaKeyPairGenerationRequestEvent> keyPairGenerationRequestListener(
      RsaKeyPairRepository repository, @Value("${jwt.key.id}") String keyId) {
    return event -> repository.save(KeyUtils.generateKeyPair(keyId, event.getSource()));
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyListener(
      ApplicationEventPublisher publisher, RsaKeyPairRepository repository) {
    return event -> {
      if (repository.findKeyPairs().isEmpty())
        publisher.publishEvent(new RsaKeyPairGenerationRequestEvent(Instant.now()));
    };
  }
}
