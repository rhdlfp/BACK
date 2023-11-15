package com.example.springsocial.bakery.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.springsocial.bakery.model.Bakery;
import com.example.springsocial.bakery.model.BakeryMenu;
import com.example.springsocial.bakery.model.BakeryPhoto;

public class BakeryDetails {
    private Long id;
    private String name;
    private String gujuso;
    private String detailejuso;
    private String businesshours;
    private String mainphoto;
    private List<String> photonames;
    private List<MenuDetail> menus;

    public BakeryDetails(Bakery bakery, List<BakeryPhoto> photos, List<BakeryMenu> menus) {
        this.id = bakery.getId();
        this.name = bakery.getName();
        this.gujuso = bakery.getGujuso();
        this.detailejuso = bakery.getDetailejuso();
        this.businesshours = bakery.getBusinesshours();
        this.mainphoto = bakery.getMainphoto();

        this.photonames = photos.stream().map(BakeryPhoto::getPhotoname).collect(Collectors.toList());

        this.menus = menus.stream()
            .map(menu -> new MenuDetail(menu.getMenuname(), menu.getPrice()))
            .collect(Collectors.toList());
    }

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

	public List<String> getPhotonames() {
		return photonames;
	}

	public void setPhotonames(List<String> photonames) {
		this.photonames = photonames;
	}

	public List<MenuDetail> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuDetail> menus) {
		this.menus = menus;
	}
    
}


