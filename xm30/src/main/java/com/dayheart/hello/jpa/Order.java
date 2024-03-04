package com.dayheart.hello.jpa;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7879692942569601545L;

	@Id
	@Column(nullable = false, unique = true, name = "order_num")
	private long OrderNum;
	
	@Column(nullable = false, name = "order_date")
	private Date orderDate;
	
	@Column(nullable = false, name = "cust")
	private int cust; // cust_num
	
	@Column(name = "rep")
	private int rep; // empl_num
	
	@Column(nullable = false, name = "MFR")
	private String mfr; // mfr_id
	
	@Column(nullable = false, name = "PRODUCT")
	private String product; // product_id
	
	@Column(nullable = false, name = "QTY")
	private int qty; // 
	
	@Column(nullable = false, name = "AMOUNT")
	private double amount; // money
	
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(long orderNum, Date orderDate, int cust, int rep, String mfr, String product, int qty, double amount) {
		super();
		OrderNum = orderNum;
		this.orderDate = orderDate;
		this.cust = cust;
		this.rep = rep;
		this.mfr = mfr;
		this.product = product;
		this.qty = qty;
		this.amount = amount;
	}

	public Order(long orderNum, Date orderDate, int cust, String mfr, String product, int qty, double amount) {
		super();
		OrderNum = orderNum;
		this.orderDate = orderDate;
		this.cust = cust;
		this.mfr = mfr;
		this.product = product;
		this.qty = qty;
		this.amount = amount;
	}
	
	
	public long getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(long orderNum) {
		OrderNum = orderNum;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getCust() {
		return cust;
	}

	public void setCust(int cust) {
		this.cust = cust;
	}

	public int getRep() {
		return rep;
	}

	public void setRep(int rep) {
		this.rep = rep;
	}

	public String getMfr() {
		return mfr;
	}

	public void setMfr(String mfr) {
		this.mfr = mfr;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (OrderNum ^ (OrderNum >>> 32));
		result = prime * result + ((mfr == null) ? 0 : mfr.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		Order other = (Order) obj;
		if (OrderNum != other.OrderNum)
			return false;
		if (mfr == null) {
			if (other.mfr != null)
				return false;
		} else if (!mfr.equals(other.mfr))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [OrderNum=" + OrderNum + ", orderDate=" + orderDate + ", cust=" + cust + ", rep=" + rep + ", mfr="
				+ mfr + ", product=" + product + ", qty=" + qty + ", amount=" + amount + "]";
	}
	
	
}
