package dev.hobie.resource_service.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hobie.resource_service.model.Customer;
import dev.hobie.resource_service.repository.CustomerRepository;

@RestController
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerRepository repository;

  @GetMapping("/customers")
  public Collection<Customer> customers() {
    return this.repository.findAll();
  }
}
