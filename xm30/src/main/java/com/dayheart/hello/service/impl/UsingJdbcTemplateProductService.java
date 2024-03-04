package com.dayheart.hello.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dayheart.hello.domain.Product;
import com.dayheart.hello.repository.UsingJdbcTemplate;
import com.dayheart.hello.repository.impl.UsingJdbcTemplateRepository;
import com.dayheart.hello.service.UsingJdbcTemplateService;

@Service
public class UsingJdbcTemplateProductService implements UsingJdbcTemplateService {

	@Autowired
	private UsingJdbcTemplateRepository jdbcTemplate;
	
	@Override
	public List<Map<String, ?>> findAll() {
		// TODO Auto-generated method stub
		String sql = "-- USING JdbcTemplate \r\n" + 
						"SELECT * FROM products p";
		Map<String, Object> params = new HashMap<>();
		return jdbcTemplate.executeQuery(sql, params);
	}

	@Override
	public List<Map<String, ?>> retrieveByProduct(String mfrId, String productId) {
		// TODO Auto-generated method stub
		
		//System.out.println(String.format("%s-%s", mfrId, productId));
		
		/*
		 * org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0
		 */
		
		String sql = "-- USING JdbcTemplate \r\n" + 
					"SELECT mfr_id as mfrId, product_id as productId, description, price, qty_on_hand as qtyOnHand FROM products where mfr_id = :mfrId and product_id = :productId";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mfrId", mfrId);
		params.put("productId", productId);
		
		return jdbcTemplate.executeQuery(sql, params);
	}

}
