package com.dayheart.hello.jpa;

import java.io.Serializable;
import java.util.Objects;

public class ProductId implements Serializable {
	
	private static final long serialVersionUID = 4503837753815573388L;
	private String mfrId;
	private String productId;
	
	public ProductId() { }

	public ProductId(String mfrId, String productId) {
		super();
		this.mfrId = mfrId;
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mfrId, productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductId other = (ProductId) obj;
		return Objects.equals(mfrId, other.mfrId) && Objects.equals(productId, other.productId);
	}

	@Override
	public String toString() {
		return "ProductId [mfrId=" + mfrId + ", productId=" + productId + "]";
	}

	public String getMfrId() {
		return mfrId;
	}

	public void setMfrId(String mfrId) {
		this.mfrId = mfrId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	

}
