package dev.hobie.resourceservice.controller;

import dev.hobie.resourceservice.repository.CustomerRepository;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

  private final MessageChannel requests;

  private final CustomerRepository repository;

  @PostMapping("/email")
  public Map<String, Object> email(
      @AuthenticationPrincipal Jwt jwt, @RequestParam Integer customerId) {
    var token = jwt.getTokenValue();
    var message =
        MessageBuilder.withPayload(repository.findCustomerById(customerId))
            .setHeader("jwt", token)
            .build();
    var sent = this.requests.send(message);
    return Map.of("sent", sent, "customerId", customerId);
  }
}
