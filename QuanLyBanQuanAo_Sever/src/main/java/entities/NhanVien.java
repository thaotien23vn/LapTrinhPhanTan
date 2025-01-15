package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "NhanVien")
@NamedQueries({
        @NamedQuery(name = "NhanVien.findAll", query = "SELECT nv FROM NhanVien nv"),
        @NamedQuery(name = "NhanVien.findByMaNV", query = "SELECT nv FROM NhanVien nv WHERE nv.maNV = :maNV"),
        @NamedQuery(name = "NhanVien.findByTenNV", query = "SELECT nv FROM NhanVien nv WHERE nv.tenNV LIKE :tenNV")
})
@Getter
@Setter
@NoArgsConstructor

@ToString
public class NhanVien implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maNV", columnDefinition = "VARCHAR(20)", unique = true, nullable = false)
    private String maNV;

    @Column(name = "tenNV", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String tenNV;

    @Column(name = "chucVu", columnDefinition = "VARCHAR(50)")
    private String chucVu;

    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "soDienThoai", columnDefinition = "VARCHAR(20)")
    private String soDienThoai;

    @Column(name = "trinhDo", columnDefinition = "VARCHAR(50)")
    private String trinhDo;

    @Column(name = "tongTienLuong", columnDefinition = "DOUBLE")
    private Double tongTienLuong;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taiKhoan", referencedColumnName = "tenDangNhap")
    private TaiKhoan taiKhoan;

    // Constructor chỉ chứa các trường bắt buộc
    public NhanVien(String maNV, String tenNV) {
        this.maNV = maNV;
        this.tenNV = tenNV;
    }

    // Constructor đầy đủ
    public NhanVien(String maNV, String tenNV, String chucVu, String email, String soDienThoai, String trinhDo, Double tongTienLuong, TaiKhoan taiKhoan) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.trinhDo = trinhDo;
        this.tongTienLuong = tongTienLuong;
        this.taiKhoan = taiKhoan;
    }

    // Override toString() để dễ dàng hiển thị thông tin nhân viên
    @Override
    public String toString() {
        return String.format("NhanVien [maNV=%s, tenNV=%s, chucVu=%s, email=%s, soDienThoai=%s, trinhDo=%s, tongTienLuong=%s]",
                maNV, tenNV, chucVu, email, soDienThoai, trinhDo, tongTienLuong);
    }
}
