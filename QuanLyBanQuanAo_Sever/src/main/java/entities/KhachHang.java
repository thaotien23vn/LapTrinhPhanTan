package entities;

import java.io.Serializable;

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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "KhachHang")
public class KhachHang implements Serializable{

    @Id
    @Column(name = "maKH",columnDefinition = "nvarchar(200)")
    private String maKH;

    @Column(name = "tenKH", columnDefinition = "nvarchar(200)")
    private String tenKH;

    @Column(name = "SDT", columnDefinition = "nvarchar(200)")
    private String SDT;

    @Column(name = "email", columnDefinition = "nvarchar(200)")
    private String email;

    @ManyToOne
    @JoinColumn(name = "maLoaiKH")
    private LoaiKhachHang loaiKH;

    @Column(name = "gioiTinh")
    private boolean gioiTinh;

    @Column(name = "diaChi", columnDefinition = "nvarchar(200)")
    private String diaChi;

    public KhachHang(String maKH, String tenKH, String sDT, String email, LoaiKhachHang khachHang, boolean gioiTinh,
			String diaChi) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		SDT = sDT;
		this.email = email;
		this.loaiKH = khachHang;
		this.gioiTinh = gioiTinh;
		this.diaChi = diaChi;
	}
    
   


	public KhachHang(String maKH) {
		super();
		this.maKH = maKH;
	}


	public KhachHang() {
		super();
	}
	public KhachHang(String maKH, String tenKH)
	{
		super();
	}
	/*public KhachHang(String maKH)
	{
		this.maKH=maKH;
	}*/
	public String getMaKH() {
		return maKH;
	}
	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}
	public String getTenKH() {
		return tenKH;
	}
	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}
	public String getSDT() {
		return SDT;
	}
	public void setSDT(String sDT) {
		SDT = sDT;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LoaiKhachHang getLoaiKhachHang() {
		return loaiKH;
	}
	public void setLoaiKhachHang(LoaiKhachHang khachHang) {
		this.loaiKH = khachHang;
	}
	public boolean isGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
}
