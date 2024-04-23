package dev.hobie.resourceservice.controller;

import dev.hobie.resourceservice.model.Customer;
import dev.hobie.resourceservice.repository.CustomerRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerRepository repository;

  @GetMapping("/customers")
  public Collection<Customer> customers() {
    return this.repository.findAll();
  }
}
