package com.example.springsocial.kakaopay.entity;

import java.util.List;

import javax.persistence.*;
import com.example.springsocial.model.User;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tid;
    private String StoreName;
    private String StoreAddress;
    private String price;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계 설정
    @JoinColumn(name = "userid") // 외래 키로 연결할 컬럼명
    private User user;
    
    // ON DELETE CASCADE 설정을 통한 자식 엔터티 관리
    @OneToMany(mappedBy = "payment", cascade = CascadeType.REMOVE)
    private List<BuyList> buyLists;
    
    public List<BuyList> getBuyLists() {
        return buyLists;
    }

    public void setBuyLists(List<BuyList> buyLists) {
        this.buyLists = buyLists;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public String getStoreAddress() {
		return StoreAddress;
	}

	public void setStoreAddress(String storeAddress) {
		StoreAddress = storeAddress;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}

