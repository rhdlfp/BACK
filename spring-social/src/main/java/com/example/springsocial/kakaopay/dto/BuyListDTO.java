package com.example.springsocial.kakaopay.dto;

public class BuyListDTO {
    private Long id;
    private String menuName;
    private String quantity;
    private String menuPrice;
    

    public BuyListDTO(Long id, String menuName, String quantity, String menuPrice) {
        this.id = id;
        this.menuName = menuName;
        this.quantity = quantity;
        this.menuPrice = menuPrice;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}
    
    
}
