package org.server.dao;

import org.server.entities.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerDAO {
    void save(Customer customer);
    Customer findById(Long id);
    List<Customer> findAll();
    void update(Customer customer);
    void delete(Long id);
	Optional<Customer> findByEmail(String email);
}
