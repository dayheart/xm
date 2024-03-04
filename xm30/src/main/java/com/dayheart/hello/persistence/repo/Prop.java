package com.dayheart.hello.persistence.repo;

public class Prop {
	public String name;
	public String value;
	
	public Prop(String name, String value) {
		this.name = name;
		this.value = value;
	}

	// for JSTL, no need on Thymeleaf
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
