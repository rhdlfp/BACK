package com.example.springsocial.bakery.model;

import javax.persistence.*;

@Entity
@Table(name = "bakery")
public class Bakery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String gujuso;

    @Column(length = 255)
    private String detailejuso;

    @Column(length = 255)
    private String businesshours;

    @Column(length = 255)
    private String mainphoto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGujuso() {
		return gujuso;
	}

	public void setGujuso(String gujuso) {
		this.gujuso = gujuso;
	}

	public String getDetailejuso() {
		return detailejuso;
	}

	public void setDetailejuso(String detailejuso) {
		this.detailejuso = detailejuso;
	}

	public String getBusinesshours() {
		return businesshours;
	}

	public void setBusinesshours(String businesshours) {
		this.businesshours = businesshours;
	}

	public String getMainphoto() {
		return mainphoto;
	}

	public void setMainphoto(String mainphoto) {
		this.mainphoto = mainphoto;
	}

    
}
