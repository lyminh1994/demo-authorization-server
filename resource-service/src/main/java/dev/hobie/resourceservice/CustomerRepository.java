package dev.hobie.resourceservice;

import org.springframework.data.repository.ListCrudRepository;

public interface CustomerRepository extends ListCrudRepository<Customer, Integer> {
  Customer findCustomerById(Integer id);
}
