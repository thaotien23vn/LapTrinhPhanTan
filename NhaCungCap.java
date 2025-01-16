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

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "NhaCungCap")
public class NhaCungCap implements Serializable{

    @Id
    @Column(name = "maNCC", columnDefinition = "nvarchar(200)")
    private String maNCC;

    @Column(name = "tenNCC", columnDefinition = "nvarchar(200)")
    private String tenNCC;

	public NhaCungCap(String maNCC, String tenNCC) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
	}
	public NhaCungCap() {
		super();
	}
	public NhaCungCap(String maNCC) {
		this(maNCC,"");
	}
	public String getMaNCC() {
		return maNCC;
	}
	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
	}
	public String getTenNCC() {
		return tenNCC;
	}
	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}
	
}
