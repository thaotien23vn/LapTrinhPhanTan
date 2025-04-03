package org.client.ui;

import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.client.entities.Order;
import org.client.entities.OrderItem;
import org.client.entities.Product;
import org.client.services.ClientService;

public class MainFrame extends JFrame {
    private final ClientService clientService;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton placeOrderButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    
    // Màu sắc chính cho giao diện - đồng bộ với LoginFrame
    private final Color PRIMARY_COLOR = new Color(199, 21, 63); // Hồng đậm
    private final Color BACKGROUND_COLOR = new Color(255, 248, 220); // Be nhạt
    private final Color ACCENT_COLOR = new Color(0, 123, 255); // Xanh dương
    private final Color TEXT_COLOR = new Color(51, 51, 51); // Xám đậm
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public MainFrame(ClientService clientService) {
        this.clientService = clientService;
        
        setTitle("CỬA HÀNG THỜI TRANG COLLABCREW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Tạo panel chính với layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tạo header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Tạo bảng sản phẩm với giao diện đẹp hơn
        JPanel productPanel = createProductPanel();
        
        // Tạo panel điều khiển bên phải
        JPanel controlPanel = createControlPanel();
        
        // Thêm các thành phần vào panel chính
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(productPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Thêm panel chính vào frame
        setContentPane(mainPanel);
        
        // Tải dữ liệu ban đầu
        refreshProducts();
    }

    /**
     * Tạo panel header chứa logo và thông tin chào mừng
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Tạo panel chứa logo và tên cửa hàng
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel storeNameLabel = new JLabel("CỬA HÀNG THỜI TRANG");
        storeNameLabel.setFont(HEADER_FONT);
        storeNameLabel.setForeground(PRIMARY_COLOR);
        
        JLabel brandLabel = new JLabel("COLLABCREW");
        brandLabel.setFont(HEADER_FONT);
        brandLabel.setForeground(PRIMARY_COLOR);
        
        logoPanel.add(storeNameLabel);
        logoPanel.add(Box.createHorizontalStrut(10));
        logoPanel.add(brandLabel);
        
        // Tạo panel chứa thông tin người dùng và nút đăng xuất
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(BACKGROUND_COLOR);
        
        welcomeLabel = new JLabel("Chào mừng, Khách hàng");
        welcomeLabel.setFont(REGULAR_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        
        logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(BUTTON_FONT);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(PRIMARY_COLOR);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(welcomeLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);
        
        // Thêm các panel vào header
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        // Thêm đường kẻ phân cách
        headerPanel.add(createSeparator(), BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    /**
     * Tạo panel chứa bảng sản phẩm
     */
    private JPanel createProductPanel() {
        JPanel productPanel = new JPanel(new BorderLayout(0, 10));
        productPanel.setBackground(BACKGROUND_COLOR);
        
        // Tiêu đề bảng sản phẩm
        JLabel titleLabel = new JLabel("DANH SÁCH SẢN PHẨM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Tạo bảng sản phẩm với giao diện đẹp hơn
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Mô tả", "Giá (VNĐ)", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setFont(REGULAR_FONT);
        productTable.setRowHeight(30);
        productTable.setIntercellSpacing(new Dimension(10, 5));
        productTable.setShowGrid(true);
        productTable.setGridColor(new Color(230, 230, 230));
        productTable.setSelectionBackground(new Color(240, 240, 240));
        productTable.setSelectionForeground(TEXT_COLOR);
        productTable.setFillsViewportHeight(true);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Tạo header cho bảng với màu sắc đẹp hơn
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Các cột có độ rộng khác nhau
        TableColumnModel columnModel = productTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // Mã SP
        columnModel.getColumn(1).setPreferredWidth(150); // Tên sản phẩm
        columnModel.getColumn(2).setPreferredWidth(300); // Mô tả
        columnModel.getColumn(3).setPreferredWidth(100); // Giá
        columnModel.getColumn(4).setPreferredWidth(80);  // Số lượng
        
        // Tạo cột giá hiển thị với định dạng tiền tệ
        DefaultTableCellRenderer priceCellRenderer = new DefaultTableCellRenderer() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = currencyFormat.format((Double) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        priceCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        columnModel.getColumn(3).setCellRenderer(priceCellRenderer);
        
        // Căn giữa cột số lượng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Thêm các thành phần vào panel sản phẩm
        productPanel.add(titleLabel, BorderLayout.NORTH);
        productPanel.add(scrollPane, BorderLayout.CENTER);
        
        return productPanel;
    }
    
    /**
     * Tạo panel điều khiển bên phải
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(55, 20, 0, 0));
        
        // Tạo nút đặt hàng
        placeOrderButton = new JButton("Đặt hàng");
        placeOrderButton.setFont(BUTTON_FONT);
        placeOrderButton.setBackground(PRIMARY_COLOR);
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setFocusPainted(false);
        placeOrderButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        placeOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeOrderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        placeOrderButton.setMaximumSize(new Dimension(200, 50));
        placeOrderButton.addActionListener(e -> showOrderDialog());
        
        // Tạo nút làm mới
        refreshButton = new JButton("Làm mới sản phẩm");
        refreshButton.setFont(BUTTON_FONT);
        refreshButton.setBackground(ACCENT_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setMaximumSize(new Dimension(200, 50));
        refreshButton.addActionListener(e -> refreshProducts());
        
        // Thêm các nút vào panel điều khiển
        controlPanel.add(placeOrderButton);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(refreshButton);
        
        return controlPanel;
    }
    
    /**
     * Tạo đường kẻ phân cách
     */
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        separator.setBackground(BACKGROUND_COLOR);
        return separator;
    }
    
    /**
     * Xử lý đăng xuất
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            // Đóng cửa sổ hiện tại và mở màn hình đăng nhập
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }

    /**
     * Làm mới danh sách sản phẩm
     */
    private void refreshProducts() {
        tableModel.setRowCount(0);
        List<Product> products = clientService.getAllProducts();
        if (products == null) {
            // Hiển thị thông báo lỗi trong bảng thay vì ném ngoại lệ
            tableModel.addRow(new Object[]{
                "", "Lỗi kết nối máy chủ", "Không thể lấy danh sách sản phẩm", "", ""
            });
            return;
        }
        
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
            });
        }
    }

    private void showOrderDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first");
            return;
        }

        // Get product details from the selected row
        String productName = (String) tableModel.getValueAt(selectedRow, 1);
        double price = 0;
        try {
            // Extract price from the table (assuming it's in column 2)
            String priceStr = (String) tableModel.getValueAt(selectedRow, 3);
            // Clean price string (remove currency symbols, commas, etc.)
            priceStr = priceStr.replaceAll("[^0-9.]", "");
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing product price", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create order form
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 10));
        panel.add(new JLabel("Product:"));
        panel.add(new JLabel(productName));
        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantitySpinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Place Order",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String customerName = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            int quantity = (Integer) quantitySpinner.getValue();
            
            if (customerName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all customer information", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Create order object
                Order order = new Order();
                
                // Set customer information
                order.setCustomerName(customerName);
                order.setCustomerAddress(address);
                order.setCustomerPhone(phone);
                
                // Calculate total amount
                double totalAmount = price * quantity;
                order.setTotalAmount(totalAmount);
                
                // Format the date as string
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order.setOrderDate(dateFormat.format(new Date()));
                
                // Create order item
                List<OrderItem> orderItems = new ArrayList<>();
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(selectedRow + 1); // Using row index + 1 as product ID
                orderItem.setProductName(productName);
                orderItem.setQuantity(quantity);
                orderItem.setPrice(price);
                orderItems.add(orderItem);
                
                order.setItems(orderItems);
                
                // Call service to place order
                boolean success = clientService.placeOrder(order);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Order placed successfully!");
                    refreshProducts(); // Refresh the product list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to place order. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // Start with the login frame instead of the main frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
} 