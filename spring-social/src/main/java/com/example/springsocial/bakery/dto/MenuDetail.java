package com.example.springsocial.bakery.dto;

public class MenuDetail {
    private String menuname;
    private Long price;

    public MenuDetail(String menuname, Long price) {
        this.menuname = menuname;
        this.price = price;
    }

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
    
    
}