package com.dayheart.hello.repository;

import java.util.List;
import java.util.Map;

public interface UsingJdbcTemplate {

	public List<Map<String, ?>> executeQuery(String sql, Object[] args, int[] argTypes);
	public List<Map<String, ?>> executeQuery(String sql, Map<String, ?> paramMap);
	public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType);
	public void executeUpdate(String sql, Map<String, ?> paramMap);
}
