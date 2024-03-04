package com.dayheart.hello.repository;

public class MissedColumnTypeException extends RuntimeException {

	private static final long serialVersionUID = -2860101960173170610L;
	private String colunmTypeName;
	private String columnName;
	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	public MissedColumnTypeException(String columnName, String columnTypeName) {
		super();
		this.columnName = columnName;
		this.colunmTypeName = columnTypeName;
	}
	
	public String getColumnTypeName() {
		return colunmTypeName;
	}

}
