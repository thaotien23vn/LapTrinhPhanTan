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

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "QuanLy")
public class QuanLy implements Serializable{

    @Id
    @Column(name = "tenTK", columnDefinition = "nvarchar(200)")
    private String tenTK;

    @Column(name = "matKhau", columnDefinition = "nvarchar(200)")
    private String matKhau;

    @Column(name = "tenQuanLy", columnDefinition = "nvarchar(200)")
    private String tenQuanLy;

    public QuanLy(String tenTK, String matKhau, String tenQuanLy) {
		super();
		this.tenTK = tenTK;
		this.matKhau = matKhau;
		this.tenQuanLy = tenQuanLy;
	}
    
    
	public QuanLy() {
		super();
	}


	public QuanLy(String tenTK, String matKhau) {
		super();
		this.tenTK = tenTK;
		this.matKhau = matKhau;
	}
	public QuanLy(String tenTK)
	{
		this(tenTK,"");
	}
	public String getTenTK() {
		return tenTK;
	}
	public void setTenTK(String tenTK) {
		this.tenTK = tenTK;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public String getTenQuanLy() {
		return tenQuanLy;
	}
	public void setTenQuanLy(String tenQuanLy) {
		this.tenQuanLy = tenQuanLy;
	}
}