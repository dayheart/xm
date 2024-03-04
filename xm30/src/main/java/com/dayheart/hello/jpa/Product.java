package com.dayheart.hello.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "products")
@IdClass(ProductId.class)
public class Product implements Serializable {

	private static final long serialVersionUID = 6886884333104273789L;
	
	@Id
	@Column(name = "mfr_id", nullable = false)
	private String mfrId;
	
	@Id
	@Column(name = "product_id", nullable = false)
	private String productId;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "price", nullable = false)
	private double price;
	
	@Column(name = "qty_on_hand", nullable = false)
	private int qtyOnHand;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Product(String mfrId, String prodcutId, String description, double price, int qtyOnHand) {
		super();
		this.mfrId = mfrId;
		this.productId = prodcutId;
		this.description = description;
		this.price = price;
		this.qtyOnHand = qtyOnHand;
	}



	public String getMfrId() {
		return mfrId;
	}

	public void setMfrId(String mfrId) {
		this.mfrId = mfrId;
	}

	public String getProdcutId() {
		return productId;
	}

	public void setProdcutId(String prodcutId) {
		this.productId = prodcutId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQtyOnHand() {
		return qtyOnHand;
	}

	public void setQtyOnHand(int qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}

	@Override
	public String toString() {
		return "Product [mfrId=" + mfrId + ", prodcutId=" + productId + ", description=" + description + ", price="
				+ price + ", qtyOnHand=" + qtyOnHand + "]";
	}
	
	
	
}
