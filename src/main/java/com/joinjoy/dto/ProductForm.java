package com.joinjoy.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductForm {
	
	private String id;
	
	private String name;
	
	private String imageUrl;
	
	private BigDecimal quantity;
	
	private BigDecimal price;
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public BigDecimal getQuantity() {
		return quantity;
	}



	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}



	public BigDecimal getPrice() {
		return price;
	}



	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	public ProductForm() {
		// TODO Auto-generated constructor stub
		
		
		
	}

}
