package dev.hobie.authorization_service.keys;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleConfiguration {

  // <1>
  @Bean
  ApplicationListener<RsaKeyPairGenerationRequestEvent> keyPairGenerationRequestListener(
      Keys keys, RsaKeyPairRepository repository, @Value("${jwt.key.id}") String keyId) {
    return event -> repository.save(keys.generateKeyPair(keyId, event.getSource()));
  }

  // <2>
  @Bean
  ApplicationListener<ApplicationReadyEvent> applicationReadyListener(
      ApplicationEventPublisher publisher, RsaKeyPairRepository repository) {
    return event -> {
      if (repository.findKeyPairs().isEmpty())
        publisher.publishEvent(new RsaKeyPairGenerationRequestEvent(Instant.now()));
    };
  }
}
