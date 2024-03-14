package com.dayheart.hello.web;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.dayheart.tcp.TCPClient;
import com.dayheart.tmsg.TransactionRamp;
import com.dayheart.util.TierConfig;
import com.dayheart.util.XLog;
import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.connector.IGateConnectorException;
import com.inzent.igate.connector.socket.SocketConnector;
import com.inzent.igate.core.exception.IGateException;

import kisb.sb.tmsg.SysHeader;
import kisb.sb.tmsg.TelegramMessageUtil;

@Controller
public class MCIController {
	
	private TransactionRamp txRamp = TransactionRamp.getInstance(false); // 싱글톤
	
	@Autowired
	private TierConfig tierConfig;
	
	public MCIController() {
		
	}
	
	@RequestMapping({"**/esb/**"})
	public void esbRequestHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		
		String contentType = request.getContentType();
		
		String tier = "ESB";
		String url = null;
		String out = tierConfig.getOut(tier);
		
		String[] egresses = null;
		String EGRESS = tierConfig.getEgress(tier);
		String protocol;
		String host;
		int port = 0;
		
		if(contentType!=null) {
			
			byte[] rcv_sysHeader = null;
			byte[] b_body = TCPClient.retrieveBodyToBytes(request.getInputStream());
			
			Map<String, Object> map = new HashMap<>();
			
			if( contentType.equalsIgnoreCase("application/json") ) {
				rcv_sysHeader = SysHeader.toBytes(SysHeader.flatJson(new String(b_body)));
				SysHeader.toBytesPretty(new String(b_body));
				
				
			} else if(contentType.equalsIgnoreCase("application/octet-stream")) {
				rcv_sysHeader = new byte[b_body.length];
				System.arraycopy(b_body, 0, rcv_sysHeader, 0, b_body.length);
				
				map = SysHeader.toMap(rcv_sysHeader);
				map.put(SysHeader.TRSMT_LINK_SYS_SECD.name(), "ESB");
				/*
				map.put(SysHeader.TRSMT_NODE_NO.name(), "ESB");
				map.put(SysHeader.DMND_RSPNS_SECD.name(), "ESB");
				map.put(SysHeader.TMSG_SYNCZ_SECD.name(), "ESB");
				map.put(SysHeader.TMSG_TRSMT_DT.name(), "ESB");
				*/
				
				
				if(EGRESS.indexOf(",")>-1) {
					egresses = EGRESS.split(",");
					
					for(String egress : egresses) {
						
						protocol = tierConfig.getProtocol(egress.toUpperCase());
						host = tierConfig.getHost(egress.toUpperCase());
						port = tierConfig.getPort(egress.toUpperCase());
						
						if(protocol.toUpperCase().equals("HTTP")) {

							//String uri = tierConfig.getUri(EGRESS);
							String[] uris = tierConfig.getUris(egress);
							
							for(String uri:uris) {
								url = String.format("%s://%s:%d%s", protocol, host, port, uri);
								
								txRamp.transmit(url, "POST", SysHeader.toBytes(map));
							}	
						}
					}
				} else {
					//EGRESS = mciEgress.toUpperCase();
					protocol = tierConfig.getProtocol(EGRESS);
					host = tierConfig.getHost(EGRESS);
					port = tierConfig.getPort(EGRESS);
					
					if(protocol.toUpperCase().equals("HTTP")) {

						String uri = tierConfig.getUri(EGRESS);
						url = String.format("%s://%s:%d%s", protocol, host, port, uri);
							
						txRamp.transmit(url, "POST", SysHeader.toBytes(map));	
					}
				}
				
				
			} else if(contentType.equalsIgnoreCase("application/xml")) {
				rcv_sysHeader = b_body;
			} else if(contentType.equalsIgnoreCase("text/html")) {
				
			} else {
				; // and so on
			}
			
			
			
		} // end of contentType
		
	}
	
	//@RequestMapping({"/**"})
	@RequestMapping({"**/mci/**"})
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		System.out.println(path); // like /mci/cruzlink

		
		String contentType = request.getContentType();
		
		if(contentType!=null) {
			
			byte[] rcv_sysHeader = null;
			
			byte[] b_body = TCPClient.retrieveBodyToBytes(request.getInputStream());
			XLog.stdout(String.format("b_body[%s]", new String(b_body)));
			
			if( contentType.equalsIgnoreCase("application/json") ) {
				rcv_sysHeader = SysHeader.toBytes(SysHeader.flatJson(new String(b_body)));
				
				SysHeader.toBytesPretty(new String(b_body));
				
			} else if(contentType.equalsIgnoreCase("application/octet-stream")) {
				rcv_sysHeader = new byte[b_body.length];
				System.arraycopy(b_body, 0, rcv_sysHeader, 0, b_body.length);
				
				XLog.stdout("OCTET-STREAM [" + new String(rcv_sysHeader) + "], len:" + rcv_sysHeader.length);
			} else if(contentType.equalsIgnoreCase("application/xml")) {
				rcv_sysHeader = b_body;
			} else if(contentType.equalsIgnoreCase("text/html")) {
				
			} else {
				; // and so on
			}
			
			
			byte[] b_request = null; // 요청
			byte[] b_response = null; // 회신
			byte[] b_rcv; // 수신
			
			String url = null;
			TelegramMessageUtil tmsgUtil = TelegramMessageUtil.getInstance(false); // 싱글톤
			
			// for FEP FEP header 120byte + sys_header, ex) 120byte(anylink header)
			byte[] req_sysHeader = null;
			req_sysHeader = new byte[rcv_sysHeader.length];
			//System.arraycopy(rcv_sysHeader, 0, req_sysHeader, 0, SysHeader.getLength());
			System.arraycopy(rcv_sysHeader, 0, req_sysHeader, 0, rcv_sysHeader.length);
			
			try {
				String mciOut = tierConfig.getMciOut();
				
				SocketConnector connector = null;
				AdapterParameter adapterParameter = new AdapterParameter();
			
				
				Field f = adapterParameter.getClass().getDeclaredField("data");
				f.setAccessible(true);
				
				Object obj = f.get(adapterParameter);
				if( obj instanceof java.util.HashMap ) {
					java.util.HashMap map = (java.util.HashMap)obj;
					map.put("request", request);
					map.put("response", response);
				}
				
				// 전송 전문 만들기.
				if(mciOut.equalsIgnoreCase("octet-stream")) {
					b_request = tmsgUtil.setSendMessageInfo(req_sysHeader, "MCI", "S", SysHeader.TMSG_SYNCZ_SECD.getField(req_sysHeader).trim(), "_____MCI______TRANSACTION_REQUEST_MESSAGE_BODY_____ZZ".getBytes());
					String ALL_TMSG_LNTH = String.format("%08d", b_request.length);
					SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), b_request);
					
					Method m = adapterParameter.getClass().getMethod("setRequestData", new Class[] { byte[].class });
					m.invoke(adapterParameter, new Object[] { b_request });
				} else if(mciOut.equalsIgnoreCase("octet-stream")) {
					b_request = tmsgUtil.setSendMessageInfo(req_sysHeader, "MCI", "S", SysHeader.TMSG_SYNCZ_SECD.getField(req_sysHeader).trim(), "_____MCI______TRANSACTION_REQUEST_MESSAGE_BODY_____ZZ".getBytes());
					String ALL_TMSG_LNTH = String.format("%08d", b_request.length);
					SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), b_request);
					
					Method m = adapterParameter.getClass().getMethod("setRequestData", new Class[] { byte[].class });
					m.invoke(adapterParameter, new Object[] { b_request });
				}
				
				connector = new SocketConnector();
				connector.callService(adapterParameter);
			
			
			
				// HTTP 일 경우에...
				//b_rcv = tmsgUtil.transmit(url, "POST", b_request); // if !error
				
				
				b_response = new byte[b_request.length];
				System.arraycopy(b_request, 0, b_response, 0, b_request.length);
				
				// 송신 헤더 값 결정
				SysHeader.setTRMST(b_response, "MCI", "R", SysHeader.TMSG_SYNCZ_SECD.getField(b_request));
				// 에러 유무 회신
				SysHeader.setRSPNS(b_response, "0", "MCI", "", "");
	
				
				XLog.stdout("REQ[" + new String(b_request) + "], len:" + b_request.length);
				XLog.stdout("RES[" + new String(b_response) + "], len:" + b_response.length);
				
								
				/*
				Map<String, Object> rs_map = (Map<String, Object>)results.get(0); // 해당하는 단어가 없을 수도 있다.
				
										
				String jsonString = SysHeader.toJsonString(rs_map);
				
				XLog.stdout("MCI_RESPONSE[" + jsonString + "]");
				
				response.getOutputStream().write(jsonString.getBytes());
				response.getOutputStream().flush();
				*/
			
			} catch (Exception e) {
				String[] arr = {"apple", "kiwi", "grape", "banana"};
				e.printStackTrace();
				IGateException ex = new IGateConnectorException(null, e, "MCIERR", e.toString(), arr);
			}
		} // end of contentType
		
	}

}
