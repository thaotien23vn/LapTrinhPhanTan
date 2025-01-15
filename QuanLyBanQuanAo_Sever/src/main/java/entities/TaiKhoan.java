package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TaiKhoan")
@NamedQueries({
        @NamedQuery(name = "TaiKhoan.findAll", query = "SELECT tk FROM TaiKhoan tk"),
        @NamedQuery(name = "TaiKhoan.findByTenDangNhap", query = "SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap"),
        @NamedQuery(name = "TaiKhoan.findByLoaiTaiKhoan", query = "SELECT tk FROM TaiKhoan tk WHERE tk.loaiTaiKhoan = :loaiTaiKhoan")
})
public class TaiKhoan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tenDangNhap", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "matKhau", columnDefinition = "VARCHAR(255)", nullable = false)
    private String matKhau;

    @Column(name = "loaiTaiKhoan", columnDefinition = "VARCHAR(50)")
    private String loaiTaiKhoan;

    // Constructors
    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String loaiTaiKhoan) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public TaiKhoan(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    // Getters and Setters
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    // Override toString
    @Override
    public String toString() {
        return String.format("TaiKhoan [tenDangNhap=%s, matKhau=%s, loaiTaiKhoan=%s]",
                tenDangNhap, matKhau, loaiTaiKhoan);
    }
}
