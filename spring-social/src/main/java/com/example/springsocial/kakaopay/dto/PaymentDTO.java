package com.example.springsocial.kakaopay.dto;

public class PaymentDTO {
    private Long id;
    private String storeName;
    private String storeAddress;
    private String  price;
    private String status;
    
    public PaymentDTO() {
        // 기본 생성자
    }
    
    public PaymentDTO(Long id, String storeName, String storeAddress, String price, String status) {
        this.id = id;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.price = price;
        this.status = status;
    }



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    
}
