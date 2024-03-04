package com.dayheart.hello.service.impl;

import java.sql.Types;
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
public class UsingJdbcTemplateOfficeService implements UsingJdbcTemplateService {

	@Autowired
	private UsingJdbcTemplateRepository jdbcTemplate;
	
	@Override
	public List<Map<String, ?>> findAll() {
		// TODO Auto-generated method stub
		String sql = "-- USING JdbcTemplate \n" + 
						"SELECT * FROM offices s";
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
		
		String sql = "-- USING JdbcTemplate \n" + 
					"WITH product_orders AS (\n"
					+ "  SELECT ORDER_NUM , REP , MFR , PRODUCT  FROM ORDERS o WHERE o.MFR = ? AND o.PRODUCT =  ? \n"
					+ "), order_salesreps AS ( SELECT s.EMPL_NUM, s.NAME, s.REP_OFFICE, po.MFR, po.PRODUCT  FROM SALESREPS s RIGHT JOIN product_orders po ON s.EMPL_NUM = po.rep \n"
					+ "), salesrep_offices AS (SELECT o.OFFICE , o.CITY , o.REGION , o.MGR , o.TARGET , o.SALES  FROM OFFICES o RIGHT JOIN order_salesreps os ON o.OFFICE  = os.rep_office ) \n"
					+ "SELECT so.OFFICE , so.CITY , so.REGION , so.MGR , s.NAME MANAGER , so.TARGET , so.SALES FROM salesrep_offices so LEFT JOIN SALESREPS s ON so.mgr = s.EMPL_NUM";
		/*
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mfrId", mfrId);
		params.put("productId", productId);
		return jdbcTemplate.executeQuery(sql, params);
		*/		
		return jdbcTemplate.executeQuery(sql, new Object[]{mfrId, productId}, new int[]{Types.VARCHAR, Types.VARCHAR});
	}

}
