package com.inzent.igate.connector.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dayheart.tmsg.TransactionRamp;
import com.dayheart.util.TierConfig;
import com.dayheart.util.XLog;
import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.connector.IGateConnectorException;
import com.inzent.igate.core.exception.IGateException;

import kisb.sb.tmsg.SysHeader;

public class OutBoundConnector {
	
	private TransactionRamp txRamp = TransactionRamp.getInstance(false); // 싱글톤
	
	@Autowired
	private TierConfig tierConfig;
	
	public void callService(AdapterParameter adapterParameter) throws IGateConnectorException {
		
		try {
			String url = null;
			
			String mciOut = tierConfig.getOut("MCI");
			String mciProtocol = tierConfig.getProtocol("MCI");
			String mciEgress = tierConfig.getEgress("MCI");
						
			String[] egresses = null;
			if(mciEgress.indexOf(",")>-1) {
				egresses = mciEgress.split(",");
				
				for(String egress : egresses) {
					String EGRESS = egress.toUpperCase();
					
					String protocol = tierConfig.getProtocol(EGRESS);
					String host = tierConfig.getHost(EGRESS);
					int port = tierConfig.getPort(EGRESS);
					String uri = tierConfig.getUri(EGRESS);
					String[] uris = tierConfig.getUris(egress);
					
					if(protocol.toUpperCase().equals("HTTP")) {
						
					}
					
					
				}
			}
			
			
			
			String corProtocol = txRamp.getCorProtocol();
			String corHost = txRamp.getCorHost();
			int corPort = txRamp.getCorPort();
			
			Object reqData = adapterParameter.getRequestData();
			
			Field f = adapterParameter.getClass().getDeclaredField("data");
			f.setAccessible(true);
			
			Object obj = f.get(adapterParameter);
			Object reqObj = null;
			Object resObj = null;
			Object requestData = null; 
			
			if( obj instanceof java.util.HashMap ) {
				java.util.HashMap map = (java.util.HashMap)obj;
				 reqObj = map.get("request");
				 resObj = map.get("response");
				 requestData = map.get("request.data");
			}
			
			/*
			String reqKlassName = reqObj.getClass().getSuperclass().getName();
			XLog.stdout(String.format("##### REQ_KLASS [%s]", reqKlassName));
			*/
			// org.apache.catalina.connector.RequestFacade
			
			
			if(mciProtocol.equalsIgnoreCase("HTTP")) {
				Method method = reqObj.getClass().getMethod("getContentType");
				
				if(method == null) {
					method = reqObj.getClass().getSuperclass().getMethod("getContentType");
					
					if(method == null) {
						method = reqObj.getClass().getSuperclass().getMethod("getContentType");
						
						if(method == null) {
							method = reqObj.getClass().getSuperclass().getMethod("getContentType");
						}
					}
				}
				
				String contentType = "";
				Object o_contentType = method.invoke(reqObj);
				
				if(o_contentType!=null) {
					contentType = (String)o_contentType;
				}
				
				
				method = reqObj.getClass().getMethod("getContextPath");
				
				if(method == null) {
					method = reqObj.getClass().getSuperclass().getMethod("getContextPath");
					
					if(method == null) {
						method = reqObj.getClass().getSuperclass().getMethod("getContextPath");
						
						if(method == null) {
							method = reqObj.getClass().getSuperclass().getMethod("getContextPath");
						}
					}
				}
				
				if(reqObj!=null) {
					Object rtObj = method.invoke(reqObj);
					
					//url = esbProtocol + "://" + esbHost + ":" + esbPort + (String)method.invoke(reqObj) + "/ESB";
					String coreUri = "/cor/core_tmax_fep.jsp";
					url = corProtocol + "://" + corHost + ":" + corPort + coreUri;
					
					XLog.stdout(String.format("##### URL [%s]", url));
					
					byte[] b_response = null;
					Object o_return = null;
					
					XLog.stdout(String.format("##### MCI_OUT [%s]", mciOut));
					// 2023.10.11 One-way for now
					if(corProtocol.equalsIgnoreCase("HTTP")) {
						//txRamp.transmit(url, "POST", (byte[])requestData);
						if(mciOut.equalsIgnoreCase("octet-stream")) {
							Class[] argTypes = {String.class, String.class, byte[].class};
							Object[] args = { url, "POST", requestData };
							method = txRamp.getClass().getMethod("transmit", argTypes);
							
							XLog.stdout(String.format("##### METHOD [%s]", method));
							
							Object rcvObj = method.invoke(txRamp, args);
							b_response = (byte[])rcvObj;
							XLog.stdout(String.format("##### ESB_RETURN [%s]", new String(b_response)));
							
							XLog.stdout(String.format("[%s]", SysHeader.getPrintString(b_response)));
							///// ENUM 처리해야...2023.10.19 AM 5
							method = null;
							// check SysHeader.class.isEnum()
							Field enumConst = SysHeader.class.getField("TMSG_SYNCZ_SECD");
							
							//XLog.stdout(String.format("##### ENUM: %s", enumConst.toString())); // public static final kisb.sb.tmsg.SysHeader kisb.sb.tmsg.SysHeader.TMSG_SYNCZ_SECD
							//XLog.stdout(String.format("##### EUNM FIELD [%s]", enumConst));
							//##### EUNM FIELD [public static final kisb.sb.tmsg.SysHeader kisb.sb.tmsg.SysHeader.TMSG_SYNCZ_SECD]
							// check enumConst.isEnumConstant()
							
							Object enumObj = enumConst.get(null);
							XLog.stdout(String.format("##### ENUM_FIELD [%s]", enumObj));
							
							method = SysHeader.class.getMethod("getField", new Class[] { byte[].class });
							XLog.stdout(String.format("##### ENUM_METHOD [%s]", method));
							
							Object o_tmsg = method.invoke(enumObj, b_response);
							XLog.stdout(String.format("##### ENUM_VAL: ", o_tmsg));
							
							
							
							/*
							Class[] byteTypes = { byte[].class };
							method = SysHeader.class.getMethod("getField", byteTypes);
							Object o_synczSecd = method.invoke(null, requestData);
							XLog.stdout(String.format("##### METHOD: ", method));
							
							Class[] trmsTypes = { byte[].class, String.class, String.class, String.class};
							Object[] trmsArgs = {b_response, "MCI", "R", o_synczSecd};
							method = SysHeader.class.getMethod("setTRMST", trmsTypes);
							XLog.stdout(String.format("##### METHOD: ", method));
							method.invoke(null, trmsArgs);
							
							method = null;
							Class[] rspnsTypes = { byte[].class, String.class, String.class, String.class};
							Object[] rspnsArgs = {b_response, "0", "MCI", "", "" };
							method = SysHeader.class.getMethod("setTRMST", rspnsTypes);
							XLog.stdout(String.format("##### METHOD: ", method));
							method.invoke(null, rspnsArgs);
							*/
							
							method = null;
							//Map<String, Object> map = SysHeader.toMap(b_response);
							Class[] headerTypes = { byte[].class };
							method = SysHeader.class.getMethod("toMap", headerTypes);
							o_return = method.invoke(null, rcvObj);
							
							
						} else if(mciOut.equalsIgnoreCase("application/json")) {
							
						} else if(mciOut.equalsIgnoreCase("application/xml")) {
							
						} else if(mciOut.equalsIgnoreCase("text/html")) {
							
						}
						
						XLog.stdout(String.format("##### MAKE RESPONSE"));
						
						if(contentType.equalsIgnoreCase("octet-stream")) {
							
						} else if(mciOut.equalsIgnoreCase("application/json")) {
							method = null;
							Class[] pTypes = { java.util.Map.class };
							method = SysHeader.class.getMethod("toJsonString", pTypes);
							XLog.stdout(String.format("##### METHOD: ", method));
							o_return = method.invoke(null, o_return);
							
							String json = (String)o_return;
							byte[] b_json = json.getBytes("UTF-8");
							
							method = null;
							Class[] encTypes = { String.class };
							method = resObj.getClass().getMethod("setCharacterEncoding", encTypes);
							XLog.stdout(String.format("##### METHOD: ", method));
							method.invoke(resObj, "UTF-8");
							
							
							method = null;
							Object wr = resObj.getClass().getMethod("getWriter");
							method = wr.getClass().getMethod("print", encTypes);
							XLog.stdout(String.format("##### METHOD: ", method));
							method.invoke(wr, new String(b_json));
							
							method = null;
							method = wr.getClass().getMethod("flush");
							XLog.stdout(String.format("##### METHOD: ", method));
							method.invoke(wr);
							
						} else if(mciOut.equalsIgnoreCase("application/xml")) {
							
						} else if(mciOut.equalsIgnoreCase("text/html")) {
							
						}
						
						
						
					} else if(corProtocol.equalsIgnoreCase("TCP")) {
						
					}
				} 
			} else if(mciProtocol.equalsIgnoreCase("TCP")) {
				
			}
			
			
			
			
			
			Class[] clses = reqObj.getClass().getInterfaces();
			
			for(Class cls : clses) {
				
				XLog.stdout(String.format("##### CLS [%s]", cls.getName()));
				//javax.servlet.http.HttpServletRequest
				
			}
			
			
			
			
			
		}catch (Exception e) {
			String[] arr = {"apple", "kiwi", "grape", "banana"};
			IGateConnectorException ex = new IGateConnectorException(null, e, "MCI_ERR", "ADAPTER_PARAMETER_NULL", arr);
			throw ex;
		}
	}

}
