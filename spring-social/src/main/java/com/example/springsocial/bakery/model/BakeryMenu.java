package com.example.springsocial.bakery.model;

import javax.persistence.*;

@Entity
@Table(name = "bakerymenu")
public class BakeryMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bakeryid")
    private Bakery bakery;

    @Column(length = 255)
    private String menuname;

    private Long price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Bakery getBakery() {
		return bakery;
	}

	public void setBakery(Bakery bakery) {
		this.bakery = bakery;
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

    // Constructors, getters, setters, and other methods
}

