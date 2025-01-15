package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "KhachHang")
@NamedQueries({
        @NamedQuery(name = "KhachHang.findAll", query = "SELECT kh FROM KhachHang kh"),
        @NamedQuery(name = "KhachHang.findByMaKhachHang", query = "SELECT kh FROM KhachHang kh WHERE kh.maKhachHang = :maKhachHang"),
        @NamedQuery(name = "KhachHang.findByTenKhachHang", query = "SELECT kh FROM KhachHang kh WHERE kh.tenKhachHang LIKE :tenKhachHang"),
        @NamedQuery(name = "KhachHang.findByEmail", query = "SELECT kh FROM KhachHang kh WHERE kh.email = :email")
})
public class KhachHang implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maKhachHang", columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String maKhachHang;

    @Column(name = "tenKhachHang", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String tenKhachHang;

    @Column(name = "gioiTinh", columnDefinition = "VARCHAR(10)")
    private String gioiTinh;

    @Column(name = "soDienThoai", columnDefinition = "VARCHAR(20)")
    private String soDienThoai;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String email;

    @Column(name = "ngaySinh", columnDefinition = "DATE")
    private LocalDate ngaySinh;

    // Constructors
    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String tenKhachHang, String gioiTinh, String soDienThoai, String email, LocalDate ngaySinh) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngaySinh = ngaySinh;
    }

    public KhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    // Getters and setters
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    // Override toString
    @Override
    public String toString() {
        return String.format(
                "KhachHang [maKhachHang=%s, tenKhachHang=%s, gioiTinh=%s, soDienThoai=%s, email=%s, ngaySinh=%s]",
                maKhachHang, tenKhachHang, gioiTinh, soDienThoai, email, ngaySinh
        );
    }
}
