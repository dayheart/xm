package com.inzent.igate.connector;

import com.inzent.igate.core.exception.IGateException;

public class IGateConnectorException extends IGateException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5758992248579235899L;
	public final String systemId;
	public final String adapterId;
	public final String connectorId;
	
	public IGateConnectorException(Connector con, String code, String s2, String ...strings) {
		this(con, null, code, s2, strings);
	}
	
	public IGateConnectorException(Connector con, Throwable t, String code, String s2, String ...strings) {
		super(t, code, s2, strings);
		this.systemId = con.getSystemId();
		this.adapterId = con.getAdapterId();
		this.connectorId = con.getId();
	}

}
