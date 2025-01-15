package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SanPham")
public class SanPham implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maSanPham", columnDefinition = "VARCHAR(20)", unique = true, nullable = false)
    private String maSanPham;

    @Column(name = "tenSanPham", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String tenSanPham;

    @Column(name = "giaBan", nullable = false)
    private Double giaBan;

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "moTa", columnDefinition = "TEXT")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "maNhaCungCap", referencedColumnName = "maNhaCungCap", nullable = false)
    private NhaCungCap nhaCungCap;

    // Getters and setters
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Double giaBan) {
        this.giaBan = giaBan;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    // Override toString()
    @Override
    public String toString() {
        return String.format("SanPham [maSanPham=%s, tenSanPham=%s, giaBan=%s, soLuong=%s, moTa=%s, nhaCungCap=%s]",
                maSanPham, tenSanPham, giaBan, soLuong, moTa, nhaCungCap.getMaNhaCungCap());
    }
}
