package org.server.dao;

import org.server.entities.Cart;
import java.util.List;

public interface ICartDAO {
    void save(Cart cart);
    Cart findById(Long id);
    List<Cart> findAll();
    void update(Cart cart);
    void delete(Long id);
}
