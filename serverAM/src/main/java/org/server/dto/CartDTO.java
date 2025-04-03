package org.server.dto;

import java.util.List;

public class CartDTO {
    private Long customerId;
    private List<CartItemDTO> items;

    public CartDTO() {}

    public CartDTO(Long customerId, List<CartItemDTO> items) {
        this.customerId = customerId;
        this.items = items;
    }

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "CartDTO [customerId=" + customerId + ", items=" + items + "]";
	}

    
    
}
