package org.client.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.client.entities.*;
import org.client.services.ClientService;
import org.client.utils.SocketClient;

import java.lang.reflect.Type;
import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final SocketClient socketClient;
    private final Gson gson;

    public ClientServiceImpl(String host, int port) {
        this.socketClient = new SocketClient(host, port);
        this.gson = new Gson();
    }

    // Product endpoints
    @Override
    public List<Product> getAllProducts() {
        String response = socketClient.sendRequest("GET_PRODUCTS");
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        return gson.fromJson(response, productListType);
    }

    @Override
    public Product getProductById(int id) {
        String response = socketClient.sendRequest("GET_PRODUCT_BY_ID:" + id);
        return gson.fromJson(response, Product.class);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        String response = socketClient.sendRequest("SEARCH_PRODUCTS:" + keyword);
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        return gson.fromJson(response, productListType);
    }

    // Cart endpoints
    @Override
    public boolean addToCart(CartItem item) {
        String itemJson = gson.toJson(item);
        String response = socketClient.sendRequest("ADD_TO_CART:" + itemJson);
        return Boolean.parseBoolean(response);
    }

    @Override
    public boolean removeFromCart(int productId) {
        String response = socketClient.sendRequest("REMOVE_FROM_CART:" + productId);
        return Boolean.parseBoolean(response);
    }

    @Override
    public boolean updateCartItem(int productId, int quantity) {
        String response = socketClient.sendRequest("UPDATE_CART_ITEM:" + productId + ":" + quantity);
        return Boolean.parseBoolean(response);
    }

    @Override
    public List<CartItem> getCartItems() {
        String response = socketClient.sendRequest("GET_CART_ITEMS");
        Type cartItemListType = new TypeToken<List<CartItem>>(){}.getType();
        return gson.fromJson(response, cartItemListType);
    }

    @Override
    public boolean clearCart() {
        String response = socketClient.sendRequest("CLEAR_CART");
        return Boolean.parseBoolean(response);
    }

    // Order endpoints
    @Override
    public boolean placeOrder(Order order) {
        // Thay vì gửi JSON, gửi các trường riêng biệt được phân tách bằng dấu ':'
        StringBuilder itemsData = new StringBuilder();
        
        // Format các mặt hàng: productId,productName,price,quantity;productId,productName,price,quantity;...
        if (order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);
                if (i > 0) {
                    itemsData.append(";");
                }
                itemsData.append(item.getProductId())
                       .append(",")
                       .append(item.getProductName())
                       .append(",")
                       .append(item.getPrice())
                       .append(",")
                       .append(item.getQuantity());
            }
        }
        
        // Gửi yêu cầu với định dạng: PLACE_ORDER:customerName:customerPhone:customerAddress:itemsData
        String request = String.format("PLACE_ORDER:%s:%s:%s:%s", 
            order.getCustomerName(), 
            order.getCustomerPhone(), 
            order.getCustomerAddress(),
            itemsData.toString());
        
        System.out.println("Sending order request: " + request);
        String response = socketClient.sendRequest(request);
        return Boolean.parseBoolean(response);
    }

    @Override
    public Order getOrderStatus(int orderId) {
        String response = socketClient.sendRequest("GET_ORDER_STATUS:" + orderId);
        return gson.fromJson(response, Order.class);
    }

    @Override
    public List<Order> getOrderHistory() {
        String response = socketClient.sendRequest("GET_ORDER_HISTORY");
        Type orderListType = new TypeToken<List<Order>>(){}.getType();
        return gson.fromJson(response, orderListType);
    }

    @Override
    public boolean cancelOrder(int orderId) {
        String response = socketClient.sendRequest("CANCEL_ORDER:" + orderId);
        return Boolean.parseBoolean(response);
    }

    // User endpoints
    @Override
    public boolean register(User user) {
        // Thay vì gửi JSON, gửi các trường riêng biệt được phân tách bằng dấu ':'
        String request = String.format("REGISTER:%s:%s:%s:%s:%s", 
            user.getEmail(), 
            user.getPassword(), 
            user.getFullName(), 
            user.getPhone(), 
            user.getAddress());
        
        System.out.println("Sending registration request: " + request);
        String response = socketClient.sendRequest(request);
        return Boolean.parseBoolean(response);
    }

    @Override
    public User login(String username, String password) {
        String response = socketClient.sendRequest("LOGIN:" + username + ":" + password);
        return gson.fromJson(response, User.class);
    }

    @Override
    public User getUserInfo() {
        String response = socketClient.sendRequest("GET_USER_INFO");
        return gson.fromJson(response, User.class);
    }

    @Override
    public boolean updateUserInfo(User user) {
        String userJson = gson.toJson(user);
        String response = socketClient.sendRequest("UPDATE_USER_INFO:" + userJson);
        return Boolean.parseBoolean(response);
    }

    // Search endpoints
    @Override
    public List<User> searchUsers(String keyword) {
        String response = socketClient.sendRequest("SEARCH_USERS:" + keyword);
        Type userListType = new TypeToken<List<User>>(){}.getType();
        return gson.fromJson(response, userListType);
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        String response = socketClient.sendRequest("SEARCH_CUSTOMERS:" + keyword);
        Type customerListType = new TypeToken<List<Customer>>(){}.getType();
        return gson.fromJson(response, customerListType);
    }
}