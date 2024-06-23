package dev.hobie.resource_service.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import dev.hobie.resource_service.model.Customer;

@Repository
public interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

  Customer findCustomerById(Integer id);
}
