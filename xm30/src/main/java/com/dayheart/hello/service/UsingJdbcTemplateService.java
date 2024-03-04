package com.dayheart.hello.service;

import java.util.List;
import java.util.Map;

import com.dayheart.hello.domain.Product;

public interface UsingJdbcTemplateService {
	
	public List<Map<String, ?>> findAll();
	
	public List<Map<String, ?>> retrieveByProduct(String mfrId, String productId);

}
