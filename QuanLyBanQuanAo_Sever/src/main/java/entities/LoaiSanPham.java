package entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "LoaiSanPham")
public class LoaiSanPham implements Serializable{

    @Id
    @Column(name = "maLoaiSanPham", columnDefinition = "nvarchar(200)")
    private String maLoaiSanPham;

    @Column(name = "tenLoaiSanPham", columnDefinition = "nvarchar(200)")
    private String tenLoaiSanPham;

	
	public LoaiSanPham(String maLoaiSanPham, String tenLoaiSanPham) {
		super();
		this.maLoaiSanPham = maLoaiSanPham;
		this.tenLoaiSanPham = tenLoaiSanPham;
	}
	
	public LoaiSanPham() {
		super();
	}

	public LoaiSanPham(String maLoaiSanPham) {
		this(maLoaiSanPham, "");
	}

	public String getMaLoaiSanPham() {
		return maLoaiSanPham;
	}

	public void setMaLoaiSanPham(String maLoaiSanPham) {
		this.maLoaiSanPham = maLoaiSanPham;
	}

	public String getTenLoaiSanPham() {
		return tenLoaiSanPham;
	}

	public void setTenLoaiSanPham(String tenLoaiSanPham) {
		this.tenLoaiSanPham = tenLoaiSanPham;
	}
	
}
