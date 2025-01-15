package entities;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table (name = "NhanVien")


// JPQL Queries
@NamedQueries ({
    @NamedQuery (name = "NhanVien.findAll", query = "SELECT nv FROM NhanVien nv"),
    @NamedQuery (name = "NhanVien.findByMaNhanVien", query = "SELECT nv FROM NhanVien nv WHERE nv.maNhanVien = :maNhanVien"),
    @NamedQuery (name = "NhanVien.findByTenNhanVien", query = "SELECT nv FROM NhanVien nv WHERE nv.tenNhanVien = :tenNhanVien"),
})


public class NhanVien implements Serializable {
	private static final long serialVersionUID = 1L;

	// Attributes
	@Id
	@Column (name = "maNhanVien", columnDefinition = "VARCHAR(20)", unique = true, nullable = false)
	private String maNhanVien;
	
	@Column (name = "tenNhanVien", columnDefinition = "NVARCHAR(255)")
	private String tenNhanVien;
	
	@Column (name = "ngaySinh", columnDefinition = "DATE")
	private LocalDate ngaySinh;
	
	@Column (name = "gioiTinh", columnDefinition = "NVARCHAR(10)")
	private String gioiTinh;
	
	@Column (name = "soDienThoai", columnDefinition = "VARCHAR(20)")
	private String soDienThoai;
	
	@Column (name = "email", columnDefinition = "VARCHAR(255)")
	private String email;
	
	@Column (name = "diaChi", columnDefinition = "NVARCHAR(255)")
	private String diaChi;
	
	@Column (name = "chucVu", columnDefinition = "NVARCHAR(255)")
	private String chucVu;
	
	@Column (name = "trangThaiLamViec", columnDefinition = "NVARCHAR(255)")
	private String trangThaiLamViec;
	
	@Column (name = "trinhDo", columnDefinition = "NVARCHAR(255)")
	private String trinhDo;
	
	@Column (name = "TenDangNhap", columnDefinition = "NVARCHAR(255)")
	private String TenDangNhap;
	
	@Column (name = "matKhau", columnDefinition = "NVARCHAR(255)")
	private String matKhau;
	
	
	// Relationships
	@OneToMany (mappedBy = "nhanVien")
	private Set<HoaDon> hoaDon;
	
	
//	@OneToMany (mappedBy = "nhanVien")
//	private Set<PhieuDatHang> phieuDatHang;
	
	
	// Constructors
	public NhanVien (String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	
	
	public NhanVien (String maNhanVien, String tenNhanVien) {
		this.maNhanVien = maNhanVien;
		this.tenNhanVien = tenNhanVien;
	}
	
	
	public NhanVien (String maNhanVien, String tenNhanVien, LocalDate ngaySinh, String gioiTinh, String soDienThoai,
			String email, String diaChi, String chucVu, String trangThaiLamViec, String trinhDo, String tenDangNhap,
			String matKhau) {
		this.maNhanVien = maNhanVien;
		this.tenNhanVien = tenNhanVien;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.diaChi = diaChi;
		this.chucVu = chucVu;
		this.trangThaiLamViec = trangThaiLamViec;
		this.trinhDo = trinhDo;
		this.TenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
	}
	
	
	// toString
	@Override
	public String toString() {
		return String.format("NhanVien [maNhanVien = %s, tenNhanVien = %s, ngaySinh = %s, gioiTinh = %s, "
						+ "soDienThoai = %s, email = %s, diaChi = %s, chucVu = %s, trangThaiLamViec = %s, "
						+ "trinhDo = %s, TenDangNhap = %s, matKhau = %s]",
				maNhanVien, tenNhanVien, ngaySinh, gioiTinh, soDienThoai, email, 
				diaChi, chucVu, trangThaiLamViec, trinhDo, TenDangNhap, matKhau);
	}
}