package org.server.services;

import org.server.dao.IProductDAO;
import org.server.entities.Category;
import org.server.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductService implements IProductDAO {
	private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
	private final EntityManager entityManager;

	public ProductService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void save(Product product) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(product);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi lưu sản phẩm: " + e.getMessage(), e);
		}
	}

	@Override
	public Product findById(Long id) {
		return entityManager.find(Product.class, id);
	}

	@Override
	public List<Product> findAll() {
		return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
	}

	@Override
	public void update(Product product) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.merge(product);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), e);
		}
	}

	@Override
	public void delete(Long id) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Product product = findById(id);
			if (product != null) {
				entityManager.remove(product);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi xóa sản phẩm: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> findByName(String name) {
		return entityManager.createQuery("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:name)", Product.class)
				.setParameter("name", "%" + name + "%").getResultList();
	}

	@Override
	public List<Product> findByCategory(String category) {
		return entityManager.createQuery("SELECT p FROM Product p WHERE p.category = :category", Product.class)
				.setParameter("category", category).getResultList();
	}

	@Override
	public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
		return entityManager
				.createQuery("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice", Product.class)
				.setParameter("minPrice", minPrice).setParameter("maxPrice", maxPrice).getResultList();
	}

	@Override
	public boolean isQuantity(Long productId) {
		Product product = findById(productId);
		return product != null && product.getQuantity() > 0;
	}

	@Override
	public void updateProductDetails(Long id, String name, Category category, BigDecimal price, Integer quantity) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Product product = findById(id);
			if (product != null) {
				if (name != null && !name.trim().isEmpty())
					product.setName(name);
				if (category != null && !category.getName().trim().isEmpty())
					product.setCategory(category);
				if (price != null && price.compareTo(BigDecimal.ZERO) > 0)
					product.setPrice(price);
				if (quantity != null && quantity >= 0)
					product.setQuantity(quantity);
				entityManager.merge(product);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), e);
		}
	}

	/**
	 * Đếm tổng số sản phẩm.
	 */
	public long countAllProducts() {
		return entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
	}

	/**
	 * Tính tổng số trang dựa trên pageSize.
	 */
	public int getTotalPages(int pageSize) {
		long totalProducts = countAllProducts();
		return (int) Math.ceil((double) totalProducts / pageSize);
	}

	/**
	 * Lấy danh sách sản phẩm với phân trang.
	 */
	@Override
	public PaginatedResult<Product> findAll(int pageNumber, int pageSize) {
		List<Product> products = entityManager.createQuery("SELECT p FROM Product p", Product.class)
				.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize).getResultList();

		int totalPages = getTotalPages(pageSize);
		return new PaginatedResult<>(products, totalPages);
	}
}
