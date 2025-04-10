package org.client.ui;

import org.client.app.ApplicationManager;
import org.client.services.ClientService;
import org.client.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private ClientService clientService;
    
    public LoginFrame(ClientService clientService) {
        this.clientService = clientService;
        // Set up the frame
        setTitle("Trang đăng nhập");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Không kết thúc ứng dụng khi đóng cửa sổ
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with a light beige background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 248, 220)); // Light beige color
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Store name label with custom font and color
        JLabel storeNameLabel = new JLabel("CỬA HÀNG THỜI TRANG");
        storeNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        storeNameLabel.setForeground(new Color(199, 21, 63)); // Dark pink color
        storeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel brandLabel = new JLabel("COLLABCREW");
        brandLabel.setFont(new Font("Arial", Font.BOLD, 24));
        brandLabel.setForeground(new Color(199, 21, 63)); // Dark pink color
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login information label
        JLabel loginInfoLabel = new JLabel("Thông Tin Đăng Nhập");
        loginInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginInfoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Email field with icon
        JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
        emailPanel.setOpaque(false);
        JLabel emailIconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/user_icon.png")));
        if (emailIconLabel.getIcon() == null) {
            emailIconLabel = new JLabel("👤");
        }
        emailField = new JTextField(20);
        emailField.setMargin(new Insets(8, 8, 8, 8));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        // Add placeholder behavior
        TextPrompt emailPrompt = new TextPrompt("Email", emailField);
        emailPrompt.changeAlpha(0.7f);
        emailPrompt.changeStyle(Font.ITALIC);
        
        emailPanel.add(emailIconLabel, BorderLayout.WEST);
        emailPanel.add(emailField, BorderLayout.CENTER);
        
        // Password field with icon
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        JLabel passwordIconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/lock_icon.png")));
        if (passwordIconLabel.getIcon() == null) {
            passwordIconLabel = new JLabel("🔒");
        }
        passwordField = new JPasswordField(20);
        passwordField.setMargin(new Insets(8, 8, 8, 8));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        // Add placeholder behavior
        TextPrompt passwordPrompt = new TextPrompt("Mật Khẩu", passwordField);
        passwordPrompt.changeAlpha(0.7f);
        passwordPrompt.changeStyle(Font.ITALIC);
        
        // Add show/hide password button
        JButton showPasswordButton = new JButton("👁️");
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setBorderPainted(false);
        showPasswordButton.setContentAreaFilled(false);
        showPasswordButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar('●');
            } else {
                passwordField.setEchoChar((char) 0);
            }
        });
        
        passwordPanel.add(passwordIconLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);
        
        // Forgot password button
        forgotPasswordButton = new JButton("QUÊN MẬT KHẨU");
        forgotPasswordButton.setForeground(new Color(0, 123, 255)); // Blue color
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(e -> {
            // Mở giao diện quên mật khẩu thông qua ApplicationManager
            ApplicationManager.getInstance().showForgotPasswordScreen();
            // Không cần dispose() vì ApplicationManager sẽ xử lý
        });
                
        // Register button
        JButton registerButton = new JButton("ĐĂNG KÝ TÀI KHOẢN MỚI");
        registerButton.setForeground(new Color(0, 123, 255)); // Blue color
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> {
            // Mở giao diện đăng ký thông qua ApplicationManager
            ApplicationManager.getInstance().showRegisterScreen();
            // Không cần dispose() vì ApplicationManager sẽ xử lý
        });
        
        // Login button
        loginButton = new JButton("Đăng Nhập");
        loginButton.setBackground(new Color(199, 21, 63)); // Dark pink color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // ClientService được truyền từ ApplicationManager
        
        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Email và mật khẩu không được để trống", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Call client service to authenticate
                    User user = clientService.login(email, password);
                    
                    if (user != null) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Navigate based on role
                        if (user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN")) {
                            // Mở giao diện quản trị viên thông qua ApplicationManager
                            ApplicationManager.getInstance().showAdminDashboard();
                        } else {
                            // Mở giao diện nhân viên bán hàng thông qua ApplicationManager
                            ApplicationManager.getInstance().showStaffPOS();
                        }
                        
                        // Không cần dispose() vì ApplicationManager sẽ xử lý
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, 
                                "Đăng nhập thất bại! Kiểm tra lại email và mật khẩu.", 
                                "Lỗi Đăng Nhập", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Connection error
                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            "Không thể kết nối đến máy chủ. Vui lòng thử lại sau.",
                            "Lỗi kết nối",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        // Add components to main panel
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(storeNameLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(brandLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(loginInfoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(passwordPanel);
        
        // Panel for forgot password (right-aligned)
        JPanel forgotPasswordPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        forgotPasswordPanel.setOpaque(false);
        forgotPasswordPanel.add(forgotPasswordButton);
        mainPanel.add(forgotPasswordPanel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(registerButton);
        mainPanel.add(Box.createVerticalGlue());
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    // Phương thức main đã được chuyển vào ClientApp
}
