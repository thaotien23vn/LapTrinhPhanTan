package org.client.ui;

import org.client.entities.User;
import org.client.services.ClientService;
import org.client.services.impl.MockClientServiceImpl;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class StaffPOSFrame extends JFrame {
    // Main panels
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JPanel productPanel;
    private JPanel cartPanel;
    private JPanel accountPanel;
    private JPanel helpPanel;
    
    // Product panel components
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField searchField;
    private JPanel productDetailPanel;
    private JLabel productNameLabel;
    private JLabel productPriceLabel;
    private JLabel productTypeLabel;
    private JLabel productBrandLabel;
    private JLabel productQuantityLabel;
    private JTextArea productDescriptionArea;
    private JTextField quantityField;
    private JButton addToCartButton;
    
    // Cart panel components
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalPriceLabel;
    private JButton checkoutButton;
    private JButton removeFromCartButton;
    private JButton clearCartButton;
    
    // Client service for backend operations
    private ClientService clientService;
    
    /**
     * Create the frame.
     * @param clientService The client service for backend operations
     */
    public StaffPOSFrame(ClientService clientService) {
        this.clientService = clientService;
        setTitle("Trang chủ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1024, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        initComponents();
        loadSampleProducts();
    }
    
    /**
     * Initialize all UI components.
     */
    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        // Create tab pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        // Initialize panels
        initProductPanel();
        initCartPanel();
        initAccountPanel();
        initHelpPanel();
        
        // Add panels to tab pane
        tabbedPane.addTab("Tài khoản", null, accountPanel, null);
        tabbedPane.addTab("Giỏ hàng", null, cartPanel, null);
        tabbedPane.addTab("Trợ giúp", null, helpPanel, null);
        
        // Add main product panel with store name
        JPanel storePanel = new JPanel(new BorderLayout());
        JLabel storeLabel = new JLabel("Cửa hàng quần áo");
        storeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        storeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel storeSubLabel = new JLabel("Xin chào, minh");
        storeSubLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(storeLabel, BorderLayout.WEST);
        headerPanel.add(storeSubLabel, BorderLayout.EAST);
        
        storePanel.add(headerPanel, BorderLayout.NORTH);
        storePanel.add(productPanel, BorderLayout.CENTER);
        
        tabbedPane.insertTab("Trang chủ", null, storePanel, null, 0);
        tabbedPane.setSelectedIndex(0);
    }
    
    /**
     * Initialize the product panel with listing and details.
     */
    private void initProductPanel() {
        productPanel = new JPanel(new BorderLayout(10, 10));
        productPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setToolTipText("Nhập tên sản phẩm để tìm kiếm");
        
        // Thêm phím Enter để tìm kiếm
        searchField.addActionListener(e -> searchProducts());
        
        JButton searchButton = new JButton("Tìm");
        searchButton.addActionListener(e -> searchProducts());
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        productPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Product list panel (left side)
        JPanel productListPanel = new JPanel(new BorderLayout());
        productListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        
        // Table for products
        String[] productColumns = {"Tên sản phẩm", "Giá"};
        productTableModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(productTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setReorderingAllowed(false);
        
        // Add mouse listener to show product details
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    showProductDetails(selectedRow);
                }
            }
        });
        
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productListPanel.add(productScrollPane, BorderLayout.CENTER);
        
        // Product details panel (right side)
        productDetailPanel = new JPanel();
        productDetailPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));
        productDetailPanel.setLayout(new BoxLayout(productDetailPanel, BoxLayout.Y_AXIS));
        
        // Add product detail components
        productNameLabel = new JLabel("", SwingConstants.LEFT);
        productNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        productPriceLabel = new JLabel("Giá: ");
        productTypeLabel = new JLabel("Danh mục: ");
        productBrandLabel = new JLabel("Thương hiệu: ");
        productQuantityLabel = new JLabel("Tồn kho: ");
        
        JLabel descriptionLabel = new JLabel("Mô tả: ");
        productDescriptionArea = new JTextArea(5, 20);
        productDescriptionArea.setLineWrap(true);
        productDescriptionArea.setWrapStyleWord(true);
        productDescriptionArea.setEditable(false);
        JScrollPane descScrollPane = new JScrollPane(productDescriptionArea);
        
        // Add to cart panel
        JPanel addToCartPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        addToCartPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel quantityLabel = new JLabel("Số lượng: ");
        quantityField = new JTextField("1");
        
        addToCartButton = new JButton("Thêm vào giỏ hàng");
        addToCartButton.addActionListener(e -> addToCart());
        
        addToCartPanel.add(quantityLabel);
        addToCartPanel.add(quantityField);
        addToCartPanel.add(new JLabel("")); // Empty cell for spacing
        addToCartPanel.add(addToCartButton);
        
        // Add components to detail panel
        productDetailPanel.add(productNameLabel);
        productDetailPanel.add(Box.createVerticalStrut(10));
        productDetailPanel.add(productPriceLabel);
        productDetailPanel.add(productTypeLabel);
        productDetailPanel.add(productBrandLabel);
        productDetailPanel.add(productQuantityLabel);
        productDetailPanel.add(Box.createVerticalStrut(10));
        productDetailPanel.add(descriptionLabel);
        productDetailPanel.add(descScrollPane);
        productDetailPanel.add(addToCartPanel);
        
        // Split pane for product list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                             productListPanel, productDetailPanel);
        splitPane.setResizeWeight(0.6);
        productPanel.add(splitPane, BorderLayout.CENTER);
        
        // Set initial button states
        addToCartButton.setEnabled(false);
    }
    
    /**
     * Initialize the cart panel.
     */
    private void initCartPanel() {
        cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create tabs for cart
        JTabbedPane cartTabbedPane = new JTabbedPane();
        
        // Cart items panel
        JPanel cartItemsPanel = new JPanel(new BorderLayout());
        
        // Table for cart items
        String[] cartColumns = {"Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartItemsPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        // Cart actions panel
        JPanel cartActionsPanel = new JPanel(new BorderLayout());
        
        // Total price panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPriceLabel = new JLabel("Tổng tiền: 0 VNĐ");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalPriceLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        removeFromCartButton = new JButton("Xóa sản phẩm");
        removeFromCartButton.addActionListener(e -> removeFromCart());
        
        clearCartButton = new JButton("Xóa giỏ hàng");
        clearCartButton.addActionListener(e -> clearCart());
        
        checkoutButton = new JButton("Thanh toán");
        checkoutButton.addActionListener(e -> checkout());
        
        buttonsPanel.add(removeFromCartButton);
        buttonsPanel.add(clearCartButton);
        buttonsPanel.add(checkoutButton);
        
        cartActionsPanel.add(totalPanel, BorderLayout.NORTH);
        cartActionsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        cartItemsPanel.add(cartActionsPanel, BorderLayout.SOUTH);
        
        // Add cart items panel to tab
        cartTabbedPane.addTab("Sản phẩm", null, cartItemsPanel, null);
        cartTabbedPane.addTab("Đơn giá", null, new JPanel(), null);
        cartTabbedPane.addTab("Số lượng", null, new JPanel(), null);
        cartTabbedPane.addTab("Thành tiền", null, new JPanel(), null);
        cartTabbedPane.addTab("Thao tác", null, new JPanel(), null);
        
        // Add empty panel message to be shown when cart is empty
        JPanel emptyCartPanel = new JPanel(new BorderLayout());
        JLabel emptyCartLabel = new JLabel("Không có sản phẩm trong giỏ hàng", SwingConstants.CENTER);
        emptyCartLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emptyCartPanel.add(emptyCartLabel, BorderLayout.CENTER);
        
        // Add tab pane to cart panel
        cartPanel.add(cartTabbedPane, BorderLayout.CENTER);
        
        // Update button states
        updateCartButtonStates();
    }
    
    /**
     * Initialize the account panel.
     */
    private void initAccountPanel() {
        accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("THÔNG TIN TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Avatar panel
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(120, 120));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        // Placeholder for avatar (can be replaced with actual image)
        avatarLabel.setText("📷");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        avatarPanel.add(avatarLabel);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(avatarPanel, BorderLayout.CENTER);
        
        // Main info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin cá nhân"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Giá trị mặc định khi không có thông tin từ service
        String fullName = "Chưa có thông tin"; 
        String email = "Chưa có thông tin";
        String phone = "Chưa có thông tin";
        String address = "Chưa có thông tin";
        String role = "Nhân viên bán hàng";
        String joinDate = "Chưa có thông tin";
        
        // Lấy thông tin thực tế từ clientService
        try {
            if (clientService != null && clientService.getCurrentUser() != null) {
                User currentUser = clientService.getCurrentUser();
                
                // Lấy họ tên
                if (currentUser.getFullName() != null && !currentUser.getFullName().isEmpty()) {
                    fullName = currentUser.getFullName();
                }
                
                // Lấy email
                if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                    email = currentUser.getEmail();
                }
                
                // Lấy số điện thoại thực tế từ User
                if (currentUser.getPhone() != null && !currentUser.getPhone().isEmpty()) {
                    phone = currentUser.getPhone();
                }
                
                // Lấy địa chỉ thực tế từ User
                if (currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
                    address = currentUser.getAddress();
                }
                
                // Định dạng ngày tạo tài khoản
                if (currentUser.getCreatedDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    joinDate = dateFormat.format(currentUser.getCreatedDate());
                }
            }
        } catch (Exception e) {
            // Sử dụng giá trị mặc định nếu có lỗi
            System.out.println("Không thể lấy thông tin người dùng: " + e.getMessage());
        }
        
        // Setup GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 1: Full Name
        addLabelAndField(infoPanel, gbc, 0, "Họ và tên:", fullName);
        
        // Row 2: Email
        addLabelAndField(infoPanel, gbc, 1, "Email:", email);
        
        // Row 3: Phone
        addLabelAndField(infoPanel, gbc, 2, "Số điện thoại:", phone);
        
        // Row 4: Address
        addLabelAndField(infoPanel, gbc, 3, "Địa chỉ:", address);
        
        // Row 5: Role
        addLabelAndField(infoPanel, gbc, 4, "Vai trò:", role);
        
        // Row 6: Join Date
        addLabelAndField(infoPanel, gbc, 5, "Ngày tham gia:", joinDate);
        
        // Security panel
        JPanel securityPanel = new JPanel(new GridBagLayout());
        securityPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Bảo mật"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Change password button
        JButton changePasswordButton = new JButton("Đổi mật khẩu");
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        securityPanel.add(changePasswordButton, gbc);
        
        // Action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> logout());
        
        JButton editProfileButton = new JButton("Cập nhật thông tin");
        editProfileButton.setBackground(new Color(40, 167, 69));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.addActionListener(e -> editProfile());
        
        actionPanel.add(editProfileButton);
        actionPanel.add(logoutButton);
        
        // Main content panel to hold everything
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Add components to content panel
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(securityPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(actionPanel);
        
        // Add all panels to main account panel
        accountPanel.add(headerPanel, BorderLayout.NORTH);
        accountPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Helper method to add a label and field to a GridBagLayout panel
     */
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String fieldText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        
        JLabel field = new JLabel(fieldText);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }
    
    /**
     * Show dialog to change password
     */
    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        JPasswordField currentPassword = new JPasswordField();
        JPasswordField newPassword = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        
        panel.add(new JLabel("Mật khẩu hiện tại:"));
        panel.add(currentPassword);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(newPassword);
        panel.add(new JLabel("Xác nhận mật khẩu mới:"));
        panel.add(confirmPassword);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Đổi mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassword.getPassword());
            String newPass = new String(newPassword.getPassword());
            String confirmPass = new String(confirmPassword.getPassword());
            
            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // In a real app, call clientService to change password
            // For demo purposes, just show success
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Log out of the application
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Sử dụng ApplicationManager để quay lại màn hình đăng nhập
            org.client.app.ApplicationManager.getInstance().showLoginScreen();
            this.dispose();
        }
    }
    
    /**
     * Edit user profile
     */
    private void editProfile() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JTextField nameField = new JTextField("Minh - Staff");
        JTextField phoneField = new JTextField("0123456789");
        JTextField addressField = new JTextField("123 Đường Lê Lợi, Quận 1, TP.HCM");
        
        panel.add(new JLabel("Họ và tên:"));
        panel.add(nameField);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(phoneField);
        panel.add(new JLabel("Địa chỉ:"));
        panel.add(addressField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Cập nhật thông tin",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            // In a real app, call clientService to update user info
            // For demo purposes, just show success
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // Refresh account panel to show updated info
            accountPanel.removeAll();
            initAccountPanel();
            accountPanel.revalidate();
            accountPanel.repaint();
        }
    }
    
    /**
     * Initialize the help panel.
     */
    private void initHelpPanel() {
        helpPanel = new JPanel(new BorderLayout());
        helpPanel.add(new JLabel("Thông tin trợ giúp sẽ được hiển thị ở đây.", SwingConstants.CENTER));
    }
    
    /**
     * Tìm kiếm sản phẩm dựa trên từ khóa
     */
    private void searchProducts() {
        String keyword = searchField.getText().trim();
        
        if (keyword.isEmpty()) {
            // Nếu không có từ khóa, hiển thị tất cả sản phẩm
            loadSampleProducts();
            return;
        }
        
        // Chuyển từ khóa về chữ thường cho tìm kiếm không phân biệt hoa/thường
        keyword = keyword.toLowerCase();
        
        // Xóa dữ liệu hiện tại
        productTableModel.setRowCount(0);
        
        // Mảng dữ liệu sản phẩm mẫu
        String[][] sampleProducts = {
            {"Áo thun basic", "150,000 đ"},
            {"Áo sơ mi trắng", "350,000 đ"},
            {"Quần jean skinny", "450,000 đ"},
            {"Váy liền thân", "450,000 đ"},
            {"Giày thể thao", "550,000 đ"}
        };
        
        // Lọc và thêm sản phẩm vào bảng
        boolean found = false;
        for (String[] product : sampleProducts) {
            // Tìm kiếm không phân biệt hoa thường
            if (product[0].toLowerCase().contains(keyword)) {
                addProductRow(product[0], product[1]);
                found = true;
            }
        }
        
        // Hiển thị thông báo nếu không tìm thấy sản phẩm nào
        if (!found) {
            JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy sản phẩm nào phù hợp với từ khóa '" + searchField.getText() + "'", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            // Hiển thị lại tất cả sản phẩm nếu không tìm thấy
            loadSampleProducts();
        }
        
        // Bỏ chọn sản phẩm trước đó và tắt nút thêm vào giỏ hàng
        productTable.clearSelection();
        addToCartButton.setEnabled(false);
    }
    
    /**
     * Load sample products into the product table.
     */
    private void loadSampleProducts() {
        // Clear existing data
        productTableModel.setRowCount(0);
        
        // Add sample products
        addProductRow("Áo thun basic", "150,000 đ");
        addProductRow("Áo sơ mi trắng", "350,000 đ");
        addProductRow("Quần jean skinny", "450,000 đ");
        addProductRow("Váy liền thân", "450,000 đ");
        addProductRow("Giày thể thao", "550,000 đ");
    }
    
    /**
     * Add a product row to the table.
     */
    private void addProductRow(String name, String price) {
        productTableModel.addRow(new Object[]{name, price});
    }
    
    /**
     * Show product details in the right panel.
     */
    private void showProductDetails(int row) {
        String productName = (String) productTableModel.getValueAt(row, 0);
        String productPrice = (String) productTableModel.getValueAt(row, 1);
        
        // Enable add to cart button
        addToCartButton.setEnabled(true);
        
        // Set product details
        productNameLabel.setText(productName);
        productPriceLabel.setText("Giá: " + productPrice);
        
        // Sample data based on product name
        if (productName.equals("Áo thun basic")) {
            productTypeLabel.setText("Danh mục: Áo sơ mi");
            productBrandLabel.setText("Thương hiệu: Nike");
            productQuantityLabel.setText("Tồn kho: 100");
            productDescriptionArea.setText("Áo thun cotton chất lượng cao");
        } else if (productName.equals("Áo sơ mi trắng")) {
            productTypeLabel.setText("Danh mục: Áo sơ mi");
            productBrandLabel.setText("Thương hiệu: Adidas");
            productQuantityLabel.setText("Tồn kho: 75");
            productDescriptionArea.setText("Áo sơ mi trắng thiết kế hiện đại");
        } else if (productName.equals("Quần jean skinny")) {
            productTypeLabel.setText("Danh mục: Quần");
            productBrandLabel.setText("Thương hiệu: Levi's");
            productQuantityLabel.setText("Tồn kho: 50");
            productDescriptionArea.setText("Quần jean skinny co giãn thoải mái");
        } else if (productName.equals("Váy liền thân")) {
            productTypeLabel.setText("Danh mục: Váy");
            productBrandLabel.setText("Thương hiệu: H&M");
            productQuantityLabel.setText("Tồn kho: 35");
            productDescriptionArea.setText("Váy liền thân thời trang mùa hè");
        } else if (productName.equals("Giày thể thao")) {
            productTypeLabel.setText("Danh mục: Giày");
            productBrandLabel.setText("Thương hiệu: Nike");
            productQuantityLabel.setText("Tồn kho: 40");
            productDescriptionArea.setText("Giày thể thao chống trơn trượt");
        }
    }
    
    /**
     * Add selected product to cart.
     */
    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để thêm vào giỏ hàng");
            return;
        }
        
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
                return;
            }
            
            String productName = (String) productTableModel.getValueAt(selectedRow, 0);
            String priceString = (String) productTableModel.getValueAt(selectedRow, 1);
            priceString = priceString.replace(",", "").replace("đ", "").trim();
            double price = Double.parseDouble(priceString);
            
            // Check if product already exists in cart
            boolean found = false;
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                if (cartTableModel.getValueAt(i, 0).equals(productName)) {
                    // Update quantity
                    int currentQty = Integer.parseInt(cartTableModel.getValueAt(i, 2).toString());
                    int newQty = currentQty + quantity;
                    cartTableModel.setValueAt(String.valueOf(newQty), i, 2);
                    
                    // Update total
                    double total = price * newQty;
                    cartTableModel.setValueAt(String.format("%,.0f đ", total), i, 3);
                    
                    found = true;
                    break;
                }
            }
            
            // Add new row if not found
            if (!found) {
                double total = price * quantity;
                cartTableModel.addRow(new Object[]{
                    productName,
                    String.format("%,.0f đ", price),
                    String.valueOf(quantity),
                    String.format("%,.0f đ", total)
                });
            }
            
            // Update total price
            updateTotalPrice();
            
            // Update button states
            updateCartButtonStates();
            
            // Switch to cart tab after adding
            tabbedPane.setSelectedIndex(1);
            
            JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào giỏ hàng!");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ");
        }
    }
    
    /**
     * Remove selected product from cart.
     */
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa khỏi giỏ hàng");
            return;
        }
        
        cartTableModel.removeRow(selectedRow);
        updateTotalPrice();
        updateCartButtonStates();
    }
    
    /**
     * Clear all items from cart.
     */
    private void clearCart() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đã trống");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartTableModel.setRowCount(0);
            updateTotalPrice();
            updateCartButtonStates();
        }
    }
    
    /**
     * Process checkout.
     */
    private void checkout() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống, vui lòng thêm sản phẩm trước khi thanh toán");
            return;
        }
        
        // Display order summary
        StringBuilder summary = new StringBuilder();
        summary.append("Chi tiết đơn hàng:\n\n");
        
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            summary.append(cartTableModel.getValueAt(i, 0))
                   .append(" x ")
                   .append(cartTableModel.getValueAt(i, 2))
                   .append(" = ")
                   .append(cartTableModel.getValueAt(i, 3))
                   .append("\n");
        }
        
        summary.append("\nTổng cộng: ").append(totalPriceLabel.getText().substring(11));
        
        int confirm = JOptionPane.showConfirmDialog(this,
                summary.toString() + "\n\nXác nhận thanh toán?",
                "Xác nhận đơn hàng", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Đơn hàng đã được xử lý thành công!");
            cartTableModel.setRowCount(0);
            updateTotalPrice();
            updateCartButtonStates();
        }
    }
    
    /**
     * Update the total price label.
     */
    private void updateTotalPrice() {
        double total = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String priceString = (String) cartTableModel.getValueAt(i, 3);
            priceString = priceString.replace(",", "").replace("đ", "").trim();
            total += Double.parseDouble(priceString);
        }
        
        totalPriceLabel.setText("Tổng tiền: " + String.format("%,.0f VNĐ", total));
    }
    
    /**
     * Update cart-related button states.
     */
    private void updateCartButtonStates() {
        boolean hasItems = cartTableModel.getRowCount() > 0;
        removeFromCartButton.setEnabled(hasItems);
        clearCartButton.setEnabled(hasItems);
        checkoutButton.setEnabled(hasItems);
    }
    


    /**
     * Launch the application for direct testing.
     * In production, this frame should be launched from the LoginFrame.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // For testing purposes only - normally this would come from LoginFrame
                    ClientService mockClientService = new MockClientServiceImpl();
                    StaffPOSFrame frame = new StaffPOSFrame(mockClientService);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
