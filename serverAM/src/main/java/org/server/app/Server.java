package org.server.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.server.controllers.ClientHandler;
import org.server.utils.EntityManagerFactoryUtil;

import jakarta.persistence.EntityManager;

public class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton startServerButton;
	private JButton stopServerButton;
	private JTextArea logArea;
	private ServerSocket serverSocket;
	private List<ClientHandler> clients = new CopyOnWriteArrayList<ClientHandler>();
	private EntityManagerFactoryUtil mangerFactoryUtil;
	private EntityManager entityManager;
	private ExecutorService executorService;
	private volatile boolean running = true; // Điều khiển vòng lặp server


	public Server() {

		setTitle("QLqa Server");
		logArea = new JTextArea(16, 50);
		logArea.setEditable(false);
		startServerButton = new JButton("Start Server");
		stopServerButton = new JButton("Stop Server");
		stopServerButton.setEnabled(false);

		startServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});

		stopServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopServer();
			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(logArea), BorderLayout.CENTER);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(startServerButton, BorderLayout.NORTH);
		getContentPane().add(stopServerButton, BorderLayout.WEST);
		pack();
	}

	private void startServer() {
	    new Thread(() -> {
	        final int PORT = 5000;
	        try {
	            this.mangerFactoryUtil = new EntityManagerFactoryUtil();
	            this.entityManager = this.mangerFactoryUtil.getEnManager();
	            serverSocket = new ServerSocket(PORT);
	            executorService = Executors.newCachedThreadPool();
	            running = true;

	            logArea.append("Server is listening on port " + PORT + "\n");
	            startServerButton.setEnabled(false);
	            stopServerButton.setEnabled(true);

	            while (running) {  // Kiểm tra biến running để có thể dừng server
	                Socket clientSocket = serverSocket.accept();
	                logArea.append("Listen from: " + clientSocket.getInetAddress().getHostAddress() + "\n");

	                ClientHandler clientHandler = new ClientHandler(clientSocket, logArea, entityManager);
	                clients.add(clientHandler);
	                executorService.execute(clientHandler);
	            }

	        } catch (IOException e) {
	            if (running) { // Nếu không phải do stop server thì in lỗi
	                logArea.append("Server error: " + e.getMessage() + "\n");
	                e.printStackTrace();
	            }
	        }
	    }).start();
	}


	private void stopServer() {
	    running = false; // Thoát vòng lặp while(true)

	    try {
	        if (serverSocket != null && !serverSocket.isClosed()) {
	            serverSocket.close();
	        }

	        for (ClientHandler client : clients) {
	            client.closeConnection(); // Đóng tất cả kết nối client
	        }
	        clients.clear(); // Xóa danh sách clients

	        if (executorService != null && !executorService.isShutdown()) {
	            executorService.shutdownNow(); // Dừng tất cả thread
	        }

	        startServerButton.setEnabled(true);
	        stopServerButton.setEnabled(false);
	        logArea.append("Server stopped.\n");

	    } catch (IOException e) {
	        logArea.append("Could not stop the server.\n");
	        e.printStackTrace();
	    }
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Server server = new Server();
				server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				server.setVisible(true);
			}
		});
	}

}