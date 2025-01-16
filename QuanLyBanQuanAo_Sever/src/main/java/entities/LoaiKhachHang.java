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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LoaiKhachHang")
public class LoaiKhachHang implements Serializable{

    @Id
    @Column(name = "maLoaiKH", columnDefinition = "nvarchar(200)")
    private String maLoaiKH;

    @Column(name = "tenLoaiKH", columnDefinition = "nvarchar(200)")
    private String tenLoaiKH;

    // getters and setters
    public LoaiKhachHang(String maLoaiKH, String tenLoaiKH) {
		super();
		this.maLoaiKH = maLoaiKH;
		this.tenLoaiKH = tenLoaiKH;
	}

	public LoaiKhachHang() {
		super();
	}
	public LoaiKhachHang(String maLoaiKH) {
		this(maLoaiKH,"");
	}

	public String getMaLoaiKH() {
		return maLoaiKH;
	}

	public void setMaLoaiKH(String maLoaiKH) {
		this.maLoaiKH = maLoaiKH;
	}

	public String getTenLoaiKH() {
		return tenLoaiKH;
	}

	public void setTenLoaiKH(String tenLoaiKH) {
		this.tenLoaiKH = tenLoaiKH;
	}
}
