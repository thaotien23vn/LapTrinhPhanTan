package entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "SanPham")
public class SanPham implements Serializable{

    @Id
    @Column(name = "maSanPham", columnDefinition = "nvarchar(200)")
    private String maSanPham;

    @Column(name = "tenSanPham", columnDefinition = "nvarchar(200)")
    private String tenSanPham;

    @Column(name = "donGia")
    private double donGia;

    @Column(name = "moTa", columnDefinition = "nvarchar(200)")
    private String moTa;

    @Column(name = "tacGia", columnDefinition = "nvarchar(200)")
    private String tacgia;

    @ManyToOne
    @JoinColumn(name = "maNCC")
    private NhaCungCap nhacungcap;

    @ManyToOne
    @JoinColumn(name = "maLoaiSanPham")
    private LoaiSanPham loaisanpham;

    @Column(name = "trangThai")
    private boolean trangThai;

    @Column(name = "urlAnh", columnDefinition = "nvarchar(200)")
    private String urlAnh;
    
    

    public SanPham() {
		super();
	}   
	
	public SanPham(String maSach, String tenSach, double donGia, String moTa, String tacgia, NhaCungCap nhacungcap,
			LoaiSanPham loaisanpham, boolean trangThai, String urlAnh) {
		super();
		this.maSanPham = maSach;
		this.tenSanPham = tenSach;
		this.donGia = donGia;
		this.moTa = moTa;
		this.tacgia = tacgia;
		this.nhacungcap = nhacungcap;
		this.loaisanpham = loaisanpham;
		this.trangThai = trangThai;
		this.urlAnh = urlAnh;
	}


	public SanPham(String maSanPham, String tenSanPham) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
	}

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

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getTacgia() {
		return tacgia;
	}

	public void setTacgia(String tacgia) {
		this.tacgia = tacgia;
	}

	public NhaCungCap getNhacungcap() {
		return nhacungcap;
	}

	public void setNhacungcap(NhaCungCap nhacungcap) {
		this.nhacungcap = nhacungcap;
	}

	public LoaiSanPham getLoaisanpham() {
		return loaisanpham;
	}

	public void setLoaisanpham(LoaiSanPham loaisanpham) {
		this.loaisanpham = loaisanpham;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getUrlAnh() {
		return urlAnh;
	}

	public void setUrlAnh(String urlAnh) {
		this.urlAnh = urlAnh;
	}
	
	

}
