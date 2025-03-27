package org.server.dao;

import org.server.entities.Role;
import java.util.List;

public interface IRoleDAO {
    void save(Role role);
    Role findById(Long id);
    List<Role> findAll();
    void update(Role role);
    void delete(Long id);
}
