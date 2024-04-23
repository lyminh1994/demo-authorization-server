package dev.hobie.resourceservice.repository;

import dev.hobie.resourceservice.model.Customer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

  Customer findCustomerById(Integer id);
}
