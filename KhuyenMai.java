package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @Column(name = "maKhuyenMai", nullable = false)
    private String maKhuyenMai;

    @Column(name = "loaiMa", nullable = false)
    private String loaiMa;

    @Column(name = "giaTri", nullable = false)
    private float giaTri;

    @Column(name = "mucGiam", nullable = false)
    private float mucGiam;

    @Column(name = "ngayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    // Getters and setters
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getLoaiMa() {
        return loaiMa;
    }

    public void setLoaiMa(String loaiMa) {
        this.loaiMa = loaiMa;
    }

    public float getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(float giaTri) {
        this.giaTri = giaTri;
    }

    public float getMucGiam() {
        return mucGiam;
    }

    public void setMucGiam(float mucGiam) {
        this.mucGiam = mucGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
