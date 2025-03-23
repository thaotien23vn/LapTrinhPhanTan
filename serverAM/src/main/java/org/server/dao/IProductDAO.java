package org.server.dao;

import org.server.entities.Category;
import org.server.entities.Product;
import org.server.services.PaginatedResult;

import java.math.BigDecimal;
import java.util.List;

public interface IProductDAO {
	void save(Product product);

	Product findById(Long id);

	List<Product> findAll();

	void update(Product product);

	void delete(Long id);

	PaginatedResult<Product> findAll(int pageNumber, int pageSize);

	boolean isQuantity(Long productId);

	List<Product> findByName(String name);

	List<Product> findByCategory(String category);

	List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

	void updateProductDetails(Long id, String name, Category category, BigDecimal price, Integer quantity);
}
