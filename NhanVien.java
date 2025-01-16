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
@Table(name = "NhanVien")
public class NhanVien implements Serializable{

    @Id
    @Column(name = "maNV", columnDefinition = "nvarchar(200)")
    private String maNV;

    @Column(name = "tenNV", columnDefinition = "nvarchar(200)")
    private String tenNV;

    @ManyToOne
    @JoinColumn(name = "maLoaiNV")
    private LoaiNhanVien loaiNV;

    @Column(name = "CCCD", columnDefinition = "nvarchar(200)")
    private String CCCD;

    @Column(name = "SDT", columnDefinition = "nvarchar(200)")
    private String SDT;

    @Column(name = "ngaySinh")
    private Date ngaySinh;

    @Column(name = "ngayVaoLam")
    private Date ngayVaoLam;

    @Column(name = "diaChi", columnDefinition = "nvarchar(200)")
    private String diaChi;

    @Column(name = "email", columnDefinition = "nvarchar(200)")
    private String email;

    @Column(name = "gioiTinh")
    private boolean gioiTinh;

    @Column(name = "matKhau", columnDefinition = "nvarchar(200)")
    private String matKhau;

    @Column(name = "trangThai")
    private boolean trangThai;

    @Column(name = "urlAnhNV",columnDefinition = "nvarchar(200)")
    private String urlAnhNV;

    public NhanVien(String maNV, String tenNV, LoaiNhanVien loaiNV, String cCCD, String sDT, Date ngaySinh,
			Date ngayVaoLam, String diaChi, String email, boolean gioiTinh, String matKhau, boolean trangThai, String urlAnhNV) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.loaiNV = loaiNV;
		CCCD = cCCD;
		SDT = sDT;
		this.ngaySinh = ngaySinh;
		this.ngayVaoLam = ngayVaoLam;
		this.diaChi = diaChi;
		this.email = email;
		this.gioiTinh = gioiTinh;
		this.matKhau = matKhau;
		this.trangThai=trangThai;
		this.urlAnhNV=urlAnhNV;
	}

	public NhanVien(String maNV, String tenNV, String urlAnhNV) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.urlAnhNV=urlAnhNV;
	}
	public NhanVien(String maNV, boolean trangThai) {
		super();
		this.maNV = maNV;
		this.trangThai=trangThai;
	}
	public NhanVien(String matKhau, String maNV) {
		super();
		this.matKhau=matKhau;
		this.maNV = maNV;
	}
	
	
	public NhanVien() {
		super();
	}
	
	
	
/*	public NhanVien(String tenNV) {
		super();
		this.tenNV = tenNV;
	}*/
	
	

	public String getUrlAnhNV() {
		return urlAnhNV;
	}

	public NhanVien(String maNV) {
		super();
		this.maNV = maNV;
	}

	public void setUrlAnhNV(String urlAnhNV) {
		this.urlAnhNV = urlAnhNV;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}

	public LoaiNhanVien getLoaiNV() {
		return loaiNV;
	}

	public void setLoaiNV(LoaiNhanVien loaiNV) {
		this.loaiNV = loaiNV;
	}

	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}

	public String getSDT() {
		return SDT;
	}

	public void setSDT(String sDT) {
		SDT = sDT;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public Date getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(Date ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
}
