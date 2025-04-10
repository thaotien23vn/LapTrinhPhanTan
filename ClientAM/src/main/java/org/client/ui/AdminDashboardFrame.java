package org.client.ui;

import org.client.services.ClientService;
import org.client.entities.Product;
import org.client.entities.User;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AdminDashboardFrame extends JFrame {
    private final ClientService clientService;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel productPanel;
    private JPanel staffPanel;
    private JPanel customerPanel;
    private JPanel orderPanel;
    private JPanel statisticsPanel;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField productQuantityField;
    private JTextField productDescriptionField;
    private JTextField productBrandField;
    private JLabel productImageLabel;
    private File selectedImageFile;
    
    // Staff management fields
    private JTextField staffIdField;
    private JTextField staffNameField;
    private JTextField staffPhoneField;
    private JTextField staffEmailField;
    private JTextField staffAddressField;
    private JComboBox<String> staffRoleComboBox;
    private JTable staffTable;
    private DefaultTableModel staffTableModel;
    
    // Customer management fields
    private JTextField customerIdField;
    private JTextField customerNameField;
    private JTextField customerPhoneField;
    private JTextField customerEmailField;
    private JTextField customerAddressField;
    private JComboBox<String> customerTypeComboBox;
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    
    // Order management fields
    private JTextField orderIdField;
    private JTextField orderCustomerIdField;
    private JTextField orderDateField;
    private JTextField orderTotalField;
    private JComboBox<String> orderStatusComboBox;
    private JTextField orderStaffIdField;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    
    // Statistics fields
    private JTextField fromDateField;
    private JTextField toDateField;
    private JComboBox<String> productTypeComboBox;
    private JComboBox<String> staffComboBox;
    private JTable statsTable;
    private DefaultTableModel statsTableModel;
    
    private static final Color PRIMARY_COLOR = new Color(208, 33, 80); // Màu đỏ hồng từ hình ảnh
    private static final Color BACKGROUND_COLOR = new Color(255, 250, 240); // Màu nền nhạt
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font SIDEBAR_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    public AdminDashboardFrame(ClientService clientService) {
        this.clientService = clientService;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("QUẢN LÝ CỬA HÀNG THỜI TRANG");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set up the main layout
        setLayout(new BorderLayout());
        
        // Create the sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Create the content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);
        
        // Create panels for each section
        productPanel = createProductPanel();
        staffPanel = createStaffPanel();
        customerPanel = createCustomerPanel();
        orderPanel = createOrderPanel();
        statisticsPanel = createStatisticsPanel();
        
        // Add panels to the card layout
        contentPanel.add(productPanel, "products");
        contentPanel.add(staffPanel, "staff");
        contentPanel.add(customerPanel, "customers");
        contentPanel.add(orderPanel, "orders");
        contentPanel.add(statisticsPanel, "statistics");
        
        // Show the product panel by default
        cardLayout.show(contentPanel, "products");
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        // Add profile image and name
        JLabel profileImage = new JLabel();
        profileImage.setPreferredSize(new Dimension(100, 100));
        profileImage.setBackground(Color.DARK_GRAY);
        profileImage.setOpaque(true);
        profileImage.setText("Ảnh");
        profileImage.setForeground(Color.WHITE);
        profileImage.setHorizontalAlignment(JLabel.CENTER);
        profileImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("TIẾN TRẦN");
        nameLabel.setFont(HEADER_FONT);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("QUẢN LÝ");
        roleLabel.setFont(SIDEBAR_FONT);
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(profileImage);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(nameLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(roleLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Add menu buttons
        String[] menuItems = {
            "QUẢN LÝ NHÂN VIÊN", 
            "QUẢN LÝ KHÁCH HÀNG", 
            "QUẢN LÝ SẢN PHẨM", 
            "THỐNG KÊ DOANH THU", 
            "QUẢN LÝ HÓA ĐƠN"
        };
        
        String[] cardNames = {"staff", "customers", "products", "statistics", "orders"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], cardNames[i]);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Add logout button at the bottom
        sidebar.add(Box.createVerticalGlue());
        JButton logoutButton = new JButton("ĐĂNG XUẤT");
        styleButton(logoutButton);
        logoutButton.setBackground(new Color(150, 20, 60)); // Darker red for logout
        logoutButton.addActionListener(e -> logout());
        sidebar.add(logoutButton);
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        return button;
    }
    
    private void styleButton(JButton button) {
        button.setFont(SIDEBAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 60, 100));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }
    
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Product form panel (top section)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ SẢN PHẨM");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        fieldsPanel.setBackground(BACKGROUND_COLOR);
        fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Row 1
        JLabel idLabel = new JLabel("MÃ SẢN PHẨM:");
        productIdField = new JTextField();
        JLabel nameLabel = new JLabel("TÊN SẢN PHẨM:");
        productNameField = new JTextField();
        
        // Row 2
        JLabel priceLabel = new JLabel("GIÁ SẢN PHẨM:");
        productPriceField = new JTextField();
        JLabel typeLabel = new JLabel("LOẠI SẢN PHẨM:");
        String[] productTypes = {"Áo", "Quần", "Váy", "Giày", "Phụ kiện"};
        JComboBox<String> productTypeCombo = new JComboBox<>(productTypes);
        
        // Row 3
        JLabel quantityLabel = new JLabel("SL TỒN KHO:");
        productQuantityField = new JTextField();
        JLabel brandLabel = new JLabel("THƯƠNG HIỆU:");
        productBrandField = new JTextField();
        
        // Add components to the fields panel
        fieldsPanel.add(idLabel);
        fieldsPanel.add(productIdField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(productNameField);
        fieldsPanel.add(priceLabel);
        fieldsPanel.add(productPriceField);
        fieldsPanel.add(typeLabel);
        fieldsPanel.add(productTypeCombo);
        fieldsPanel.add(quantityLabel);
        fieldsPanel.add(productQuantityField);
        fieldsPanel.add(brandLabel);
        fieldsPanel.add(productBrandField);
        
        // Description field
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(BACKGROUND_COLOR);
        JLabel descLabel = new JLabel("MÔ TẢ:");
        productDescriptionField = new JTextField();
        descPanel.add(descLabel, BorderLayout.WEST);
        descPanel.add(productDescriptionField, BorderLayout.CENTER);
        
        // Image selection
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(BACKGROUND_COLOR);
        productImageLabel = new JLabel();
        productImageLabel.setPreferredSize(new Dimension(150, 150));
        productImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productImageLabel.setText("Hình ảnh");
        JButton chooseImageButton = new JButton("CHỌN ẢNH");
        chooseImageButton.setBackground(new Color(255, 200, 200));
        chooseImageButton.addActionListener(e -> chooseProductImage());
        
        imagePanel.add(productImageLabel, BorderLayout.CENTER);
        imagePanel.add(chooseImageButton, BorderLayout.SOUTH);
        
        // Combine description and image
        JPanel bottomFormPanel = new JPanel(new BorderLayout(10, 0));
        bottomFormPanel.setBackground(BACKGROUND_COLOR);
        bottomFormPanel.add(descPanel, BorderLayout.CENTER);
        bottomFormPanel.add(imagePanel, BorderLayout.EAST);
        
        // Add all form components
        JPanel completeFormPanel = new JPanel(new BorderLayout());
        completeFormPanel.setBackground(BACKGROUND_COLOR);
        completeFormPanel.add(fieldsPanel, BorderLayout.CENTER);
        completeFormPanel.add(bottomFormPanel, BorderLayout.SOUTH);
        
        formPanel.add(completeFormPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton clearButton = new JButton("Làm mới");
        
        // Style the buttons
        for (JButton button : new JButton[]{addButton, updateButton, deleteButton, clearButton}) {
            button.setFont(NORMAL_FONT);
            button.setPreferredSize(new Dimension(100, 30));
        }
        
        addButton.setBackground(new Color(100, 180, 100));
        updateButton.setBackground(new Color(100, 150, 200));
        deleteButton.setBackground(new Color(200, 100, 100));
        clearButton.setBackground(new Color(200, 200, 100));
        
        // Add action listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearProductForm());
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Product table (bottom section)
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại", "Giá", "SL tồn", "Thương hiệu"};
        productTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        productTable = new JTable(productTableModel);
        productTable.setFont(NORMAL_FONT);
        productTable.getTableHeader().setFont(NORMAL_FONT.deriveFont(Font.BOLD));
        productTable.setRowHeight(25);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add selection listener to populate form when a row is selected
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                populateProductForm(productTable.getSelectedRow());
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        
        // Add form and table to the main panel
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Load product data
        loadProductData();
        
        return panel;
    }
    
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Staff form panel (top section)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ NHÂN VIÊN");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        fieldsPanel.setBackground(BACKGROUND_COLOR);
        fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Row 1
        JLabel idLabel = new JLabel("MÃ NHÂN VIÊN:");
        staffIdField = new JTextField();
        JLabel nameLabel = new JLabel("TÊN NHÂN VIÊN:");
        staffNameField = new JTextField();
        
        // Row 2
        JLabel phoneLabel = new JLabel("SỐ ĐIỆN THOẠI:");
        staffPhoneField = new JTextField();
        JLabel emailLabel = new JLabel("EMAIL:");
        staffEmailField = new JTextField();
        
        // Row 3
        JLabel addressLabel = new JLabel("ĐỊA CHỈ:");
        staffAddressField = new JTextField();
        JLabel roleLabel = new JLabel("CHỨC VỤ:");
        staffRoleComboBox = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng", "Nhân viên kho"});
        
        // Add components to fields panel
        fieldsPanel.add(idLabel);
        fieldsPanel.add(staffIdField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(staffNameField);
        fieldsPanel.add(phoneLabel);
        fieldsPanel.add(staffPhoneField);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(staffEmailField);
        fieldsPanel.add(addressLabel);
        fieldsPanel.add(staffAddressField);
        fieldsPanel.add(roleLabel);
        fieldsPanel.add(staffRoleComboBox);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton clearButton = new JButton("Làm mới");
        
        // Add event listeners to buttons
        addButton.addActionListener(e -> addStaff());
        editButton.addActionListener(e -> updateStaff());
        deleteButton.addActionListener(e -> deleteStaff());
        clearButton.addActionListener(e -> clearStaffForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel (bottom section)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        // Create the table model and table
        staffTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã NV", "Tên nhân viên", "Số điện thoại", "Email", "Địa chỉ", "Chức vụ"}
        );
        
        staffTable = new JTable(staffTableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add selection listener to populate form when a row is selected
        staffTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && staffTable.getSelectedRow() != -1) {
                populateStaffForm(staffTable.getSelectedRow());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(staffTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add sample data
        staffTableModel.addRow(new Object[]{"NV001", "Nguyễn Văn A", "0123456789", "nva@example.com", "Hà Nội", "Quản lý"});
        staffTableModel.addRow(new Object[]{"NV002", "Trần Thị B", "0987654321", "ttb@example.com", "Hồ Chí Minh", "Nhân viên bán hàng"});
        staffTableModel.addRow(new Object[]{"NV003", "Lê Văn C", "0369852147", "lvc@example.com", "Đà Nẵng", "Nhân viên kho"});
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Customer form panel (top section)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        fieldsPanel.setBackground(BACKGROUND_COLOR);
        fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Row 1
        JLabel idLabel = new JLabel("MÃ KHÁCH HÀNG:");
        customerIdField = new JTextField();
        JLabel nameLabel = new JLabel("TÊN KHÁCH HÀNG:");
        customerNameField = new JTextField();
        
        // Row 2
        JLabel phoneLabel = new JLabel("SỐ ĐIỆN THOẠI:");
        customerPhoneField = new JTextField();
        JLabel emailLabel = new JLabel("EMAIL:");
        customerEmailField = new JTextField();
        
        // Row 3
        JLabel addressLabel = new JLabel("ĐỊA CHỈ:");
        customerAddressField = new JTextField();
        JLabel typeLabel = new JLabel("LOẠI KHÁCH HÀNG:");
        customerTypeComboBox = new JComboBox<>(new String[]{"Thường", "VIP", "Thân thiết"});
        
        // Add components to fields panel
        fieldsPanel.add(idLabel);
        fieldsPanel.add(customerIdField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(customerNameField);
        fieldsPanel.add(phoneLabel);
        fieldsPanel.add(customerPhoneField);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(customerEmailField);
        fieldsPanel.add(addressLabel);
        fieldsPanel.add(customerAddressField);
        fieldsPanel.add(typeLabel);
        fieldsPanel.add(customerTypeComboBox);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton clearButton = new JButton("Làm mới");
        
        // Add event listeners to buttons
        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearCustomerForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel (bottom section)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        // Create the table model and table
        customerTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã KH", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ", "Loại khách hàng"}
        );
        
        customerTable = new JTable(customerTableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add selection listener to populate form when a row is selected
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                populateCustomerForm(customerTable.getSelectedRow());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add sample data
        customerTableModel.addRow(new Object[]{"KH001", "Phạm Thị D", "0123456789", "ptd@example.com", "Hà Nội", "VIP"});
        customerTableModel.addRow(new Object[]{"KH002", "Đỗ Văn E", "0987654321", "dve@example.com", "Hồ Chí Minh", "Thường"});
        customerTableModel.addRow(new Object[]{"KH003", "Hoàng Văn F", "0369852147", "hvf@example.com", "Đà Nẵng", "Thân thiết"});
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Order form panel (top section)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ HÓA ĐƠN");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        fieldsPanel.setBackground(BACKGROUND_COLOR);
        fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Row 1
        JLabel orderIdLabel = new JLabel("MÃ HÓA ĐƠN:");
        orderIdField = new JTextField();
        JLabel customerIdLabel = new JLabel("MÃ KHÁCH HÀNG:");
        orderCustomerIdField = new JTextField();
        
        // Row 2
        JLabel dateLabel = new JLabel("NGÀY LẬP:");
        orderDateField = new JTextField();
        JLabel totalLabel = new JLabel("TỔNG TIỀN:");
        orderTotalField = new JTextField();
        
        // Row 3
        JLabel statusLabel = new JLabel("TRẠNG THÁI:");
        orderStatusComboBox = new JComboBox<>(new String[]{"Đã thanh toán", "Chưa thanh toán", "Đã hủy"});
        JLabel staffIdLabel = new JLabel("MÃ NHÂN VIÊN:");
        orderStaffIdField = new JTextField();
        
        // Add components to fields panel
        fieldsPanel.add(orderIdLabel);
        fieldsPanel.add(orderIdField);
        fieldsPanel.add(customerIdLabel);
        fieldsPanel.add(orderCustomerIdField);
        fieldsPanel.add(dateLabel);
        fieldsPanel.add(orderDateField);
        fieldsPanel.add(totalLabel);
        fieldsPanel.add(orderTotalField);
        fieldsPanel.add(statusLabel);
        fieldsPanel.add(orderStatusComboBox);
        fieldsPanel.add(staffIdLabel);
        fieldsPanel.add(orderStaffIdField);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton viewButton = new JButton("Xem");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton printButton = new JButton("In hóa đơn");
        JButton clearButton = new JButton("Làm mới");
        
        // Add event listeners to buttons
        viewButton.addActionListener(e -> viewOrderDetails());
        editButton.addActionListener(e -> updateOrder());
        deleteButton.addActionListener(e -> deleteOrder());
        printButton.addActionListener(e -> printOrder());
        clearButton.addActionListener(e -> clearOrderForm());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(printButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel (bottom section)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        // Create the table model and table
        orderTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HĐ", "Ngày lập", "Khách hàng", "Tổng tiền", "Trạng thái", "Nhân viên"}
        );
        
        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add selection listener to populate form when a row is selected
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && orderTable.getSelectedRow() != -1) {
                populateOrderForm(orderTable.getSelectedRow());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add sample data
        orderTableModel.addRow(new Object[]{"HD001", "20/04/2025", "Phạm Thị D", "350,000 VNĐ", "Đã thanh toán", "Nguyễn Văn A"});
        orderTableModel.addRow(new Object[]{"HD002", "21/04/2025", "Đỗ Văn E", "1,120,000 VNĐ", "Đã thanh toán", "Trần Thị B"});
        orderTableModel.addRow(new Object[]{"HD003", "21/04/2025", "Hoàng Văn F", "650,000 VNĐ", "Chưa thanh toán", "Nguyễn Văn A"});
        orderTableModel.addRow(new Object[]{"HD004", "22/04/2025", "Lê Thị G", "850,000 VNĐ", "Đã hủy", "Lê Văn C"});
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Statistics form panel (top section)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("THỐNG KÊ DOANH THU");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        fieldsPanel.setBackground(BACKGROUND_COLOR);
        fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Row 1
        JLabel fromDateLabel = new JLabel("TỪ NGÀY:");
        fromDateField = new JTextField();
        fromDateField.setText("01/04/2025"); // Default start date
        JLabel toDateLabel = new JLabel("ĐẾN NGÀY:");
        toDateField = new JTextField();
        toDateField.setText("22/04/2025"); // Default end date
        
        // Row 2
        JLabel productTypeLabel = new JLabel("LOẠI SẢN PHẨM:");
        productTypeComboBox = new JComboBox<>(new String[]{"Tất cả", "Áo", "Quần", "Váy", "Giày", "Phụ kiện"});
        JLabel staffLabel = new JLabel("NHÂN VIÊN:");
        staffComboBox = new JComboBox<>(new String[]{"Tất cả", "Nguyễn Văn A", "Trần Thị B", "Lê Văn C"});
        
        // Add components to fields panel
        fieldsPanel.add(fromDateLabel);
        fieldsPanel.add(fromDateField);
        fieldsPanel.add(toDateLabel);
        fieldsPanel.add(toDateField);
        fieldsPanel.add(productTypeLabel);
        fieldsPanel.add(productTypeComboBox);
        fieldsPanel.add(staffLabel);
        fieldsPanel.add(staffComboBox);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton searchButton = new JButton("Tìm kiếm");
        JButton exportButton = new JButton("Xuất báo cáo");
        JButton printButton = new JButton("In báo cáo");
        
        // Add event listeners to buttons
        searchButton.addActionListener(e -> searchStatistics());
        exportButton.addActionListener(e -> exportStatistics());
        printButton.addActionListener(e -> printStatistics());
        
        buttonPanel.add(searchButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(printButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Create statistics display panel
        JPanel statisticsDisplayPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statisticsDisplayPanel.setBackground(BACKGROUND_COLOR);
        statisticsDisplayPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Left panel - Summary info
        JPanel summaryPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Thông tin tổng quan"));
        
        JLabel totalOrdersLabel = new JLabel("Tổng số đơn hàng: 358");
        JLabel totalRevenueLabel = new JLabel("Tổng doanh thu: 358,950,000 VNĐ");
        JLabel averageOrderLabel = new JLabel("Giá trị đơn hàng trung bình: 1,002,653 VNĐ");
        JLabel totalProductsLabel = new JLabel("Tổng sản phẩm đã bán: 789");
        
        summaryPanel.add(totalOrdersLabel);
        summaryPanel.add(totalRevenueLabel);
        summaryPanel.add(averageOrderLabel);
        summaryPanel.add(totalProductsLabel);
        
        // Right panel - Chart placeholder (create simple bar chart)
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ doanh thu"));
        
        // Create a simple visualization panel for the chart
        JPanel barChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int barWidth = width / 5;
                int maxHeight = height - 50;
                
                // Draw axes
                g2d.setColor(Color.BLACK);
                g2d.drawLine(40, height - 30, width - 20, height - 30); // x-axis
                g2d.drawLine(40, 20, 40, height - 30); // y-axis
                
                // Sample data (values as percentages of max height)
                int[] values = {60, 80, 45, 75, 55};
                String[] days = {"T2", "T3", "T4", "T5", "T6"};
                
                // Draw bars
                for (int i = 0; i < values.length; i++) {
                    int barHeight = (values[i] * maxHeight) / 100;
                    int x = 60 + i * (barWidth + 10);
                    int y = height - 30 - barHeight;
                    
                    // Bar
                    g2d.setColor(new Color(208, 33, 80));
                    g2d.fillRect(x, y, barWidth - 20, barHeight);
                    
                    // Border
                    g2d.setColor(new Color(150, 20, 60));
                    g2d.drawRect(x, y, barWidth - 20, barHeight);
                    
                    // Label
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(days[i], x + (barWidth - 20) / 2 - 5, height - 15);
                }
                
                // Y-axis labels
                g2d.drawString("0", 25, height - 25);
                g2d.drawString("50%", 20, height / 2);
                g2d.drawString("100%", 15, 25);
            }
        };
        barChartPanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.add(barChartPanel, BorderLayout.CENTER);
        
        statisticsDisplayPanel.add(summaryPanel);
        statisticsDisplayPanel.add(chartPanel);
        
        // Table panel (bottom section)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Create the table model and table
        statsTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Ngày", "Số đơn hàng", "Doanh thu", "Sản phẩm bán ra", "Nhân viên bán hàng"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        statsTable = new JTable(statsTableModel);
        JScrollPane scrollPane = new JScrollPane(statsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add sample data
        statsTableModel.addRow(new Object[]{"20/04/2025", "15", "15,850,000 VNĐ", "32", "Nguyễn Văn A"});
        statsTableModel.addRow(new Object[]{"21/04/2025", "18", "18,750,000 VNĐ", "38", "Trần Thị B"});
        statsTableModel.addRow(new Object[]{"22/04/2025", "12", "12,350,000 VNĐ", "25", "Lê Văn C"});
        
        // Add components to main panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(statisticsDisplayPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void chooseProductImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            try {
                ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
                Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                productImageLabel.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể tải ảnh: " + e.getMessage());
            }
        }
    }
    
    private void loadProductData() {
        // Clear existing data
        productTableModel.setRowCount(0);
        
        // Add sample data for now
        // In a real app, this would fetch data from the clientService
        Object[][] sampleData = {
            {"SP001", "Áo thun nam", "Áo", "250000", "50", "Nike"},
            {"SP002", "Quần jean nữ", "Quần", "450000", "30", "Levi's"},
            {"SP003", "Váy dạ hội", "Váy", "1200000", "15", "Gucci"},
            {"SP004", "Giày thể thao", "Giày", "800000", "25", "Adidas"},
            {"SP005", "Túi xách", "Phụ kiện", "950000", "20", "Louis Vuitton"}
        };
        
        for (Object[] row : sampleData) {
            productTableModel.addRow(row);
        }
    }
    
    private void populateProductForm(int selectedRow) {
        productIdField.setText(productTableModel.getValueAt(selectedRow, 0).toString());
        productNameField.setText(productTableModel.getValueAt(selectedRow, 1).toString());
        productPriceField.setText(productTableModel.getValueAt(selectedRow, 3).toString());
        productQuantityField.setText(productTableModel.getValueAt(selectedRow, 4).toString());
        productBrandField.setText(productTableModel.getValueAt(selectedRow, 5).toString());
        productDescriptionField.setText(""); // Description not in table, would come from database
        
        // Clear the image
        productImageLabel.setIcon(null);
        selectedImageFile = null;
    }
    
    private void addProduct() {
        // Validate input
        if (productNameField.getText().isEmpty() || productPriceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và giá sản phẩm");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String id = productIdField.getText().isEmpty() ? "SP" + (productTableModel.getRowCount() + 1) : productIdField.getText();
            String name = productNameField.getText();
            String type = "Áo"; // Would come from the combo box
            double price = Double.parseDouble(productPriceField.getText());
            int quantity = productQuantityField.getText().isEmpty() ? 0 : Integer.parseInt(productQuantityField.getText());
            String brand = productBrandField.getText();
            String description = productDescriptionField.getText();
            
            // Tạo đối tượng Product
            Product product = new Product();
            product.setId(Integer.parseInt(id.replaceAll("\\D", ""))); // Chuyển SP001 thành 1
            product.setName(name);
            product.setType(type);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setBrand(brand);
            product.setDescription(description);
            
            // Lưu vào database
            boolean success = clientService.addProduct(product);
            
            if (success) {
                // Thêm vào bảng hiển thị nếu thành công
                Object[] rowData = {id, name, type, String.valueOf(price), String.valueOf(quantity), brand};
                productTableModel.addRow(rowData);
                
                // Clear the form
                clearProductForm();
                
                JOptionPane.showMessageDialog(this, "Sản phẩm đã được thêm thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm vào database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng: Vui lòng nhập giá và số lượng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật");
            return;
        }
        
        // Validate input
        if (productNameField.getText().isEmpty() || productPriceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và giá sản phẩm");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String id = productIdField.getText();
            String name = productNameField.getText();
            String type = "Áo"; // Gốc lấy từ combo box
            double price = Double.parseDouble(productPriceField.getText());
            int quantity = Integer.parseInt(productQuantityField.getText());
            String brand = productBrandField.getText();
            String description = productDescriptionField.getText();
            
            // Tạo đối tượng Product
            Product product = new Product();
            product.setId(Integer.parseInt(id.replaceAll("\\D", "")));
            product.setName(name);
            product.setType(type);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setBrand(brand);
            product.setDescription(description);
            
            // Cập nhật vào database
            boolean success = clientService.updateProduct(product);
            
            if (success) {
                // Cập nhật bảng hiển thị nếu thành công
                productTableModel.setValueAt(id, selectedRow, 0);
                productTableModel.setValueAt(name, selectedRow, 1);
                // Type would be updated from combo box
                productTableModel.setValueAt(String.valueOf(price), selectedRow, 3);
                productTableModel.setValueAt(String.valueOf(quantity), selectedRow, 4);
                productTableModel.setValueAt(brand, selectedRow, 5);
                
                // Clear the form
                clearProductForm();
                
                JOptionPane.showMessageDialog(this, "Sản phẩm đã được cập nhật thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật sản phẩm vào database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng: Vui lòng nhập giá và số lượng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa sản phẩm này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy mã sản phẩm từ bảng
                String productId = productTableModel.getValueAt(selectedRow, 0).toString();
                int id = Integer.parseInt(productId.replaceAll("\\D", "")); // Chuyển SP001 thành 1
                
                // Xóa sản phẩm trong database
                boolean success = clientService.deleteProduct(id);
                
                if (success) {
                    // Nếu xóa thành công thì cập nhật giao diện
                    productTableModel.removeRow(selectedRow);
                    clearProductForm();
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã được xóa thành công và cập nhật trong database");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm khỏi database", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng mã sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearProductForm() {
        productIdField.setText("");
        productNameField.setText("");
        productPriceField.setText("");
        productQuantityField.setText("");
        productDescriptionField.setText("");
        productBrandField.setText("");
        productImageLabel.setIcon(null);
        selectedImageFile = null;
        productTable.clearSelection();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // Sử dụng ApplicationManager để hiển thị màn hình đăng nhập
            org.client.app.ApplicationManager.getInstance().showLoginScreen();
        }
    }
    
    // Staff Management Methods
    private void addStaff() {
        // Validate input
        if (staffNameField.getText().isEmpty() || staffPhoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại nhân viên");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String id = staffIdField.getText().isEmpty() ? "NV" + (staffTableModel.getRowCount() + 1) : staffIdField.getText();
            String name = staffNameField.getText();
            String phone = staffPhoneField.getText();
            String email = staffEmailField.getText();
            String address = staffAddressField.getText();
            String role = staffRoleComboBox.getSelectedItem().toString();
            String password = "123456"; // Mật khẩu mặc định
            
            // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu chưa
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = '" + email + "'";
            ResultSet emailCheckResult = clientService.executeQuery(checkEmailSql);
            if (emailCheckResult != null && emailCheckResult.next() && emailCheckResult.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Email " + email + " đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Lấy next ID từ database để tránh trùng lập
            String getMaxIdSql = "SELECT MAX(id) FROM users";
            ResultSet maxIdResult = clientService.executeQuery(getMaxIdSql);
            int nextId = 1;
            if (maxIdResult != null && maxIdResult.next() && maxIdResult.getObject(1) != null) {
                nextId = maxIdResult.getInt(1) + 1;
            }
            
            // Sử dụng câu lệnh SQL trực tiếp để thêm nhân viên vào database, không chỉ định ID
            String insertUserSql = "INSERT INTO users (email, username, password, full_name, phone, address, role) VALUES (" +
                "'" + email + "', '" + email + "', '" + password + "', '" + name + "', '" + phone + "', '" + address + "', '" + role + "')";
            
            boolean success = clientService.executeCustomQuery(insertUserSql);
            
            if (success) {
                // Thêm vào bảng hiển thị nếu thành công
                // Lấy ID thật từ database sau khi insert
                String getUserIdSql = "SELECT id FROM users WHERE email = '" + email + "'";
                ResultSet userIdResult = clientService.executeQuery(getUserIdSql);
                int actualId = nextId;
                if (userIdResult != null && userIdResult.next()) {
                    actualId = userIdResult.getInt("id");
                }
                
                // Format ID nhân viên
                String formattedId = "NV" + String.format("%03d", actualId);
                
                Object[] rowData = {formattedId, name, phone, email, address, role};
                staffTableModel.addRow(rowData);
                
                // Xóa form
                clearStaffForm();
                
                JOptionPane.showMessageDialog(this, "Nhân viên đã được thêm thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên vào database.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // In ra console để debug
        }
    }
    
    private void updateStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần cập nhật");
            return;
        }
        
        // Validate input
        if (staffNameField.getText().isEmpty() || staffPhoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại nhân viên");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String id = staffIdField.getText();
            String name = staffNameField.getText();
            String phone = staffPhoneField.getText();
            String email = staffEmailField.getText();
            String address = staffAddressField.getText();
            String role = staffRoleComboBox.getSelectedItem().toString();
            
            // Tạo đối tượng User
            User staff = new User();
            staff.setId(Integer.parseInt(id.replaceAll("\\D", ""))); // Chuyển NV001 thành 1
            staff.setFullName(name);
            staff.setPhone(phone);
            staff.setEmail(email);
            staff.setAddress(address);
            staff.setRole(role);
            
            // Cập nhật vào database sử dụng câu lệnh SQL trực tiếp
            String updateUserSql = "UPDATE users SET full_name = '" + name + "', phone = '" + phone + 
                "', email = '" + email + "', address = '" + address + "', role = '" + role + 
                "' WHERE id = " + staff.getId();
            
            boolean success = clientService.executeCustomQuery(updateUserSql);
            
            if (success) {
                // Cập nhật bảng hiển thị nếu thành công
                staffTableModel.setValueAt(id, selectedRow, 0);
                staffTableModel.setValueAt(name, selectedRow, 1);
                staffTableModel.setValueAt(phone, selectedRow, 2);
                staffTableModel.setValueAt(email, selectedRow, 3);
                staffTableModel.setValueAt(address, selectedRow, 4);
                staffTableModel.setValueAt(role, selectedRow, 5);
                
                // Clear the form
                clearStaffForm();
                
                JOptionPane.showMessageDialog(this, "Nhân viên đã được cập nhật thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật nhân viên vào database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa nhân viên này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy ID nhân viên
                String staffId = staffTableModel.getValueAt(selectedRow, 0).toString();
                int id = Integer.parseInt(staffId.replaceAll("\\D", "")); // Chuyển NV001 thành 1
                
                // Xóa nhân viên trong database
                String sql = "DELETE FROM users WHERE id = " + id + " AND role NOT LIKE '%admin%'";
                boolean success = clientService.executeCustomQuery(sql);
                
                if (success) {
                    // Xóa dữ liệu trên giao diện nếu thành công
                    staffTableModel.removeRow(selectedRow);
                    clearStaffForm();
                    JOptionPane.showMessageDialog(this, "Nhân viên đã được xóa thành công và cập nhật trong database");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên khỏi database", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearStaffForm() {
        staffIdField.setText("");
        staffNameField.setText("");
        staffPhoneField.setText("");
        staffEmailField.setText("");
        staffAddressField.setText("");
        staffRoleComboBox.setSelectedIndex(0);
        staffTable.clearSelection();
    }
    
    private void populateStaffForm(int selectedRow) {
        staffIdField.setText(staffTableModel.getValueAt(selectedRow, 0).toString());
        staffNameField.setText(staffTableModel.getValueAt(selectedRow, 1).toString());
        staffPhoneField.setText(staffTableModel.getValueAt(selectedRow, 2).toString());
        staffEmailField.setText(staffTableModel.getValueAt(selectedRow, 3).toString());
        staffAddressField.setText(staffTableModel.getValueAt(selectedRow, 4).toString());
        staffRoleComboBox.setSelectedItem(staffTableModel.getValueAt(selectedRow, 5).toString());
    }
    
    // Customer Management Methods
    private void addCustomer() {
        // Validate input
        if (customerNameField.getText().isEmpty() || customerPhoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại khách hàng");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String name = customerNameField.getText();
            String phone = customerPhoneField.getText();
            String email = customerEmailField.getText();
            String address = customerAddressField.getText();
            String type = customerTypeComboBox.getSelectedItem().toString();
            String password = "123456"; // Mật khẩu mặc định
            
            // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu chưa
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = '" + email + "'";
            ResultSet emailCheckResult = clientService.executeQuery(checkEmailSql);
            if (emailCheckResult != null && emailCheckResult.next() && emailCheckResult.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Email " + email + " đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                if (emailCheckResult != null) {
                    try { emailCheckResult.close(); } catch (Exception ex) { }
                }
                return;
            }
            if (emailCheckResult != null) {
                try { emailCheckResult.close(); } catch (Exception ex) { }
            }
            
            // Lấy next ID từ database để tránh trùng lập
            String getMaxIdSql = "SELECT MAX(id) FROM users";
            ResultSet maxIdResult = clientService.executeQuery(getMaxIdSql);
            int nextId = 1;
            if (maxIdResult != null && maxIdResult.next() && maxIdResult.getObject(1) != null) {
                nextId = maxIdResult.getInt(1) + 1;
            }
            if (maxIdResult != null) {
                try { maxIdResult.close(); } catch (Exception ex) { }
            }
            
            // Sử dụng câu lệnh SQL trực tiếp để thêm khách hàng vào database, không chỉ định ID
            String insertUserSql = "INSERT INTO users (email, username, password, full_name, phone, address, role) VALUES (" +
                "'" + email + "', '" + email + "', '" + password + "', '" + name + "', '" + phone + "', '" + address + "', 'CUSTOMER')";
            
            boolean success = clientService.executeCustomQuery(insertUserSql);
            
            // Lấy ID thật từ database sau khi insert
            int actualId = nextId;
            if (success) {
                String getUserIdSql = "SELECT id FROM users WHERE email = '" + email + "'";
                ResultSet userIdResult = clientService.executeQuery(getUserIdSql);
                if (userIdResult != null && userIdResult.next()) {
                    actualId = userIdResult.getInt("id");
                }
                if (userIdResult != null) {
                    try { userIdResult.close(); } catch (Exception ex) { }
                }
                
                // Format ID khách hàng
                String formattedId = "KH" + String.format("%03d", actualId);
                
                // Lưu thông tin thêm về loại khách hàng vào bảng khách hàng
                String customerTypeQuery = "INSERT INTO customer_type (customer_id, type) VALUES (" + actualId + ", '" + type + "')";
                clientService.executeCustomQuery(customerTypeQuery);
                
                // Thêm vào bảng hiển thị nếu thành công
                Object[] rowData = {formattedId, name, phone, email, address, type};
                customerTableModel.addRow(rowData);
                
                // Clear the form
                clearCustomerForm();
                
                JOptionPane.showMessageDialog(this, "Khách hàng đã được thêm thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng vào database.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // In ra console để debug
        }
    }
    
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần cập nhật");
            return;
        }
        
        // Validate input
        if (customerNameField.getText().isEmpty() || customerPhoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại khách hàng");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String id = customerIdField.getText();
            String name = customerNameField.getText();
            String phone = customerPhoneField.getText();
            String email = customerEmailField.getText();
            String address = customerAddressField.getText();
            String type = customerTypeComboBox.getSelectedItem().toString();
            
            // Tạo đối tượng User với role là customer
            User customer = new User();
            customer.setId(Integer.parseInt(id.replaceAll("\\D", ""))); // Chuyển KH001 thành 1
            customer.setFullName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            customer.setRole("CUSTOMER");
            
            // Cập nhật thông tin khách hàng vào database
            // Giả sử rằng có phương thức updateUser trong clientService
            boolean success = clientService.executeCustomQuery(
                "UPDATE users SET full_name = '" + name + "', phone = '" + phone + 
                "', email = '" + email + "', address = '" + address + "' WHERE id = " + customer.getId());
            
            // Cập nhật loại khách hàng
            if (success) {
                // Cập nhật loại khách hàng trong bảng customer_type
                String updateTypeQuery = "UPDATE customer_type SET type = '" + type + "' WHERE customer_id = " + customer.getId();
                clientService.executeCustomQuery(updateTypeQuery);
                
                // Cập nhật bảng hiển thị
                customerTableModel.setValueAt(id, selectedRow, 0);
                customerTableModel.setValueAt(name, selectedRow, 1);
                customerTableModel.setValueAt(phone, selectedRow, 2);
                customerTableModel.setValueAt(email, selectedRow, 3);
                customerTableModel.setValueAt(address, selectedRow, 4);
                customerTableModel.setValueAt(type, selectedRow, 5);
                
                // Clear the form
                clearCustomerForm();
                
                JOptionPane.showMessageDialog(this, "Khách hàng đã được cập nhật thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật khách hàng vào database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa khách hàng này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy ID khách hàng
                String customerId = customerTableModel.getValueAt(selectedRow, 0).toString();
                int id = Integer.parseInt(customerId.replaceAll("\\D", "")); // Chuyển KH001 thành 1
                
                // Xóa bản ghi loại khách hàng trong bảng customer_type
                String deleteTypeQuery = "DELETE FROM customer_type WHERE customer_id = " + id;
                boolean deleteTypeSuccess = clientService.executeCustomQuery(deleteTypeQuery);
                
                // Xóa khách hàng trong bảng users
                String deleteUserQuery = "DELETE FROM users WHERE id = " + id + " AND role = 'CUSTOMER'";
                boolean success = clientService.executeCustomQuery(deleteUserQuery);
                
                if (success) {
                    // Nếu xóa thành công thì cập nhật giao diện
                    customerTableModel.removeRow(selectedRow);
                    clearCustomerForm();
                    JOptionPane.showMessageDialog(this, "Khách hàng đã được xóa thành công và cập nhật trong database");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng khỏi database", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearCustomerForm() {
        customerIdField.setText("");
        customerNameField.setText("");
        customerPhoneField.setText("");
        customerEmailField.setText("");
        customerAddressField.setText("");
        customerTypeComboBox.setSelectedIndex(0);
        customerTable.clearSelection();
    }
    
    private void populateCustomerForm(int selectedRow) {
        customerIdField.setText(customerTableModel.getValueAt(selectedRow, 0).toString());
        customerNameField.setText(customerTableModel.getValueAt(selectedRow, 1).toString());
        customerPhoneField.setText(customerTableModel.getValueAt(selectedRow, 2).toString());
        customerEmailField.setText(customerTableModel.getValueAt(selectedRow, 3).toString());
        customerAddressField.setText(customerTableModel.getValueAt(selectedRow, 4).toString());
        customerTypeComboBox.setSelectedItem(customerTableModel.getValueAt(selectedRow, 5).toString());
    }
    
    // Order Management Methods
    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xem chi tiết");
            return;
        }
        
        String orderId = orderTableModel.getValueAt(selectedRow, 0).toString();
        
        // Create order details dialog
        JDialog detailsDialog = new JDialog(this, "Chi tiết hóa đơn " + orderId, true);
        detailsDialog.setSize(800, 500);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout());
        
        // Order info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(BACKGROUND_COLOR);
        
        infoPanel.add(new JLabel("Mã hóa đơn:"));
        infoPanel.add(new JLabel(orderId));
        
        infoPanel.add(new JLabel("Ngày lập:"));
        infoPanel.add(new JLabel(orderTableModel.getValueAt(selectedRow, 1).toString()));
        
        infoPanel.add(new JLabel("Khách hàng:"));
        infoPanel.add(new JLabel(orderTableModel.getValueAt(selectedRow, 2).toString()));
        
        infoPanel.add(new JLabel("Tổng tiền:"));
        infoPanel.add(new JLabel(orderTableModel.getValueAt(selectedRow, 3).toString()));
        
        infoPanel.add(new JLabel("Trạng thái:"));
        infoPanel.add(new JLabel(orderTableModel.getValueAt(selectedRow, 4).toString()));
        
        infoPanel.add(new JLabel("Nhân viên:"));
        infoPanel.add(new JLabel(orderTableModel.getValueAt(selectedRow, 5).toString()));
        
        // Order items panel
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        itemsPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel itemsTitle = new JLabel("Danh sách sản phẩm");
        itemsTitle.setFont(HEADER_FONT);
        itemsTitle.setHorizontalAlignment(JLabel.CENTER);
        itemsPanel.add(itemsTitle, BorderLayout.NORTH);
        
        // Create sample order details table
        String[] columnNames = {"STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Giá", "Thành tiền"};
        Object[][] data = new Object[0][0];
        
        // Add sample data for order details based on order ID
        if (orderId.equals("HD001")) {
            data = new Object[][]{
                {"1", "SP001", "Áo thun nam", "1", "250,000 VNĐ", "250,000 VNĐ"},
                {"2", "SP004", "Giày thể thao", "1", "100,000 VNĐ", "100,000 VNĐ"}
            };
        } else if (orderId.equals("HD002")) {
            data = new Object[][]{
                {"1", "SP002", "Quần jean nữ", "2", "450,000 VNĐ", "900,000 VNĐ"},
                {"2", "SP005", "Túi xách", "1", "220,000 VNĐ", "220,000 VNĐ"}
            };
        } else if (orderId.equals("HD003")) {
            data = new Object[][]{
                {"1", "SP003", "Váy dạ hội", "1", "650,000 VNĐ", "650,000 VNĐ"}
            };
        } else if (orderId.equals("HD004")) {
            data = new Object[][]{
                {"1", "SP001", "Áo thun nam", "1", "250,000 VNĐ", "250,000 VNĐ"},
                {"2", "SP002", "Quần jean nữ", "1", "450,000 VNĐ", "450,000 VNĐ"},
                {"3", "SP004", "Giày thể thao", "1", "150,000 VNĐ", "150,000 VNĐ"}
            };
        }
        
        DefaultTableModel detailsModel = new DefaultTableModel(data, columnNames);
        JTable detailsTable = new JTable(detailsModel);
        JScrollPane scrollPane = new JScrollPane(detailsTable);
        itemsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to dialog
        detailsDialog.add(infoPanel, BorderLayout.NORTH);
        detailsDialog.add(itemsPanel, BorderLayout.CENTER);
        
        // Add buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> detailsDialog.dispose());
        buttonPanel.add(closeButton);
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        detailsDialog.setVisible(true);
    }
    
    private void updateOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần cập nhật");
            return;
        }
        
        // Validate input
        if (orderCustomerIdField.getText().isEmpty() || orderDateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng và ngày lập");
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String orderId = orderIdField.getText();
            String orderDate = orderDateField.getText();
            String customerId = orderCustomerIdField.getText();
            String total = orderTotalField.getText().replaceAll("[^\\d.]", ""); // Lấy chỉ số từ chuỗi ví dụ "500,000 VNĐ"
            String status = orderStatusComboBox.getSelectedItem().toString();
            String staffId = orderStaffIdField.getText();
            
            // Tạo câu lệnh SQL để cập nhật hóa đơn
            int orderIdNumber = Integer.parseInt(orderId.replaceAll("\\D", "")); // Chuyển HD001 thành 1
            int customerIdNumber = Integer.parseInt(customerId.replaceAll("\\D", ""));
            int staffIdNumber = Integer.parseInt(staffId.replaceAll("\\D", ""));
            double totalAmount = Double.parseDouble(total);
            
            // Cập nhật hóa đơn trong database
            String updateOrderQuery = "UPDATE orders SET order_date = '" + orderDate + 
                "', customer_id = " + customerIdNumber + 
                ", total_amount = " + totalAmount + 
                ", status = '" + status + 
                "', staff_id = " + staffIdNumber + 
                " WHERE id = " + orderIdNumber;
            
            boolean success = clientService.executeCustomQuery(updateOrderQuery);
            
            if (success) {
                // Cập nhật bảng hiển thị nếu thành công
                orderTableModel.setValueAt(orderId, selectedRow, 0);
                orderTableModel.setValueAt(orderDate, selectedRow, 1);
                orderTableModel.setValueAt(customerId, selectedRow, 2); // Note: This should be customer name in a real app
                orderTableModel.setValueAt(totalAmount + " VNĐ", selectedRow, 3);
                orderTableModel.setValueAt(status, selectedRow, 4);
                orderTableModel.setValueAt(staffId, selectedRow, 5); // Note: This should be staff name in a real app
                
                // Clear the form
                clearOrderForm();
                
                JOptionPane.showMessageDialog(this, "Hóa đơn đã được cập nhật thành công và lưu vào database");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật hóa đơn vào database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa hóa đơn này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy ID hóa đơn
                String orderId = orderTableModel.getValueAt(selectedRow, 0).toString();
                int id = Integer.parseInt(orderId.replaceAll("\\D", "")); // Chuyển HD001 thành 1
                
                // Xóa chi tiết hóa đơn trước (vì có ràng buộc khóa ngoại)
                String deleteOrderDetailsQuery = "DELETE FROM order_details WHERE order_id = " + id;
                boolean deleteDetailsSuccess = clientService.executeCustomQuery(deleteOrderDetailsQuery);
                
                // Xóa hóa đơn trong database
                String deleteOrderQuery = "DELETE FROM orders WHERE id = " + id;
                boolean success = clientService.executeCustomQuery(deleteOrderQuery);
                
                if (success) {
                    // Nếu xóa thành công thì cập nhật giao diện
                    orderTableModel.removeRow(selectedRow);
                    clearOrderForm();
                    JOptionPane.showMessageDialog(this, "Hóa đơn đã được xóa thành công và cập nhật trong database");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa hóa đơn khỏi database", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void printOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để in");
            return;
        }
        
        String orderId = orderTableModel.getValueAt(selectedRow, 0).toString();
        JOptionPane.showMessageDialog(this, "In hóa đơn " + orderId + " thành công!");
    }
    
    private void clearOrderForm() {
        orderIdField.setText("");
        orderCustomerIdField.setText("");
        orderDateField.setText("");
        orderTotalField.setText("");
        orderStatusComboBox.setSelectedIndex(0);
        orderStaffIdField.setText("");
        orderTable.clearSelection();
    }
    
    private void populateOrderForm(int selectedRow) {
        orderIdField.setText(orderTableModel.getValueAt(selectedRow, 0).toString());
        orderDateField.setText(orderTableModel.getValueAt(selectedRow, 1).toString());
        orderCustomerIdField.setText(orderTableModel.getValueAt(selectedRow, 2).toString()); // This is customer name, not ID in the table
        orderTotalField.setText(orderTableModel.getValueAt(selectedRow, 3).toString());
        orderStatusComboBox.setSelectedItem(orderTableModel.getValueAt(selectedRow, 4).toString());
        orderStaffIdField.setText(orderTableModel.getValueAt(selectedRow, 5).toString()); // This is staff name, not ID in the table
    }
    
    // Statistics Management Methods
    private void searchStatistics() {
        try {
            // Get the search criteria
            String fromDate = fromDateField.getText();
            String toDate = toDateField.getText();
            String productType = productTypeComboBox.getSelectedItem().toString();
            String staff = staffComboBox.getSelectedItem().toString();
            
            // Validate date input
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày bắt đầu và ngày kết thúc");
                return;
            }
            
            // Clear existing data
            statsTableModel.setRowCount(0);
            
            // In a real app, this would search from the database via clientService
            // For now, add some sample filtered data based on criteria
            if (staff.equals("Tất cả") && productType.equals("Tất cả")) {
                // Show all data
                statsTableModel.addRow(new Object[]{"20/04/2025", "15", "15,850,000 VNĐ", "32", "Nguyễn Văn A"});
                statsTableModel.addRow(new Object[]{"21/04/2025", "18", "18,750,000 VNĐ", "38", "Trần Thị B"});
                statsTableModel.addRow(new Object[]{"22/04/2025", "12", "12,350,000 VNĐ", "25", "Lê Văn C"});
            } else if (!staff.equals("Tất cả") && productType.equals("Tất cả")) {
                // Filter by staff
                if (staff.equals("Nguyễn Văn A")) {
                    statsTableModel.addRow(new Object[]{"20/04/2025", "15", "15,850,000 VNĐ", "32", "Nguyễn Văn A"});
                } else if (staff.equals("Trần Thị B")) {
                    statsTableModel.addRow(new Object[]{"21/04/2025", "18", "18,750,000 VNĐ", "38", "Trần Thị B"});
                } else {
                    statsTableModel.addRow(new Object[]{"22/04/2025", "12", "12,350,000 VNĐ", "25", "Lê Văn C"});
                }
            } else if (staff.equals("Tất cả") && !productType.equals("Tất cả")) {
                // Filter by product type
                if (productType.equals("Áo")) {
                    statsTableModel.addRow(new Object[]{"20/04/2025", "7", "7,850,000 VNĐ", "15", "Nguyễn Văn A"});
                    statsTableModel.addRow(new Object[]{"21/04/2025", "9", "9,450,000 VNĐ", "18", "Trần Thị B"});
                } else if (productType.equals("Quần")) {
                    statsTableModel.addRow(new Object[]{"21/04/2025", "6", "5,400,000 VNĐ", "12", "Trần Thị B"});
                    statsTableModel.addRow(new Object[]{"22/04/2025", "5", "4,500,000 VNĐ", "10", "Lê Văn C"});
                } else {
                    statsTableModel.addRow(new Object[]{"20/04/2025", "3", "4,200,000 VNĐ", "5", "Nguyễn Văn A"});
                }
            } else {
                // Filter by both staff and product type (very specific)
                if (staff.equals("Nguyễn Văn A") && productType.equals("Áo")) {
                    statsTableModel.addRow(new Object[]{"20/04/2025", "7", "7,850,000 VNĐ", "15", "Nguyễn Văn A"});
                } else if (staff.equals("Trần Thị B") && productType.equals("Quần")) {
                    statsTableModel.addRow(new Object[]{"21/04/2025", "6", "5,400,000 VNĐ", "12", "Trần Thị B"});
                } else {
                    // No data for this specific combination
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu thống kê cho bộ lọc đã chọn");
                }
            }
            
            // Show success message if data was found
            if (statsTableModel.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this, "Tìm kiếm thành công, đã tìm thấy " + statsTableModel.getRowCount() + " kết quả");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm thống kê: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportStatistics() {
        if (statsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất báo cáo");
            return;
        }
        
        // Create a file chooser to select save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất báo cáo thống kê");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                // Ensure the file has the correct extension
                if (!selectedFile.getName().toLowerCase().endsWith(".xlsx")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".xlsx");
                }
                
                // In a real app, this would export data to an Excel file
                // For now, just show a success message
                JOptionPane.showMessageDialog(this, "Đã xuất báo cáo thành công tới " + selectedFile.getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void printStatistics() {
        if (statsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để in báo cáo");
            return;
        }
        
        try {
            // In a real app, this would print the statistics report
            // For now, just show a success message
            JOptionPane.showMessageDialog(this, "Đã gửi báo cáo thống kê tới máy in");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // For testing purposes, create with a mock service
            AdminDashboardFrame frame = new AdminDashboardFrame(null);
            frame.setVisible(true);
        });
    }
}
