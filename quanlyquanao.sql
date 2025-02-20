USE [quanlyquanao]
GO
/****** Object:  Table [dbo].[hoadon]    Script Date: 1/18/2025 16:03:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[hoadon](
	[maHoaDon] [nvarchar](200) NOT NULL,
	[ngayLapHoaDon] [date] NULL,
	[tongTien] [float] NULL,
	[trangThai] [bit] NULL,
	[uuDai] [bit] NULL,
	[maKH] [nvarchar](200) NULL,
	[maNV] [nvarchar](200) NULL,
	[maSanPham] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maHoaDon] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[khachhang]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[khachhang](
	[maKH] [nvarchar](200) NOT NULL,
	[SDT] [nvarchar](200) NULL,
	[diaChi] [nvarchar](200) NULL,
	[email] [nvarchar](200) NULL,
	[gioiTinh] [bit] NULL,
	[tenKH] [nvarchar](200) NULL,
	[maLoaiKH] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[loaikhachhang]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[loaikhachhang](
	[maLoaiKH] [nvarchar](200) NOT NULL,
	[tenLoaiKH] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoaiKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[loainhanvien]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[loainhanvien](
	[maLoaiNV] [nvarchar](200) NOT NULL,
	[chucVu] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoaiNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[loaisanpham]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[loaisanpham](
	[maLoaiSanPham] [nvarchar](200) NOT NULL,
	[tenLoaiSanPham] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoaiSanPham] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[nhacungcap]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[nhacungcap](
	[maNCC] [nvarchar](200) NOT NULL,
	[tenNCC] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maNCC] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[nhanvien]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[nhanvien](
	[maNV] [nvarchar](200) NOT NULL,
	[CCCD] [nvarchar](200) NULL,
	[SDT] [nvarchar](200) NULL,
	[diaChi] [nvarchar](200) NULL,
	[email] [nvarchar](200) NULL,
	[gioiTinh] [bit] NULL,
	[matKhau] [nvarchar](200) NULL,
	[ngaySinh] [datetime] NULL,
	[ngayVaoLam] [datetime] NULL,
	[tenNV] [nvarchar](200) NULL,
	[trangThai] [bit] NULL,
	[urlAnhNV] [nvarchar](200) NULL,
	[maLoaiNV] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[quanly]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[quanly](
	[tenTK] [nvarchar](200) NOT NULL,
	[matKhau] [nvarchar](200) NULL,
	[tenQuanLy] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[tenTK] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[sanpham]    Script Date: 1/18/2025 16:03:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[sanpham](
	[maSanPham] [nvarchar](200) NOT NULL,
	[donGia] [float] NULL,
	[moTa] [nvarchar](200) NULL,
	[tacGia] [nvarchar](200) NULL,
	[tenSanPham] [nvarchar](200) NULL,
	[trangThai] [bit] NULL,
	[urlAnh] [nvarchar](200) NULL,
	[maLoaiSanPham] [nvarchar](200) NULL,
	[maNCC] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[maSanPham] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[hoadon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_KhachHang] FOREIGN KEY([maKH])
REFERENCES [dbo].[khachhang] ([maKH])
GO
ALTER TABLE [dbo].[hoadon] CHECK CONSTRAINT [FK_HoaDon_KhachHang]
GO
ALTER TABLE [dbo].[hoadon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_NhanVien] FOREIGN KEY([maNV])
REFERENCES [dbo].[nhanvien] ([maNV])
GO
ALTER TABLE [dbo].[hoadon] CHECK CONSTRAINT [FK_HoaDon_NhanVien]
GO
ALTER TABLE [dbo].[hoadon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_SanPham] FOREIGN KEY([maSanPham])
REFERENCES [dbo].[sanpham] ([maSanPham])
GO
ALTER TABLE [dbo].[hoadon] CHECK CONSTRAINT [FK_HoaDon_SanPham]
GO
ALTER TABLE [dbo].[khachhang]  WITH CHECK ADD  CONSTRAINT [FK_KhachHang_LoaiKhachHang] FOREIGN KEY([maLoaiKH])
REFERENCES [dbo].[loaikhachhang] ([maLoaiKH])
GO
ALTER TABLE [dbo].[khachhang] CHECK CONSTRAINT [FK_KhachHang_LoaiKhachHang]
GO
ALTER TABLE [dbo].[nhanvien]  WITH CHECK ADD  CONSTRAINT [FK_NhanVien_LoaiNhanVien] FOREIGN KEY([maLoaiNV])
REFERENCES [dbo].[loainhanvien] ([maLoaiNV])
GO
ALTER TABLE [dbo].[nhanvien] CHECK CONSTRAINT [FK_NhanVien_LoaiNhanVien]
GO
ALTER TABLE [dbo].[sanpham]  WITH CHECK ADD  CONSTRAINT [FK_SanPham_LoaiSanPham] FOREIGN KEY([maLoaiSanPham])
REFERENCES [dbo].[loaisanpham] ([maLoaiSanPham])
GO
ALTER TABLE [dbo].[sanpham] CHECK CONSTRAINT [FK_SanPham_LoaiSanPham]
GO
ALTER TABLE [dbo].[sanpham]  WITH CHECK ADD  CONSTRAINT [FK_SanPham_NhaCungCap] FOREIGN KEY([maNCC])
REFERENCES [dbo].[nhacungcap] ([maNCC])
GO
ALTER TABLE [dbo].[sanpham] CHECK CONSTRAINT [FK_SanPham_NhaCungCap]
GO
