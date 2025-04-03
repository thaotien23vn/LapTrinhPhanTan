package org.client.services;

import org.client.entities.Order;
import org.client.entities.Product;
import org.client.entities.User;
import org.client.entities.CartItem;
import org.client.entities.Customer;
import java.util.List;

public interface ClientService {
    // Product endpoints
    List<Product> getAllProducts();
    Product getProductById(int id);
    List<Product> searchProducts(String keyword);
    
    // Cart endpoints
    boolean addToCart(CartItem item);
    boolean removeFromCart(int productId);
    boolean updateCartItem(int productId, int quantity);
    List<CartItem> getCartItems();
    boolean clearCart();
    
    // Order endpoints
    boolean placeOrder(Order order);
    Order getOrderStatus(int orderId);
    List<Order> getOrderHistory();
    boolean cancelOrder(int orderId);
    
    // User endpoints
    boolean register(User user);
    User login(String username, String password);
    User getUserInfo();
    boolean updateUserInfo(User user);
    
    // Search endpoints
    List<User> searchUsers(String keyword);
    List<Customer> searchCustomers(String keyword);
} 