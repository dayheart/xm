package com.dayheart.tmsg;

import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.dayheart.tcp.TCPClient;
import com.dayheart.util.XLog;


public class TransactionRamp {
	
	private String mciProtocol;
	private String mciHost;
	private int mciPort;
	private String mciOut;
	
	private String esbProtocol;
	private String esbHost;
	private int esbPort;
	private String esbOut;
	
	private String eaiProtocol;
	private String eaiHost;
	private int eaiPort;
	private String eaiOut;
	
	private String corProtocol;
	private String corHost;
	private int corPort;
	private String corOut;
	
	private String fepProtocol;
	private String fepHost;
	private int fepPort;
	private String fepOut;
	
	private String apiProtocol;
	private String apiHost;
	private int apiPort;
	private String apiOut;
	
	private static TransactionRamp instance;
	
	/**
	 *  synchronize 방식의 싱글톤
	 * @return
	 */
	public static synchronized TransactionRamp getInstance() {
		if(instance == null) {
			instance = new TransactionRamp();
		}
		return instance;
	}
	
	/**
	 * 싱글톤 생성자
	 */
	private TransactionRamp() {
		loadProperties(0);
	}
	
	/**
	 * 홀더 방식을 이용한 싱글톤 생성
	 * @author dayheart
	 *
	 */
	private static class SingletonHolder {
		
		private static final TransactionRamp instance = new TransactionRamp();
	}
	
	/**
	 * 싱글톤 방식의 객체 생성
	 * @param fast 홀더 방식을 이용한 싱글톤 객체 반환
	 * @return 싱글토 객체 반환
	 */
	public static TransactionRamp getInstance(boolean fast) {
		
		TransactionRamp rt = null;
		
		String dbg = null;
		
		if(fast) {
			rt = SingletonHolder.instance;
			dbg = "#### HOLDER SINGLETON";
		} else {
			rt = getInstance();
			dbg = "#### SYNCHRONIZED SINGLETON";
		}
		
		XLog.stdout( dbg + " [" + rt + "]");
		
		return rt;
	}
	
	/**
	 * 프러퍼티 로딩
	 * @param i 0:현재 클래스의 클래스로더를 이용한 프러퍼티 파일 읽기
	 */
	private void loadProperties(int i) {
		
		switch(i) {
		
			case 0: 
				loadPropertiesByClassLoader();
				break;
			
			default:
				break;
		}
	}
	
	
	/**
	 * E2E 각 티어에 대한 전송 프로토콜 및 IP, port 설정값 읽기
	 */
	private void loadPropertiesByClassLoader() {
		String configFilePath = "config" + File.separator + "tier.properties";
		
		//XLog.stdout(configFilePath);
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		
		Properties prop = new Properties();
		try {
			prop.load(is);
			
			XLog.stdout(prop.toString());
			
			mciProtocol = prop.getProperty("MCI.PROTOCOL");
			mciHost = prop.getProperty("MCI.HOST");
			mciPort =  Integer.parseInt( prop.getProperty("MCI.PORT") );
			mciOut = prop.getProperty("MCI.OUT");
			
			esbProtocol = prop.getProperty("ESB.PROTOCOL");
			esbHost = prop.getProperty("ESB.HOST");
			esbPort =  Integer.parseInt( prop.getProperty("ESB.PORT") );
			esbOut = prop.getProperty("ESB.OUT");
			
			eaiProtocol = prop.getProperty("EAI.PROTOCOL");
			eaiHost = prop.getProperty("EAI.HOST");
			eaiPort =  Integer.parseInt( prop.getProperty("EAI.PORT") );
			eaiOut = prop.getProperty("EAI.OUT");
			
			corProtocol = prop.getProperty("COR.PROTOCOL");
			corHost = prop.getProperty("COR.HOST");
			corPort =  Integer.parseInt( prop.getProperty("COR.PORT") );
			corOut = prop.getProperty("COR.OUT");
			
			fepProtocol = prop.getProperty("FEP.PROTOCOL");
			fepHost = prop.getProperty("FEP.HOST");
			fepPort =  Integer.parseInt( prop.getProperty("FEP.PORT") );
			fepOut = prop.getProperty("FEP.OUT");
			
			apiProtocol = prop.getProperty("API.PROTOCOL");
			apiHost = prop.getProperty("API.HOST");
			apiPort =  Integer.parseInt( prop.getProperty("API.PORT") );
			apiOut = prop.getProperty("API.OUT");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * E2E 전문 HTTP 프로토콜 요청
	 * @param url 요청 URL
	 * @param method HTTP 메소드
	 * @param req_tmsg HTTP 요청 바디
	 * @return HTTP 응답
	 */
	public byte[] transmit(String url, String method, byte[] message) {
		
		byte[] response;
		
		response = TCPClient.executeBytesByApacheHttpClient(url, method, message);
		// check null
		
		return response; 
	}
	
	
	public byte[] transmit(String contentType, String url, String method, byte[] message) {
		byte[] response = null;
		
		try {
			switch(contentType) {
			case "application/octet-stream" :
				response = TCPClient.executeBytesByApacheHttpClient(url, method, message); 
				break;
			case "application/xml" :
				String xml = TCPClient.executeXmlByApacheHttpClient(url, method, new String(message, "UTF-8"));
				response = (xml==null)?new byte[0]:xml.getBytes("UTF-8");
				break;
			default :
				String json = TCPClient.executeJsonByApacheHttpClient(url, method, new String(message, "UTF-8"));
				response = (json==null)?new byte[0]:json.getBytes("UTF-8");
				break;			
			}
		}catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		
		
		return response;
	}
	
	public String transmit(String contentType, String url, String method, String message) {
		
		String response = null;
		
		try {
			switch(contentType) {
			case "application/octet-stream" :
				byte[] octet = TCPClient.executeBytesByApacheHttpClient(url, method, message.getBytes("UTF-8"));
				response = new String(octet);
				break;
			case "application/xml" :
				response = TCPClient.executeXmlByApacheHttpClient(url, method, message);
				break;
			default :
				response = TCPClient.executeJsonByApacheHttpClient(url, method, message);
				break;			
			}
		}catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		
		return response; 
	}
	
	
	public String getMciProtocol() {
		return mciProtocol;
	}

	public String getMciHost() {
		return mciHost;
	}

	public int getMciPort() {
		return mciPort;
	}

	public String getEsbProtocol() {
		return esbProtocol;
	}

	public String getEsbHost() {
		return esbHost;
	}

	public int getEsbPort() {
		return esbPort;
	}

	public String getEaiProtocol() {
		return eaiProtocol;
	}

	public String getEaiHost() {
		return eaiHost;
	}

	public int getEaiPort() {
		return eaiPort;
	}

	public String getCorProtocol() {
		return corProtocol;
	}

	public String getCorHost() {
		return corHost;
	}

	public int getCorPort() {
		return corPort;
	}

	public String getFepProtocol() {
		return fepProtocol;
	}

	public String getFepHost() {
		return fepHost;
	}

	public int getFepPort() {
		return fepPort;
	}

	public String getApiProtocol() {
		return apiProtocol;
	}

	public String getApiHost() {
		return apiHost;
	}

	public int getApiPort() {
		return apiPort;
	}

	public String getMciOut() {
		return mciOut;
	}

	public String getEsbOut() {
		return esbOut;
	}

	public String getEaiOut() {
		return eaiOut;
	}

	public String getCorOut() {
		return corOut;
	}

	public String getFepOut() {
		return fepOut;
	}

	public String getApiOut() {
		return apiOut;
	}

}
