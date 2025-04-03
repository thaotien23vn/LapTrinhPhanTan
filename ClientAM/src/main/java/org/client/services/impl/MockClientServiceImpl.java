package org.client.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.client.entities.*;
import org.client.services.ClientService;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of ClientService for testing without a server connection
 */
public class MockClientServiceImpl implements ClientService {
    private final Gson gson;
    private Map<String, User> users;
    private final List<Product> products;
    private final List<CartItem> cartItems;
    private User currentUser;
    private static final String USER_DATA_FILE = "user_data.json";
    
    public MockClientServiceImpl() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new HashMap<>();
        this.products = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        
        // Load user data from file if exists
        loadUserData();
        
        // Add some sample products
        initSampleData();
    }
    
    private void initSampleData() {
        // Add sample products
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("T-Shirt");
        product1.setDescription("Cotton T-Shirt");
        product1.setPrice(19.99);
        product1.setQuantity(100);
        products.add(product1);
        
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Jeans");
        product2.setDescription("Blue Denim Jeans");
        product2.setPrice(39.99);
        product2.setQuantity(50);
        products.add(product2);
        
        // Only add sample users if no users exist yet
        if (users.isEmpty()) {
            // Add customer user
            User user = new User();
            user.setId(1L);
            user.setEmail("customer@example.com");
            user.setPassword("password");
            user.setFullName("Test Customer");
            user.setRole("CUSTOMER");
            users.put(user.getEmail(), user);
            
            // Add staff user (for POS system)
            User staffUser = new User();
            staffUser.setId(2L);
            staffUser.setEmail("staff@example.com");
            staffUser.setPassword("password");
            staffUser.setFullName("Minh - Staff");
            staffUser.setRole("STAFF");
            users.put(staffUser.getEmail(), staffUser);
            
            // Add admin user
            User adminUser = new User();
            adminUser.setId(3L);
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("admin");
            adminUser.setFullName("NgocThai");
            adminUser.setRole("ADMIN");
            users.put(adminUser.getEmail(), adminUser);
            
            // Save the initial user data
            saveUserData();
        }
    }
    
    /**
     * Load user data from file
     */
    private void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, User>>(){}.getType();
                Map<String, User> loadedUsers = gson.fromJson(reader, type);
                if (loadedUsers != null) {
                    users = loadedUsers;
                    System.out.println("Loaded " + users.size() + " users from file");
                }
            } catch (IOException e) {
                System.err.println("Error loading user data: " + e.getMessage());
            }
        }
    }
    
    /**
     * Save user data to file
     */
    private void saveUserData() {
        try (Writer writer = new FileWriter(USER_DATA_FILE)) {
            gson.toJson(users, writer);
            System.out.println("Saved " + users.size() + " users to file");
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    @Override
    public boolean addToCart(CartItem item) {
        // Check if item already exists in cart
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductId() == item.getProductId()) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return true;
            }
        }
        // Add new item to cart
        cartItems.add(item);
        return true;
    }

    @Override
    public boolean removeFromCart(int productId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProductId() == productId) {
                cartItems.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCartItem(int productId, int quantity) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductId() == productId) {
                cartItem.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean clearCart() {
        cartItems.clear();
        return true;
    }

    @Override
    public boolean placeOrder(Order order) {
        // In a real implementation, this would save the order to a database
        return true;
    }

    @Override
    public Order getOrderStatus(int orderId) {
        // Mock implementation - return a sample order
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("Processing");
        return order;
    }

    @Override
    public List<Order> getOrderHistory() {
        // Mock implementation - return an empty list
        return new ArrayList<>();
    }

    @Override
    public boolean cancelOrder(int orderId) {
        // Mock implementation - always return true
        return true;
    }

    @Override
    public boolean register(User user) {
        // Check if user already exists
        if (users.containsKey(user.getEmail())) {
            return false;
        }
        
        // Generate a unique ID for the user if not set
        if (user.getId() == 0L) {
            // Find the highest existing ID and increment by 1
            long maxId = 1L;
            for (User existingUser : users.values()) {
                if (existingUser.getId() > maxId) {
                    maxId = existingUser.getId();
                }
            }
            user.setId(maxId + 1);
        }
        
        // Add user to map
        users.put(user.getEmail(), user);
        
        // Save user data to file
        saveUserData();
        
        System.out.println("User registered: " + user.getEmail());
        return true;
    }

    @Override
    public User login(String email, String password) {
        System.out.println("Attempting login with email: " + email);
        System.out.println("Available users: " + users.keySet());
        
        User user = users.get(email);
        if (user != null) {
            System.out.println("User found, checking password");
            if (user.getPassword().equals(password)) {
                System.out.println("Password correct, login successful");
                currentUser = user;
                return user;
            } else {
                System.out.println("Password incorrect");
            }
        } else {
            System.out.println("User not found with email: " + email);
        }
        return null;
    }

    @Override
    public User getUserInfo() {
        return currentUser;
    }

    @Override
    public boolean updateUserInfo(User user) {
        if (users.containsKey(user.getEmail())) {
            users.put(user.getEmail(), user);
            if (currentUser != null && currentUser.getEmail().equals(user.getEmail())) {
                currentUser = user;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Check if a user with the given email exists
     * @param email The email to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String email) {
        return users.containsKey(email);
    }
    
    @Override
    public List<User> searchUsers(String keyword) {
        List<User> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        
        String searchTerm = keyword.toLowerCase();
        for (User user : users.values()) {
            // Check if email or name contains the search term
            if (user.getEmail().toLowerCase().contains(searchTerm) || 
                (user.getFullName() != null && user.getFullName().toLowerCase().contains(searchTerm))) {
                // Create a copy without password for security
                result.add(user.withoutPassword());
            }
        }
        return result;
    }
    
    @Override
    public List<Customer> searchCustomers(String keyword) {
        // Mock implementation - create some sample customers
        List<Customer> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        
        // Create sample customers for testing
        String searchTerm = keyword.toLowerCase();
        
        // Sample customer data
        String[][] customerData = {
            {"1", "Nguyễn Văn An", "an@example.com", "0901234567", "123 Nguyễn Huệ, Q1, TP.HCM"},
            {"2", "Trần Thị Bình", "binh@example.com", "0912345678", "456 Lê Lợi, Q1, TP.HCM"},
            {"3", "Lê Văn Cường", "cuong@example.com", "0923456789", "789 Trần Hưng Đạo, Q5, TP.HCM"},
            {"4", "Phạm Thị Dung", "dung@example.com", "0934567890", "101 Nguyễn Du, Q1, TP.HCM"},
            {"5", "Hoàng Văn Em", "em@example.com", "0945678901", "202 Lý Tự Trọng, Q1, TP.HCM"}
        };
        
        for (String[] data : customerData) {
            // Check if any field contains the search term
            if (data[1].toLowerCase().contains(searchTerm) || 
                data[2].toLowerCase().contains(searchTerm) || 
                data[3].contains(searchTerm) || 
                data[4].toLowerCase().contains(searchTerm)) {
                
                Customer customer = new Customer();
                customer.setId(Long.parseLong(data[0]));
                customer.setName(data[1]);
                customer.setEmail(data[2]);
                customer.setPhone(data[3]);
                customer.setAddress(data[4]);
                result.add(customer);
            }
        }
        
        return result;
    }
}
