package org.server.dao;

import org.server.entities.Order;
import java.util.List;

public interface IOrderDAO {
    void save(Order order);
    Order findById(Long id);
    List<Order> findAll();
    void update(Order order);
    void delete(Long id);
}
