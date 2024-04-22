package dev.hobie.resourceservice;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerHttpController {

  private final CustomerRepository repository;

  @GetMapping("/customers")
  public Collection<Customer> customers() {
    return this.repository.findAll();
  }
}
