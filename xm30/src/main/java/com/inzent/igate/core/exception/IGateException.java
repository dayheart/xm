package com.inzent.igate.core.exception;

//import com.inzent.igate.core.message.MessageGenerator;
import java.util.UUID;

public class IGateException extends Exception {
		
	private static final long serialVersionUID = -5117794700756050128L;

	public final String uuid;
	public final String code;
	public boolean timeout;
	
	public IGateException() {
		this.uuid = UUID.randomUUID().toString();
		this.code = "";
	}
	public IGateException(String code, String s2, String... strings) {
		this(null, code, s2, strings);
	}
	
	public IGateException(Throwable t, String code, String s2, String...strings) {
		//super(MessageGenerator.getMessage(s1, s2, strings), t);
		this.uuid = UUID.randomUUID().toString();
		this.code = code;
	}

}
