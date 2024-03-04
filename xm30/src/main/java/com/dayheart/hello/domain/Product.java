package com.dayheart.hello.domain;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2717457891770001149L;
	private String mfrId;
	private String productId;
	private String description;
	private long price;
	private int qtyOnHand;
	
	/*
	@JsonIgnore
	private MultipartFile photo;
	public MultipartFile getPhoto() {
		return photo;
	}
	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
	*/
	
	/**
	 * @return the mfrId
	 */
	public String getMfrId() {
		return mfrId;
	}
	/**
	 * @param mfrId the mfrId to set
	 */
	public void setMfrId(String mfr_id) {
		this.mfrId = mfr_id;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String product_id) {
		this.productId = product_id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the price
	 */
	public long getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(long price) {
		this.price = price;
	}
	/**
	 * @return the qtyOnHand
	 */
	public int getQtyOnHand() {
		return qtyOnHand;
	}
	/**
	 * @param qtyOnHand the qtyOnHand to set
	 */
	public void setQtyOnHand(int qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mfrId == null) ? 0 : mfrId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (mfrId == null) {
			if (other.mfrId != null)
				return false;
		} else if (!mfrId.equals(other.mfrId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Product [mfrId=" + mfrId + ", productId=" + productId + ", description=" + description + "]";
	}
}

