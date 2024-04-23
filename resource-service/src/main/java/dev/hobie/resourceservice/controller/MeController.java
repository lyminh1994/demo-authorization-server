package dev.hobie.resourceservice.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

  @GetMapping("/me")
  public Map<String, String> principal(Principal principal) {
    return Map.of("name", principal.getName());
  }
}
