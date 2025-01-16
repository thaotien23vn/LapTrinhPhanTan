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
@Table(name = "LoaiNhanVien")
public class LoaiNhanVien implements Serializable{

    @Id
    @Column(name = "maLoaiNV",columnDefinition = "nvarchar(200)")
    private String maLoaiNV;

    @Column(name = "chucVu", columnDefinition = "nvarchar(200)")
    private String chucVu;

    public LoaiNhanVien(String maLoaiNV, String chucVu) {
		super();
		this.maLoaiNV = maLoaiNV;
		this.chucVu = chucVu;
	}
	
	public LoaiNhanVien() {
		super();
	}
	public LoaiNhanVien(String maLoaiNV) {
		this(maLoaiNV,"");
	}
	public String getMaLoaiNV() {
		return maLoaiNV;
	}
	public void setMaLoaiNV(String maLoaiNV) {
		this.maLoaiNV = maLoaiNV;
	}
	public String getChucVu() {
		return chucVu;
	}
	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}
	
}
