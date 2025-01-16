package entities;

import java.io.Serializable;
import java.util.Date;

import entities.NhanVien;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable{

    @Id
    @Column(name = "maHoaDon", columnDefinition = "nvarchar(200)")
    private String maHD;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maNV")
    private NhanVien nguoiLapHD;

    @Column(name = "ngayLapHoaDon")
    @Temporal(TemporalType.DATE)
    private Date ngayLapHD;

    @Column(name = "uuDai")
    private boolean uuDai;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maSanPham")
    private SanPham tenSanPham;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @Column(name = "tongTien")
    private Double tongTien;

    @Transient
    private double sum;

    @Transient
    private int soHoaDon;

    @Column(name = "trangThai")
    private boolean trangThai;
    
    public HoaDon() {
        
    }

   
    
    public HoaDon(String maHD, NhanVien nguoiLapHD, Date ngayLapHD, boolean uuDai, SanPham tenSanPham, KhachHang khachHang, Double tongTien) {
        this.maHD = maHD;
        this.nguoiLapHD = nguoiLapHD;
        this.ngayLapHD = ngayLapHD;
        this.uuDai = uuDai;
        this.tenSanPham = tenSanPham;
        this.khachHang = khachHang;
        this.tongTien = tongTien;
    }

    
	
	public HoaDon(NhanVien nguoiLapHD) {
		super();
		this.nguoiLapHD = nguoiLapHD;
	}


	public HoaDon(String maHD, boolean uuDai) {
		super();
		this.maHD = maHD;
		this.uuDai = uuDai;
	}
	
	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public HoaDon(Double sum)
	{
		super();
		this.sum=sum;
	}
	public HoaDon(String maHD) {
		this(maHD, true);
	}
	
	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public HoaDon(int soHoaDon)
	{
		super();
		this.soHoaDon=soHoaDon;
	}
	public int getSoHoaDon() {
		return soHoaDon;
	}

	public void setSoHoaDon(int soHoaDon) {
		this.soHoaDon = soHoaDon;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public NhanVien getNguoiLapHD() {
		return nguoiLapHD;
	}
	public void setNguoiLapHD(NhanVien nguoiLapHD) {
		this.nguoiLapHD = nguoiLapHD;
	}
	
	public Date getNgayLapHD() {
		return ngayLapHD;
	}

	public void setNgayLapHD(Date ngayLapHD) {
		this.ngayLapHD = ngayLapHD;
	}

	public boolean isUuDai() {
		return uuDai;
	}
	public void setUuDai(boolean uuDai) {
		this.uuDai = uuDai;
	}
	public SanPham getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(SanPham tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}
}
