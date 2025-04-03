package org.server.dto;

public class CartItemDTO {
	
    private Long productId;
    private int quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "CartItemDTO [productId=" + productId + ", quantity=" + quantity + "]";
	}

    
}
