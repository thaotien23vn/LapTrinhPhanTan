package org.client.ui;

import org.client.services.ClientService;
import org.client.services.impl.MockClientServiceImpl;
import org.client.entities.User;
import org.client.entities.Order;
import org.client.entities.OrderItem;
import org.client.entities.CartItem;
import org.client.entities.Product;
import org.client.entities.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class StaffPOSFrame extends JFrame {
    // Main panels
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JPanel productPanel;
    private JPanel cartPanel;
    private JPanel accountPanel;
    private JPanel helpPanel;
    private JPanel searchPanel;
    
    // Màu sắc chính cho giao diện - đồng bộ với LoginFrame
    private final Color PRIMARY_COLOR = new Color(199, 21, 63); // Hồng đậm
    private final Color BACKGROUND_COLOR = new Color(255, 248, 220); // Be nhạt
    private final Color ACCENT_COLOR = new Color(0, 123, 255); // Xanh dương
    private final Color TEXT_COLOR = new Color(51, 51, 51); // Xám đậm
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    
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
    
    // Current logged-in user
    private User currentUser;
    
    /**
     * Create the frame.
     * @param clientService The client service for backend operations
     */
    public StaffPOSFrame(ClientService clientService) {
        this.clientService = clientService;
        setTitle("Trang chủ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Get current user information
        try {
            this.currentUser = clientService.getUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể lấy thông tin người dùng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        initComponents();
        loadSampleProducts();
    }
    
    /**
     * Initialize all UI components.
     */
    private void initComponents() {
        // Thiết lập giao diện chính với màu nền mới
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBackground(BACKGROUND_COLOR);
        setContentPane(contentPane);
        
        // Tạo tab pane với phong cách mới
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(REGULAR_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        
        // Tùy chỉnh giao diện tab
        UIManager.put("TabbedPane.selected", new Color(240, 240, 240));
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.focus", PRIMARY_COLOR);
        UIManager.put("TabbedPane.highlight", PRIMARY_COLOR);
        UIManager.put("TabbedPane.borderHightlightColor", PRIMARY_COLOR);
        UIManager.put("TabbedPane.selectedForeground", PRIMARY_COLOR);
        
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        // Khởi tạo các panel
        initProductPanel();
        initCartPanel();
        initAccountPanel();
        initHelpPanel();
        initSearchPanel();
        
        // Thêm các panel vào tab pane với icon
        tabbedPane.addTab("Tài khoản", null, accountPanel, "Quản lý tài khoản");
        tabbedPane.addTab("Giỏ hàng", null, cartPanel, "Xem giỏ hàng và thanh toán");
        tabbedPane.addTab("Tìm kiếm", null, searchPanel, "Tìm kiếm sản phẩm");
        tabbedPane.addTab("Trợ giúp", null, helpPanel, "Xem hướng dẫn sử dụng");
        
        // Tạo panel cửa hàng chính với thiết kế mới
        JPanel storePanel = new JPanel(new BorderLayout());
        storePanel.setBackground(BACKGROUND_COLOR);
        
        // Tạo header với logo cửa hàng
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel chứa tên cửa hàng
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel storeLabel = new JLabel("CỬA HÀNG THỜI TRANG");
        storeLabel.setFont(HEADER_FONT);
        storeLabel.setForeground(PRIMARY_COLOR);
        
        JLabel brandLabel = new JLabel("COLLABCREW");
        brandLabel.setFont(HEADER_FONT);
        brandLabel.setForeground(PRIMARY_COLOR);
        
        logoPanel.add(storeLabel);
        logoPanel.add(Box.createHorizontalStrut(10));
        logoPanel.add(brandLabel);
        
        // Panel chứa thông tin người dùng
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(BACKGROUND_COLOR);
        
        // Hiển thị lời chào với tên nhân viên
        String welcomeMessage = "Chào mừng";
        if (currentUser != null && currentUser.getFullName() != null && !currentUser.getFullName().isEmpty()) {
            welcomeMessage += ", " + currentUser.getFullName();
        }
        JLabel welcomeLabel = new JLabel(welcomeMessage);
        welcomeLabel.setFont(REGULAR_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        
        // Nút đăng xuất
        JButton logoutButton = new JButton("Đăng xuất");
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
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        separator.setBackground(BACKGROUND_COLOR);
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        storePanel.add(headerPanel, BorderLayout.NORTH);
        storePanel.add(productPanel, BorderLayout.CENTER);
        
        tabbedPane.insertTab("Trang chủ", null, storePanel, "Trang chủ cửa hàng", 0);
        tabbedPane.setSelectedIndex(0);
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
     * Initialize the product panel with listing and details.
     */
    private void initProductPanel() {
        productPanel = new JPanel(new BorderLayout(10, 10));
        productPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        productPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel tìm kiếm với giao diện mới
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel searchLabel = new JLabel("Tìm kiếm sản phẩm:");
        searchLabel.setFont(REGULAR_FONT);
        searchLabel.setForeground(TEXT_COLOR);
        
        searchField = new JTextField();
        searchField.setFont(REGULAR_FONT);
        searchField.setMargin(new Insets(8, 8, 8, 8));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(BUTTON_FONT);
        searchButton.setBackground(ACCENT_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel searchControlPanel = new JPanel(new BorderLayout(10, 0));
        searchControlPanel.setBackground(BACKGROUND_COLOR);
        searchControlPanel.add(searchField, BorderLayout.CENTER);
        searchControlPanel.add(searchButton, BorderLayout.EAST);
        
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchControlPanel, BorderLayout.CENTER);
        productPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Panel danh sách sản phẩm (bên trái) với giao diện mới
        JPanel productListPanel = new JPanel(new BorderLayout());
        productListPanel.setBackground(BACKGROUND_COLOR);
        productListPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                "DANH SÁCH SẢN PHẨM",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_COLOR));
        
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
        productTable.setRowHeight(35);
        productTable.setFont(REGULAR_FONT);
        productTable.setGridColor(new Color(230, 230, 230));
        productTable.setShowVerticalLines(true);
        productTable.setShowHorizontalLines(true);
        
        // Tùy chỉnh header của bảng
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(PRIMARY_COLOR);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Tùy chỉnh màu nền xen kẽ cho các dòng
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(new Color(230, 230, 250)); // Màu khi được chọn
                    c.setForeground(PRIMARY_COLOR);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                    c.setForeground(TEXT_COLOR);
                }
                
                // Căn giữa nội dung
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                
                return c;
            }
        });
        
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
        productScrollPane.setBorder(BorderFactory.createEmptyBorder());
        productScrollPane.getViewport().setBackground(Color.WHITE);
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
        accountPanel.setBackground(new Color(245, 245, 250));
        accountPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        if (currentUser == null) {
            JLabel noInfoLabel = new JLabel("Không có thông tin tài khoản.", SwingConstants.CENTER);
            noInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noInfoLabel.setForeground(new Color(150, 150, 150));
            accountPanel.add(noInfoLabel, BorderLayout.CENTER);
            return;
        }
        
        // Create panel for user information
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setBackground(new Color(245, 245, 250));
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with title and avatar
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create avatar panel with circular avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(208, 33, 80)); // Màu đỏ hồng từ hình ảnh
                g2d.fillOval(0, 0, 80, 80);
                
                // Draw initials
                String initials = getInitials(currentUser.getFullName());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 32));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(initials);
                int textHeight = fm.getHeight();
                g2d.drawString(initials, (80 - textWidth) / 2, 40 + textHeight / 4);
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
        };
        
        // Title and role panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 245, 250));
        
        // Add user information fields
        JLabel titleLabel = new JLabel("THÔNG TIN TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(208, 33, 80)); // Màu đỏ hồng từ hình ảnh
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(currentUser.getRole() != null ? currentUser.getRole() : "Nhân viên");
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        roleLabel.setForeground(new Color(100, 100, 100));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(roleLabel);
        
        headerPanel.add(avatarPanel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Create card-like panel for user information
        JPanel infoCardPanel = new JPanel(new BorderLayout());
        infoCardPanel.setBackground(Color.WHITE);
        infoCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // Info panel with user details
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        infoPanel.setBackground(Color.WHITE);
        
        // Add user details with improved styling
        addStyledInfoField(infoPanel, "Họ và tên:", currentUser.getFullName());
        addStyledInfoField(infoPanel, "Email:", currentUser.getEmail());
        addStyledInfoField(infoPanel, "Số điện thoại:", currentUser.getPhone());
        addStyledInfoField(infoPanel, "Vai trò:", currentUser.getRole());
        addStyledInfoField(infoPanel, "Địa chỉ:", currentUser.getAddress());
        
        infoCardPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Add logout button with improved styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(208, 33, 80)); // Màu đỏ hồng từ hình ảnh
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(178, 23, 60)); // Darker shade when hovered
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(208, 33, 80)); // Original color
            }
        });
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn đăng xuất?", 
                    "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        buttonPanel.add(logoutButton);
        
        // Assemble all panels
        userInfoPanel.add(headerPanel);
        userInfoPanel.add(infoCardPanel);
        userInfoPanel.add(buttonPanel);
        
        // Add a scroll pane in case the window is resized
        JScrollPane scrollPane = new JScrollPane(userInfoPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(245, 245, 250));
        scrollPane.getViewport().setBackground(new Color(245, 245, 250));
        
        accountPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Helper method to add information fields to the account panel with improved styling
     */
    private void addStyledInfoField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(new Color(80, 80, 80));
        
        JLabel valueComponent = new JLabel(value != null ? value : "");
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComponent.setForeground(new Color(50, 50, 50));
        
        // Add a bottom border to create a line effect
        JPanel bottomLine = new JPanel();
        bottomLine.setPreferredSize(new Dimension(fieldPanel.getWidth(), 1));
        bottomLine.setBackground(new Color(240, 240, 240));
        
        fieldPanel.add(labelComponent, BorderLayout.WEST);
        fieldPanel.add(valueComponent, BorderLayout.EAST);
        fieldPanel.add(bottomLine, BorderLayout.SOUTH);
        
        panel.add(fieldPanel);
    }
    
    /**
     * Helper method to get initials from a name
     */
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        
        String[] parts = name.split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        } else {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
    }
    
    /**
     * Helper method to add information fields to the account panel
     */
    private void addInfoField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valueComponent = new JLabel(value != null ? value : "");
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    /**
     * Initialize the search panel with tabs for searching products, staff, and customers.
     */
    private void initSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane for different search types
        JTabbedPane searchTabs = new JTabbedPane();
        
        // Create the product search panel
        JPanel productSearchPanel = createProductSearchPanel();
        searchTabs.addTab("Tìm sản phẩm", productSearchPanel);
        
        // Create the staff search panel
        JPanel staffSearchPanel = createStaffSearchPanel();
        searchTabs.addTab("Tìm nhân viên", staffSearchPanel);
        
        // Create the customer search panel
        JPanel customerSearchPanel = createCustomerSearchPanel();
        searchTabs.addTab("Tìm khách hàng", customerSearchPanel);
        
        searchPanel.add(searchTabs, BorderLayout.CENTER);
    }
    
    /**
     * Create the product search panel.
     */
    private JPanel createProductSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search controls panel
        JPanel searchControlsPanel = new JPanel(new BorderLayout(5, 0));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchControlsPanel.add(new JLabel("Tên sản phẩm: "), BorderLayout.WEST);
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchButton, BorderLayout.EAST);
        
        // Results table
        String[] columnNames = {"ID", "Tên sản phẩm", "Giá", "Số lượng", "Mô tả"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        // Add components to panel
        panel.add(searchControlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search button action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                // Clear the table
                tableModel.setRowCount(0);
                
                // Search for products
                List<Product> products = clientService.searchProducts(keyword);
                
                if (products.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Add products to the table
                    for (Product product : products) {
                        Object[] row = {
                            product.getId(),
                            product.getName(),
                            String.format("%,.0f VNĐ", product.getPrice()),
                            product.getQuantity(),
                            product.getDescription()
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    /**
     * Create the staff search panel.
     */
    private JPanel createStaffSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search controls panel
        JPanel searchControlsPanel = new JPanel(new BorderLayout(5, 0));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchControlsPanel.add(new JLabel("Tên hoặc email: "), BorderLayout.WEST);
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchButton, BorderLayout.EAST);
        
        // Results table
        String[] columnNames = {"ID", "Họ tên", "Email", "Vai trò", "Số điện thoại"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        // Add components to panel
        panel.add(searchControlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search button action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                // Clear the table
                tableModel.setRowCount(0);
                
                // Search for users
                List<User> users = clientService.searchUsers(keyword);
                
                if (users.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Add users to the table
                    for (User user : users) {
                        Object[] row = {
                            user.getId(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getRole(),
                            user.getPhone()
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm nhân viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    /**
     * Create the customer search panel.
     */
    private JPanel createCustomerSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search controls panel
        JPanel searchControlsPanel = new JPanel(new BorderLayout(5, 0));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchControlsPanel.add(new JLabel("Tên, email hoặc số điện thoại: "), BorderLayout.WEST);
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchButton, BorderLayout.EAST);
        
        // Results table
        String[] columnNames = {"ID", "Họ tên", "Email", "Số điện thoại", "Địa chỉ"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        // Add components to panel
        panel.add(searchControlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search button action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                // Clear the table
                tableModel.setRowCount(0);
                
                // Search for customers
                List<Customer> customers = clientService.searchCustomers(keyword);
                
                if (customers.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Add customers to the table
                    for (Customer customer : customers) {
                        Object[] row = {
                            customer.getId(),
                            customer.getName(),
                            customer.getEmail(),
                            customer.getPhone(),
                            customer.getAddress()
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }

    /**
     * Initialize the help panel.
     */
    private void initHelpPanel() {
        helpPanel = new JPanel(new BorderLayout());
        helpPanel.add(new JLabel("Thông tin trợ giúp sẽ được hiển thị ở đây.", SwingConstants.CENTER));
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
        
        // Create order items list
        List<CartItem> orderItems = new ArrayList<>();
        double total = 0;
        
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String productName = (String) cartTableModel.getValueAt(i, 0);
            String priceString = (String) cartTableModel.getValueAt(i, 1);
            int quantity = Integer.parseInt(cartTableModel.getValueAt(i, 2).toString());
            String totalString = (String) cartTableModel.getValueAt(i, 3);
            
            // Clean price string and parse
            priceString = priceString.replace(",", "").replace("đ", "").trim();
            double price = Double.parseDouble(priceString);
            
            // Create cart item
            CartItem item = new CartItem();
            item.setProductId(i + 1); // Temporary ID for mock implementation
            item.setProductName(productName);
            item.setPrice(price);
            item.setQuantity(quantity);
            orderItems.add(item);
            
            summary.append(productName)
                   .append(" x ")
                   .append(quantity)
                   .append(" = ")
                   .append(totalString)
                   .append("\n");
                   
            total += price * quantity;
        }
        
        summary.append("\nTổng cộng: ").append(totalPriceLabel.getText().substring(11));
        
        int confirm = JOptionPane.showConfirmDialog(this,
                summary.toString() + "\n\nXác nhận thanh toán?",
                "Xác nhận đơn hàng", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Create order object
                Order order = new Order();
                
                // Convert CartItem to OrderItem
                List<OrderItem> orderItemList = new ArrayList<>();
                for (CartItem cartItem : orderItems) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setProductName(cartItem.getProductName());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItemList.add(orderItem);
                }
                
                order.setItems(orderItemList);
                order.setTotalAmount(total);
                
                // Format the date as string
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order.setOrderDate(dateFormat.format(new java.util.Date()));
                
                // Set customer information from current user
                if (currentUser != null) {
                    order.setCustomerName(currentUser.getFullName());
                    order.setCustomerPhone(currentUser.getPhone());
                    order.setCustomerAddress(currentUser.getAddress());
                }
                
                // Call service to place order
                boolean success = clientService.placeOrder(order);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Đơn hàng đã được lưu thành công!");
                    cartTableModel.setRowCount(0);
                    updateTotalPrice();
                    updateCartButtonStates();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Có lỗi xảy ra khi lưu đơn hàng. Vui lòng thử lại sau.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
