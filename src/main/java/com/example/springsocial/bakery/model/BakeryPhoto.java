package com.example.springsocial.bakery.model;

import javax.persistence.*;

@Entity
@Table(name = "bakeryphoto")
public class BakeryPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bakeryid")
    private Bakery bakery;

    @Column(length = 255)
    private String photoname;

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

	public String getPhotoname() {
		return photoname;
	}

	public void setPhotoname(String photoname) {
		this.photoname = photoname;
	}

    
}
