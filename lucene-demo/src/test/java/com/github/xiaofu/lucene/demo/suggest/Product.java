package com.github.xiaofu.lucene.demo.suggest;

import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String image;
	private String[] regions;
	private int numberSold;
 
	public Product(String name, String image, String[] regions, int numberSold) {
		this.name = name;
		this.image = image;
		this.regions = regions;
		this.numberSold = numberSold;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public String getImage() {
		return image;
	}
 
	public void setImage(String image) {
		this.image = image;
	}
 
	public String[] getRegions() {
		return regions;
	}
 
	public void setRegions(String[] regions) {
		this.regions = regions;
	}
 
	public int getNumberSold() {
		return numberSold;
	}
 
	public void setNumberSold(int numberSold) {
		this.numberSold = numberSold;
	}

}
