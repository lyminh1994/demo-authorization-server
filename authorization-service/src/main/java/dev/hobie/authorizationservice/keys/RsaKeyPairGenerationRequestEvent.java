package dev.hobie.authorizationservice.keys;

import java.time.Instant;
import org.springframework.context.ApplicationEvent;

public class RsaKeyPairGenerationRequestEvent extends ApplicationEvent {

  public RsaKeyPairGenerationRequestEvent(Instant instant) {
    super(instant);
  }

  @Override
  public Instant getSource() {
    return (Instant) super.getSource();
  }
}
