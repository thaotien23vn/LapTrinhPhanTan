package org.server.services;

import org.server.dao.ICustomerDAO;
import org.server.entities.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CustomerService implements ICustomerDAO {
    private final EntityManager entityManager;

    public CustomerService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Customer findById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    @Override
    public void update(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    public boolean updateCustomer(Long id, Customer newCustomerData) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Customer existingCustomer = entityManager.find(Customer.class, id);
            if (existingCustomer != null) {
                // Cập nhật thông tin khách hàng
                existingCustomer.setName(newCustomerData.getName());
                existingCustomer.setEmail(newCustomerData.getEmail());
                existingCustomer.setPhone(newCustomerData.getPhone());
                existingCustomer.setAddress(newCustomerData.getAddress());
                existingCustomer.setRole(newCustomerData.getRole());

                entityManager.merge(existingCustomer);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Customer customer = findById(id);
            if (customer != null) {
                entityManager.remove(customer);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class
            );
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
