package org.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Màn hình chào mừng hiển thị khi khởi động ứng dụng
 */
public class SplashScreen extends JWindow {
    // Callback interface to notify when splash screen is finished
    public interface SplashListener {
        void onSplashFinished();
    }
    
    private SplashListener listener;
    private static final int DISPLAY_TIME = 5000; // 5 giây
    private static final Color PRIMARY_COLOR = new Color(199, 21, 63); // Màu chủ đạo
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    
    private JProgressBar progressBar;
    private Timer progressTimer;
    private int progressValue = 0;
    
    public SplashScreen(SplashListener listener) {
        this.listener = listener;
        // Thiết lập kích thước và vị trí
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Tạo panel chính với BorderLayout
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Bật tính năng anti-aliasing cho hiển thị mượt hơn
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Vẽ nền
                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Vẽ đường viền
                g2d.setColor(PRIMARY_COLOR);
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new RoundRectangle2D.Float(10, 10, getWidth() - 20, getHeight() - 20, 20, 20));
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setContentPane(mainPanel);
        
        // Panel cho logo và tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout(0, 20));
        headerPanel.setOpaque(false);
        
        // Logo của cửa hàng
        JLabel logoLabel = new JLabel("\uD83D\uDC55", SwingConstants.CENTER); // Biểu tượng quần áo
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 80));
        logoLabel.setForeground(PRIMARY_COLOR);
        
        // Tiêu đề chào mừng
        JLabel titleLabel = new JLabel("CỬA HÀNG QUẢN LÝ QUẦN ÁO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        // Thông điệp chào mừng
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        // Tạo panel cho welcomeLabel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setOpaque(false);
        welcomePanel.add(welcomeLabel);
        
        // Thêm các thành phần vào headerPanel
        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(welcomePanel, BorderLayout.SOUTH);
        
        // Tạo thanh tiến trình
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setString("Đang tải...");
        
        // Thêm các panel vào mainPanel
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);
        
        // Timer cho thanh tiến trình
        progressTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressValue += 2;
                if (progressValue <= 100) {
                    progressBar.setValue(progressValue);
                    if (progressValue < 30) {
                        progressBar.setString("Đang khởi tạo...");
                    } else if (progressValue < 60) {
                        progressBar.setString("Đang tải dữ liệu...");
                    } else if (progressValue < 90) {
                        progressBar.setString("Chuẩn bị hoàn tất...");
                    } else {
                        progressBar.setString("Hoàn tất!");
                    }
                } else {
                    progressTimer.stop();
                }
            }
        });
    }
    
    /**
     * Hiển thị splash screen và sau đó thông báo hoàn thành
     */
    public void showSplash() {
        setVisible(true);
        
        // Bắt đầu timer của thanh tiến trình
        progressTimer.start();
        
        // Chỉ cho phép gọi onSplashFinished một lần
        final boolean[] callbackCalled = {false};
        
        // Timer để đóng splash sau 5 giây
        new Timer(DISPLAY_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop(); // Dừng timer ngay lập tức
                
                if (!callbackCalled[0]) {
                    callbackCalled[0] = true;
                    dispose();
                    // Thông báo cho listener khi splash screen kết thúc
                    if (listener != null) {
                        SwingUtilities.invokeLater(() -> listener.onSplashFinished());
                    }
                }
            }
        }).start();
    }
    
    
}
