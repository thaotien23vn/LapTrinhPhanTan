# Hệ Thống Bán Quần Áo - Mô Hình Client-Server (Socket)

## Giới Thiệu

Hệ thống bán quần áo sử dụng mô hình Client-Server với giao tiếp thông qua Socket. Hệ thống cho phép khách hàng duyệt sản phẩm, thêm vào giỏ hàng và đặt hàng thông qua ứng dụng desktop. Máy chủ sẽ xử lý yêu cầu, quản lý dữ liệu sản phẩm và đơn hàng bằng MariaDB.

## Công Nghệ Sử Dụng

- **Ngôn ngữ lập trình:** Java (Client - JavaFX/Swing, Server - Java)
- **Giao tiếp mạng:** Java Socket
- **Cơ sở dữ liệu:** MariaDB
- **Mô hình:** Client-Server

## Cấu Trúc Dự Án

```
├── app
│   ├── server (Máy chủ xử lý yêu cầu từ client)
│   ├── client (Ứng dụng desktop giao diện khách hàng)
│   ├── context
│   ├── controller
│   ├── dao
│   ├── dto
│   ├── entities
│   ├── services
│   ├── utils
└── README.md
```

## Chức Năng Chính

### 1. Máy Chủ (Server)

- Khởi động máy chủ, lắng nghe kết nối từ client.
- Quản lý danh sách sản phẩm.
- Xử lý đơn hàng từ client.
- Cập nhật dữ liệu vào cơ sở dữ liệu.

### 2. Ứng Dụng Khách Hàng (Client)

- Hiển thị danh sách sản phẩm từ server.
- Thêm sản phẩm vào giỏ hàng.
- Gửi yêu cầu đặt hàng.
- Nhận phản hồi từ server.
Subproject By CollabCrew- Member Group:1/Trần Thảo Tiên, 2/Nguyễn Cao Anh Minh, 3/Dương Trọng Tiến


