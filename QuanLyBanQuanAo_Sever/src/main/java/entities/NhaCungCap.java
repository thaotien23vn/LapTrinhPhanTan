package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "NhaCungCap")
@NamedQueries({
        @NamedQuery(name = "NhaCungCap.findAll", query = "SELECT ncc FROM NhaCungCap ncc"),
        @NamedQuery(name = "NhaCungCap.findByMaNhaCungCap", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.maNhaCungCap = :maNhaCungCap"),
        @NamedQuery(name = "NhaCungCap.findByTenNhaCungCap", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNhaCungCap LIKE :tenNhaCungCap"),
        @NamedQuery(name = "NhaCungCap.findByEmail", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.email = :email")
})
public class NhaCungCap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maNhaCungCap", columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String maNhaCungCap;

    @Column(name = "tenNhaCungCap", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String tenNhaCungCap;

    @Column(name = "diaChi", columnDefinition = "NVARCHAR(255)")
    private String diaChi;

    @Column(name = "soDienThoai", columnDefinition = "VARCHAR(20)", nullable = false)
    private String soDienThoai;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SanPham> sanPhamList;

    // Constructors
    public NhaCungCap() {
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String diaChi, String soDienThoai, String email) {
        this.maNhaCungCap = maNhaCungCap;
        this.tenNhaCungCap = tenNhaCungCap;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // Getters and Setters
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    public void setSanPhamList(List<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    // Override toString
    @Override
    public String toString() {
        return String.format(
                "NhaCungCap [maNhaCungCap=%s, tenNhaCungCap=%s, diaChi=%s, soDienThoai=%s, email=%s]",
                maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email
        );
    }
}
