package com.dayheart.hello.repository.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dayheart.hello.repository.MissedColumnTypeException;
import com.dayheart.hello.repository.UsingJdbcTemplate;

@Repository
public class UsingJdbcTemplateRepository implements UsingJdbcTemplate {
	
	@Autowired
	@Qualifier(value = "indexedParamJdbcTempate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier(value = "namedParamJdbcTemplate")
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;
	
	

	@Override
	public List<Map<String, ?>> executeQuery(String sql, Object[] args, int[] argTypes) {
		// TODO Auto-generated method stub
		try {
			return jdbcTemplate.query(sql, args, argTypes, new RowMapperImpl());
		} catch ( DataAccessException e)  { 
			throw e;
		}
	}


	@Override
	public List<Map<String, ?>> executeQuery(String sql, Map<String, ?> paramMap) {
		// TODO Auto-generated method stub
		try {
			return namedParamJdbcTemplate.query(sql, paramMap, new RowMapperImpl());
		} catch ( DataAccessException e)  {
			throw e;
		}
	}


	@Override
	public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) {
		// TODO Auto-generated method stub
		try {
			return namedParamJdbcTemplate.queryForObject(sql, paramMap, requiredType);
		} catch ( DataAccessException e)  { 
			throw e;
		}
	}


	@Override
	public void executeUpdate(String sql, Map<String, ?> paramMap) {
		// TODO Auto-generated method stub
		
	}
	
	
	private static final class RowMapperImpl implements RowMapper<Map<String, ?>> {

		@Override
		public Map<String, ?> mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//System.out.println(rsmd.getColumnCount());
			
			Map<String, Object> row = new HashMap<String, Object>();
			
			int colCnt = rsmd.getColumnCount();
			for(int i=1; i<=colCnt; i++) {
				String columnLabel = rsmd.getColumnLabel(i);
				int colType = rsmd.getColumnType(i);
				String missedColumnType = null;
				
				//System.out.println("columnLabel:" + columnLabel + ", type:" + i);
				
				switch(colType) {
				case java.sql.Types.BIGINT:
					row.put(columnLabel, rs.getLong(i));
					break;
				case java.sql.Types.NUMERIC:
				case java.sql.Types.DECIMAL:
					row.put(columnLabel, rs.getBigDecimal(i));
					break;
				case java.sql.Types.CHAR:
				case java.sql.Types.VARCHAR:
					row.put(columnLabel, rs.getString(i));
					break;
				case java.sql.Types.INTEGER:
				case java.sql.Types.SMALLINT:
					row.put(columnLabel, rs.getInt(i));
					break;
				case java.sql.Types.FLOAT:
				case java.sql.Types.REAL:
					row.put(columnLabel, rs.getFloat(i));
					break;
				case java.sql.Types.DOUBLE:
					row.put(columnLabel, rs.getDouble(i));
					break;
				case java.sql.Types.DATE:
					row.put(columnLabel, rs.getDate(i));
					break;
				case java.sql.Types.TIME:
					row.put(columnLabel, rs.getTime(i));
					break;
				case java.sql.Types.TIMESTAMP:
					row.put(columnLabel, rs.getTimestamp(i));
					break;
				case java.sql.Types.BOOLEAN:
				case java.sql.Types.BIT:
					row.put(columnLabel, rs.getBoolean(i));
					break;
/*
 * com.dayheart.tr.exception.MissedColumnTypeException
	at com.dayheart.tr.domain.repository.impl.ImxdbRepositoryPg$IMXDBMapper.mapRow(ImxdbRepositoryPg.java:114)
	at com.dayheart.tr.domain.repository.impl.ImxdbRepositoryPg$IMXDBMapper.mapRow(ImxdbRepositoryPg.java:1)
 */
				case java.sql.Types.BINARY:
				case java.sql.Types.VARBINARY:
				case java.sql.Types.LONGVARBINARY:
					row.put(columnLabel, rs.getBytes(i));
					break;
				default:
					System.out.println("default case -> columnLabel:" + columnLabel + ", type:" + colType);
					row.put(columnLabel, rsmd.getColumnTypeName(i));
					missedColumnType = rsmd.getColumnTypeName(i);
					System.out.println("default case -> columnLabel:" + columnLabel + ", missedColumnType:" + missedColumnType);
					if(missedColumnType != null)
						throw new MissedColumnTypeException(columnLabel, missedColumnType);
					break;
				}
			}

			return row;
		}
		
	}

}
