package entities;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor


@Entity
@Table (name = "NhaCungCap")


// JPQL Queries
@NamedQueries({ 
	@NamedQuery(name = "NhaCungCap.findAll", query = "SELECT ncc FROM NhaCungCap ncc"),
	@NamedQuery(name = "NhaCungCap.findByMaNCC", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.maNCC = :maNCC"),
	@NamedQuery(name = "NhaCungCap.findByTenNCC", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNCC = :tenNCC"),
})


public class NhaCungCap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "maNCC", columnDefinition = "VARCHAR(20)", unique = true, nullable = false)
	private String maNCC;
	
	@Column (name = "tenNCC", columnDefinition = "NVARCHAR(255)")
	private String tenNCC;
	
	@Column (name = "sdt", columnDefinition = "VARCHAR(20)")
	private String Sdt;
	
	@Column (name = "email", columnDefinition = "VARCHAR(255)")
	private String email;
	
	@Column (name = "diaChi", columnDefinition = "NVARCHAR(255)")
	private String diaChi;
	
	@OneToMany (mappedBy = "nhaCungCap")
	private Set<SanPham> sanPham;
	
	
	// Constructors
	public NhaCungCap (String maNCC, String tenNCC, String sdt, String email, String diaChi) {
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.Sdt = sdt;
		this.email = email;
		this.diaChi = diaChi;
	}
	
	
	public NhaCungCap (String maNCC) {
		this.maNCC = maNCC;
	}
	
	
	public NhaCungCap (String maNCC, String tenNCC) {
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
	}
	
	
	// toString
	@Override
	public String toString() {
		return String.format("NhaCungCap [maNCC = %s, tenNCC = %s, sdt = %s, email = %s, diaChi = %s]", 
			maNCC, tenNCC, Sdt, email, diaChi);
	}

}
