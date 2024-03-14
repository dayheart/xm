package com.inzent.igate.connector.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.dayheart.tmsg.TransactionRamp;
import com.dayheart.util.TierConfig;
import com.dayheart.util.XLog;
import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.connector.IGateConnectorException;
import com.inzent.igate.core.exception.IGateException;

import kisb.sb.tmsg.SysHeader;

public class OutBoundConnector {
	
	private TransactionRamp txRamp = TransactionRamp.getInstance(false); // 싱글톤
	
	private TierConfig tierConfig = TierConfig.getInstance();
	
	public void callService(AdapterParameter adapterParameter) throws IGateConnectorException {
		
		try {
			String url = null;
			
			Object reqData = adapterParameter.getRequestData();
			System.out.println("REQ_DATA:" + reqData.getClass().getName());
			
			
			Field f = adapterParameter.getClass().getDeclaredField("data");
			f.setAccessible(true);
			
			Object obj = f.get(adapterParameter);
			//System.out.println("OBJ:" + obj);
			// OBJ:{request=org.apache.catalina.connector.RequestFacade@55c4cfeb, response=org.apache.catalina.connector.ResponseFacade@598da362, request.data=[B@41dc7be2}
			Object reqObj = null;
			Object resObj = null;
			Object requestData = null; 
			
			if( obj instanceof java.util.HashMap ) {
				java.util.HashMap map = (java.util.HashMap)obj;
				 reqObj = map.get("request");
				 resObj = map.get("response");
				 requestData = map.get("request.data");
			}
			
			System.out.println("tierConfig" + tierConfig);
			
			String mciOut = tierConfig.getOut("MCI");
			System.out.println("OUT:" + mciOut);
			
			String mciProtocol = tierConfig.getProtocol("MCI");
			String mciEgress = tierConfig.getEgress("MCI");
			String contentType = "application/" + mciOut;
			
			//System.out.println("CNT:" + contentType);
						
			String[] egresses = null;
			String EGRESS;
			String protocol;
			String host;
			int port = 0;
			if(mciEgress.indexOf(",")>-1) {
				egresses = mciEgress.split(",");
				
				for(String egress : egresses) {
					EGRESS = egress.toUpperCase();
					
					protocol = tierConfig.getProtocol(EGRESS);
					host = tierConfig.getHost(EGRESS);
					port = tierConfig.getPort(EGRESS);
					
					if(protocol.toUpperCase().equals("HTTP")) {

						//String uri = tierConfig.getUri(EGRESS);
						String[] uris = tierConfig.getUris(egress);
						
						for(String uri:uris) {
							url = String.format("%s://%s:%d%s", protocol, host, port, uri);
							
							if( requestData instanceof byte[]) {
								txRamp.transmit(contentType, url, "POST", (byte[])requestData);
							} else if (requestData instanceof String) {
								txRamp.transmit(contentType, url, "POST", (String)requestData);
							}
						}	
					}
				}
			} else {
				EGRESS = mciEgress.toUpperCase();
				protocol = tierConfig.getProtocol(EGRESS);
				host = tierConfig.getHost(EGRESS);
				port = tierConfig.getPort(EGRESS);
				
				if(protocol.toUpperCase().equals("HTTP")) {

					String uri = tierConfig.getUri(EGRESS);
					url = String.format("%s://%s:%d%s", protocol, host, port, uri);
						
					if( requestData instanceof byte[]) {
						txRamp.transmit(contentType, url, "POST", (byte[])requestData);
					} else if (requestData instanceof String) {
						txRamp.transmit(contentType, url, "POST", (String)requestData);
					}	
				}
			}
			
			
			
			String corProtocol = txRamp.getCorProtocol();
			String corHost = txRamp.getCorHost();
			int corPort = txRamp.getCorPort();
			
			
			
			/*
			String reqKlassName = reqObj.getClass().getSuperclass().getName();
			XLog.stdout(String.format("##### REQ_KLASS [%s]", reqKlassName));
			*/
			// org.apache.catalina.connector.RequestFacade
			
			
			
		}catch (Exception e) {
			String[] arr = {"apple", "kiwi", "grape", "banana"};
			IGateConnectorException ex = new IGateConnectorException(null, e, "MCI_ERR", "ADAPTER_PARAMETER_NULL", arr);
			throw ex;
		}
	}

}
