package dev.hobie.resourceservice;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeHttpController {

  @GetMapping("/me")
  public Map<String, String> principal(Principal principal) {
    return Map.of("name", principal.getName());
  }
}
