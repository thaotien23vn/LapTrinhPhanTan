package org.server.controllers;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import javax.swing.JTextArea;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.server.utils.UserAdapter;
import java.util.Map;
import java.util.Date;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.server.entities.User;
import org.server.entities.Role;
import org.server.entities.Order;
import org.server.entities.OrderItem;
import org.server.entities.Product;
import org.server.entities.Customer;
import org.server.services.UserService;
import org.server.services.RoleService;
import org.server.services.OrderService;
import org.server.services.ProductService;
import org.server.services.CustomerService;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private JTextArea logArea;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isRunning = true;
    private EntityManager entityManager;

    private final Map<String, BiFunction<String[], PrintWriter, String>> commands = new HashMap<>();

    public ClientHandler(Socket socket, JTextArea logArea, EntityManager entityManager) {
        this.clientSocket = socket;
        this.logArea = logArea;
        this.entityManager = entityManager;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Định nghĩa các lệnh hợp lệ
            commands.put("PING", (args, out) -> "PONG");
            commands.put("HELLO", (args, out) -> "Xin chào từ Server!");
            commands.put("TIME", (args, out) -> "Thời gian hiện tại: " + java.time.LocalTime.now());
            
            // Product related commands
            commands.put("GET_PRODUCTS", (args, out) -> {
                log("Handling GET_PRODUCTS request");
                try {
                    // Get ProductService from context
                    ProductService productService = new ProductService(entityManager);
                    
                    // Get all products
                    List<Product> products = productService.findAll();
                    
                    // Convert to JSON and return
                    return new Gson().toJson(products);
                } catch (Exception e) {
                    log("Error getting products: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            commands.put("ADD_PRODUCT", (args, out) -> {
                log("Handling ADD_PRODUCT request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse product data from JSON
                    String productJson = args[1];
                    Product product = new Gson().fromJson(productJson, Product.class);
                    log("Adding product: " + product.getName());
                    
                    // Get ProductService from context
                    ProductService productService = new ProductService(entityManager);
                    
                    // Save product
                    productService.save(product);
                    
                    log("Product added successfully");
                    return "true";
                } catch (Exception e) {
                    log("Error adding product: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("UPDATE_PRODUCT", (args, out) -> {
                log("Handling UPDATE_PRODUCT request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse product data from JSON
                    String productJson = args[1];
                    Product product = new Gson().fromJson(productJson, Product.class);
                    log("Updating product with ID: " + product.getId());
                    
                    // Get ProductService from context
                    ProductService productService = new ProductService(entityManager);
                    
                    // Check if product exists
                    Product existingProduct = productService.findById(product.getId());
                    if (existingProduct == null) {
                        log("Product not found with ID: " + product.getId());
                        return "false";
                    }
                    
                    // Update product
                    productService.update(product);
                    
                    log("Product updated successfully");
                    return "true";
                } catch (Exception e) {
                    log("Error updating product: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("DELETE_PRODUCT", (args, out) -> {
                log("Handling DELETE_PRODUCT request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse product ID
                    Long productId = Long.parseLong(args[1]);
                    log("Deleting product with ID: " + productId);
                    
                    // Get ProductService from context
                    ProductService productService = new ProductService(entityManager);
                    
                    // Check if product exists
                    Product existingProduct = productService.findById(productId);
                    if (existingProduct == null) {
                        log("Product not found with ID: " + productId);
                        return "false";
                    }
                    
                    // Delete product
                    productService.delete(productId);
                    log("Product deleted successfully");
                    return "true";
                } catch (Exception e) {
                    log("Error deleting product: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("GET_PRODUCT_BY_ID", (args, out) -> {
                log("Handling GET_PRODUCT_BY_ID request");
                if (args.length < 2) return "null";
                int id = Integer.parseInt(args[1]);
                return "{\"id\":"+id+",\"name\":\"Product "+id+"\",\"description\":\"Description for product "+id+"\",\"price\":29.99,\"quantity\":75}";
            });
            
            // User related commands
            commands.put("LOGIN", (args, out) -> {
                log("Handling LOGIN request");
                if (args.length < 3) {
                    return "null"; // Invalid request format
                }
                
                String email = args[1];
                String password = args[2];
                log("Login attempt for: " + email);
                
                try {
                    // Get UserService from context
                    UserService userService = new UserService(entityManager);
                    User user = userService.login(email, password);
                    
                    if (user != null) {
                        log("Login successful for: " + email);
                        // Convert user to JSON using custom adapter
                        Gson gson = new GsonBuilder()
                            .registerTypeAdapter(User.class, new UserAdapter())
                            .create();
                        return gson.toJson(user);
                    } else {
                        log("Login failed for: " + email);
                        return "null"; // Authentication failed
                    }
                } catch (Exception e) {
                    log("Error during login: " + e.getMessage());
                    e.printStackTrace();
                    return "null"; // Error occurred
                }
            });
            
            commands.put("REGISTER", (args, out) -> {
                log("Handling REGISTER request");
                if (args.length < 6) {
                    log("Invalid registration format. Expected at least 6 parts.");
                    return "false"; // Invalid request format
                }
                
                try {
                    // Get user data directly from command parts instead of JSON
                    String email = args[1];
                    String password = args[2];
                    String fullName = args[3];
                    String phone = args[4];
                    String address = args[5];
                    
                    log("Registration attempt for: " + email);
                    
                    // Create user object directly
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setFullName(fullName);
                    user.setPhone(phone);
                    user.setAddress(address);
                    
                    // Get UserService and RoleService from context
                    UserService userService = new UserService(entityManager);
                    RoleService roleService = new RoleService(entityManager);
                    
                    // Get default staff role
                    Role staffRole = roleService.findByName("STAFF");
                    if (staffRole == null) {
                        // Create staff role if it doesn't exist
                        staffRole = new Role();
                        staffRole.setName("STAFF");
                        roleService.save(staffRole);
                    }
                    
                    // Check if user already exists
                    User existingUser = userService.findByEmail(user.getEmail());
                    if (existingUser != null) {
                        log("Registration failed for: " + user.getEmail() + " (email already exists)");
                        return "false"; // User already exists
                    }
                    
                    // Start transaction
                    EntityTransaction transaction = entityManager.getTransaction();
                    try {
                        transaction.begin();
                        
                        // Set role and timestamps
                        user.setRole(staffRole);
                        user.setCreatedAt(new Date());
                        user.setUpdatedAt(new Date());
                        
                        // Save user
                        entityManager.persist(user);
                        transaction.commit();
                        
                        log("Registration successful for: " + user.getEmail());
                        return "true";
                    } catch (Exception e) {
                        if (transaction.isActive()) {
                            transaction.rollback();
                        }
                        log("Error during registration transaction: " + e.getMessage());
                        e.printStackTrace();
                        return "false";
                    }
                } catch (Exception e) {
                    log("Error during registration: " + e.getMessage());
                    e.printStackTrace();
                    return "null"; // Error occurred
                }
            });

            // Search related commands
            commands.put("SEARCH_PRODUCTS", (args, out) -> {
                log("Handling SEARCH_PRODUCTS request");
                if (args.length < 2) {
                    return "[]"; // Invalid request format
                }
                
                String searchTerm = args[1];
                log("Searching for products with term: " + searchTerm);
                
                try {
                    // Get ProductService from context
                    ProductService productService = new ProductService(entityManager);
                    
                    // Search for products by name
                    List<Product> products = productService.findByName(searchTerm);
                    
                    // Convert to JSON and return
                    return new Gson().toJson(products);
                } catch (Exception e) {
                    log("Error searching products: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            commands.put("SEARCH_USERS", (args, out) -> {
                log("Handling SEARCH_USERS request");
                if (args.length < 2) {
                    return "[]"; // Invalid request format
                }
                
                String searchTerm = args[1];
                log("Searching for users with term: " + searchTerm);
                
                try {
                    // Get UserService from context
                    UserService userService = new UserService(entityManager);
                    
                    // Get all users and filter by email or name containing search term
                    List<User> allUsers = userService.findAll();
                    List<User> filteredUsers = new ArrayList<>();
                    
                    for (User user : allUsers) {
                        // Check if email or name contains search term (case insensitive)
                        if (user.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) || 
                            (user.getFullName() != null && user.getFullName().toLowerCase().contains(searchTerm.toLowerCase()))) {
                            // Create a copy without password for security
                            filteredUsers.add(user.withoutPassword());
                        }
                    }
                    
                    // Convert to JSON and return
                    return new Gson().toJson(filteredUsers);
                } catch (Exception e) {
                    log("Error searching users: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            commands.put("SEARCH_CUSTOMERS", (args, out) -> {
                log("Handling SEARCH_CUSTOMERS request");
                if (args.length < 2) {
                    return "[]"; // Invalid request format
                }
                
                String searchTerm = args[1];
                log("Searching for customers with term: " + searchTerm);
                
                try {
                    // Get CustomerService from context
                    CustomerService customerService = new CustomerService(entityManager);
                    
                    // Get all customers and filter by name, email, or phone containing search term
                    List<Customer> allCustomers = customerService.findAll();
                    List<Customer> filteredCustomers = new ArrayList<>();
                    
                    for (Customer customer : allCustomers) {
                        // Check if name, email or phone contains search term (case insensitive)
                        if ((customer.getName() != null && customer.getName().toLowerCase().contains(searchTerm.toLowerCase())) ||
                            (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(searchTerm.toLowerCase())) ||
                            (customer.getPhone() != null && customer.getPhone().contains(searchTerm))) {
                            filteredCustomers.add(customer);
                        }
                    }
                    
                    // Convert to JSON and return
                    return new Gson().toJson(filteredCustomers);
                } catch (Exception e) {
                    log("Error searching customers: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            commands.put("GET_CUSTOMERS", (args, out) -> {
                log("Handling GET_CUSTOMERS request");
                try {
                    // Get CustomerService from context
                    CustomerService customerService = new CustomerService(entityManager);
                    
                    // Get all customers
                    List<Customer> customers = customerService.findAll();
                    
                    // Convert to JSON and return
                    return new Gson().toJson(customers);
                } catch (Exception e) {
                    log("Error getting customers: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            commands.put("ADD_CUSTOMER", (args, out) -> {
                log("Handling ADD_CUSTOMER request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse customer data from JSON
                    String customerJson = args[1];
                    Customer customer = new Gson().fromJson(customerJson, Customer.class);
                    log("Adding customer: " + customer.getName());
                    
                    // Get CustomerService from context
                    CustomerService customerService = new CustomerService(entityManager);
                    
                    // Save customer
                    EntityTransaction transaction = entityManager.getTransaction();
                    try {
                        transaction.begin();
                        customerService.save(customer);
                        transaction.commit();
                        log("Customer added successfully");
                        return "true";
                    } catch (Exception e) {
                        if (transaction.isActive()) {
                            transaction.rollback();
                        }
                        throw e;
                    }
                } catch (Exception e) {
                    log("Error adding customer: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("UPDATE_CUSTOMER", (args, out) -> {
                log("Handling UPDATE_CUSTOMER request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse customer data from JSON
                    String customerJson = args[1];
                    Customer customer = new Gson().fromJson(customerJson, Customer.class);
                    log("Updating customer with ID: " + customer.getId());
                    
                    // Get CustomerService from context
                    CustomerService customerService = new CustomerService(entityManager);
                    
                    // Check if customer exists
                    Customer existingCustomer = customerService.findById(customer.getId());
                    if (existingCustomer == null) {
                        log("Customer not found with ID: " + customer.getId());
                        return "false";
                    }
                    
                    // Update customer
                    EntityTransaction transaction = entityManager.getTransaction();
                    try {
                        transaction.begin();
                        customerService.update(customer);
                        transaction.commit();
                        log("Customer updated successfully");
                        return "true";
                    } catch (Exception e) {
                        if (transaction.isActive()) {
                            transaction.rollback();
                        }
                        throw e;
                    }
                } catch (Exception e) {
                    log("Error updating customer: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("DELETE_CUSTOMER", (args, out) -> {
                log("Handling DELETE_CUSTOMER request");
                if (args.length < 2) {
                    return "false"; // Invalid request format
                }
                
                try {
                    // Parse customer ID
                    Long customerId = Long.parseLong(args[1]);
                    log("Deleting customer with ID: " + customerId);
                    
                    // Get CustomerService from context
                    CustomerService customerService = new CustomerService(entityManager);
                    
                    // Check if customer exists
                    Customer existingCustomer = customerService.findById(customerId);
                    if (existingCustomer == null) {
                        log("Customer not found with ID: " + customerId);
                        return "false";
                    }
                    
                    // Delete customer
                    EntityTransaction transaction = entityManager.getTransaction();
                    try {
                        transaction.begin();
                        customerService.delete(customerId);
                        transaction.commit();
                        log("Customer deleted successfully");
                        return "true";
                    } catch (Exception e) {
                        if (transaction.isActive()) {
                            transaction.rollback();
                        }
                        throw e;
                    }
                } catch (Exception e) {
                    log("Error deleting customer: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            // Order related commands
            commands.put("PLACE_ORDER", (args, out) -> {
                log("Handling PLACE_ORDER request");
                if (args.length < 5) {
                    log("Invalid order format. Expected at least 5 parts.");
                    return "false"; // Invalid request format
                }
                
                try {
                    // Get order data directly from command parts instead of JSON
                    String customerName = args[1];
                    String customerPhone = args[2];
                    String customerAddress = args[3];
                    String itemsData = args[4]; // Format: productId,productName,price,quantity;productId,productName,price,quantity;...
                    
                    log("Received order from: " + customerName);
                    
                    // Get services from context
                    OrderService orderService = new OrderService(entityManager);
                    
                    // Create server-side order entity
                    Order order = new Order();
                    order.setStatus("NEW");
                    order.setOrderDate(new Date());
                    
                    // Find or create customer
                    Customer customer = null;
                    try {
                        // Try to find existing customer by phone
                        if (customerPhone != null && !customerPhone.isEmpty()) {
                            customer = entityManager.createQuery("SELECT c FROM Customer c WHERE c.phone = :phone", Customer.class)
                                .setParameter("phone", customerPhone)
                                .getSingleResult();
                        }
                    } catch (Exception e) {
                        // Customer not found, will create new one
                    }
                    
                    if (customer == null) {
                        // Create new customer
                        customer = new Customer();
                        customer.setName(customerName);
                        customer.setPhone(customerPhone);
                        customer.setAddress(customerAddress);
                        
                        // Set email (required field) - use phone as default if not provided
                        String email = customerPhone + "@example.com";
                        customer.setEmail(email);
                        
                        // Set default password for guest customers
                        customer.setPassword("guest123");
                        
                        // Set default role for customers
                        Role customerRole = null;
                        try {
                            customerRole = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                                .setParameter("name", "CUSTOMER")
                                .getSingleResult();
                        } catch (Exception e) {
                            // Create customer role if it doesn't exist
                            customerRole = new Role();
                            customerRole.setName("CUSTOMER");
                            
                            EntityTransaction roleTx = entityManager.getTransaction();
                            try {
                                roleTx.begin();
                                entityManager.persist(customerRole);
                                roleTx.commit();
                            } catch (Exception ex) {
                                if (roleTx.isActive()) {
                                    roleTx.rollback();
                                }
                                log("Error creating customer role: " + ex.getMessage());
                            }
                        }
                        
                        customer.setRole(customerRole);
                        
                        // Save customer first
                        EntityTransaction transaction = entityManager.getTransaction();
                        try {
                            transaction.begin();
                            entityManager.persist(customer);
                            transaction.commit();
                        } catch (Exception e) {
                            if (transaction.isActive()) {
                                transaction.rollback();
                            }
                            throw e;
                        }
                    }
                    
                    // Set customer for order
                    order.setCustomer(customer);
                    
                    // Process order items
                    if (itemsData != null && !itemsData.isEmpty()) {
                        String[] items = itemsData.split(";");
                        
                        for (String item : items) {
                            String[] itemParts = item.split(",");
                            if (itemParts.length >= 4) {
                                OrderItem orderItem = new OrderItem();
                                
                                // Get product info
                                int productId = Integer.parseInt(itemParts[0]);
                                String productName = itemParts[1];
                                double price = Double.parseDouble(itemParts[2]);
                                int quantity = Integer.parseInt(itemParts[3]);
                                
                                // Find or create product
                                Product product = null;
                                try {
                                    product = entityManager.find(Product.class, (long) productId);
                                } catch (Exception e) {
                                    // Product not found
                                }
                                
                                if (product == null) {
                                    // Create temporary product if not found
                                    product = new Product();
                                    product.setName(productName);
                                    product.setPrice(new java.math.BigDecimal(price));
                                    product.setQuantity(100); // Default quantity
                                    
                                    // Save product
                                    EntityTransaction transaction = entityManager.getTransaction();
                                    try {
                                        transaction.begin();
                                        entityManager.persist(product);
                                        transaction.commit();
                                    } catch (Exception e) {
                                        if (transaction.isActive()) {
                                            transaction.rollback();
                                        }
                                        throw e;
                                    }
                                }
                                
                                // Set order item properties
                                orderItem.setProduct(product);
                                orderItem.setQuantity(quantity);
                                orderItem.setPrice(price);
                                orderItem.setOrder(order);
                                
                                // Add item to order
                                order.addItem(orderItem);
                            }
                        }
                    }
                    
                    // Save order to database
                    orderService.save(order);
                    
                    log("Order saved successfully");
                    return "true";
                } catch (Exception e) {
                    log("Error saving order: " + e.getMessage());
                    e.printStackTrace();
                    return "false"; // Error occurred
                }
            });
            
            commands.put("GET_ORDER_HISTORY", (args, out) -> {
                log("Handling GET_ORDER_HISTORY request");
                
                try {
                    // Get OrderService from context
                    OrderService orderService = new OrderService(entityManager);
                    
                    // Get all orders
                    List<Order> orders = orderService.findAll();
                    
                    // Convert to JSON and return
                    return new Gson().toJson(orders);
                } catch (Exception e) {
                    log("Error getting order history: " + e.getMessage());
                    e.printStackTrace();
                    return "[]"; // Return empty array on error
                }
            });
            
            // Lệnh CLOSE để ngắt kết nối client
            commands.put("CLOSE", (args, out) -> {
                closeConnection();
                return "Đang đóng kết nối...";
            });

        } catch (IOException e) {
            log("Lỗi khi tạo luồng dữ liệu: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            log("Client đã kết nối: " + clientSocket.getInetAddress().getHostAddress());

            String request;
            while (isRunning && (request = reader.readLine()) != null) {
                log("Nhận từ client: " + request);
                String response = handleRequest(request);
                writer.println(response);
            }
            log("Client đã đóng kết nối.");
        } catch (IOException e) {
            log("Lỗi giao tiếp với client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private String handleRequest(String request) {
        log("Processing request: " + request);
        
        // Tách chuỗi lệnh theo ký tự ':' (client uses this format)
        String[] parts = request.split(":");
        String command = parts[0];
        
        log("Command: " + command + ", Parts: " + parts.length);
        
        // Kiểm tra lệnh có tồn tại trong danh sách không
        String response = commands.getOrDefault(command, (args, out) -> "Lệnh không hợp lệ!").apply(parts, writer);
        log("Sending response: " + response);
        return response;
    }

    public void closeConnection() {
        isRunning = false;
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            log("Client đã ngắt kết nối.");
        } catch (IOException e) {
            log("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}
