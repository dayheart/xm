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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.dayheart.tcp.TCPClient;
import com.dayheart.tmsg.TransactionRamp;
import com.dayheart.util.TierConfig;
import com.dayheart.util.Utils;
import com.dayheart.util.XLog;
import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.connector.IGateConnectorException;
import com.inzent.igate.connector.socket.SocketConnector;
import com.inzent.igate.core.exception.IGateException;

import kisb.sb.tmsg.SysHeader;
import kisb.sb.tmsg.TelegramMessageUtil;

@Controller
public class CORController {
	
	private TransactionRamp txRamp = TransactionRamp.getInstance(false); // 싱글톤
	
	@Autowired
	private TierConfig tierConfig;
	
	public CORController() {
		
	}
	
	@RequestMapping({"**/cor/json"})
	public void handleJsonRequest(@RequestBody Map<String, Object> sysHeader) {
		XLog.stdout(String.format("MAP [%s]", sysHeader));
		
		executeRequest(sysHeader);
		XLog.stdout(String.format("MAP [%s]", sysHeader));
	}
	
	@RequestMapping({"**/cor/octet-stream"})
	public void handleBytesRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] bytesHeader = TCPClient.retrieveBodyToBytes(request.getInputStream());
		XLog.stdout(String.format("BYTES [%s]", new String(bytesHeader)));
		
		Map<String, Object> sysHeader = SysHeader.toMap(bytesHeader);
		
		executeRequest(sysHeader);
	}
	
	private void executeRequest(Map<String, Object> sysHeader) {
		String sysCd = "COR"; //sysCd(3) PRD, OFC, SAL, ORD, CST
		// SYNC | ASYNC 세팅, 이후 전문에서 지속... 최종 FEP 에서 사용
		String sync = "S";
		SysHeader.setTRMST(sysHeader, sysCd, "S", sync); // Send/Recv, Sync/Async
		
		String egress = tierConfig.getEgress("COR");
		String out = tierConfig.getOut("COR");
		if(egress!=null) {
			String[] outlets = egress.split(",");
			String url;
			String protocol;
			String host;
			int port;
			String uri;
			int i = 0;
			for(String outlet:outlets) {
				//XLog.stdout(String.format("EGRESS[%d]:%s", i++, outlet));
				switch(outlet) {
				case "ESB" :
					SysHeader.setINFC(sysHeader, "OFFICES", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "COR" :
					SysHeader.setINFC(sysHeader, "ORDERS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				default :
					SysHeader.setINFC(sysHeader, "SALESREPS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
				}
				
				protocol = tierConfig.getProtocol(outlet.toUpperCase());
				host = tierConfig.getHost(outlet.toUpperCase());
				port = tierConfig.getPort(outlet.toUpperCase());
				uri = tierConfig.getUri(outlet.toUpperCase());
				String[] uris = uri.split(",");
				
				int idx = Utils.getRandomNumber(0, (uris.length));
				uri = uris[idx];
				//XLog.stdout("CRUZ:" + uri);
				url = String.format("%s://%s:%d%s", protocol,host,port,uri);
				
				String responseStr = null;
				if(uri.endsWith("json")) {
					//responseStr = TCPClient.executeJsonByApacheHttpClient(url, "POST", SysHeader.toJsonString(sysHeader));
				} else {
					//responseStr = new String( TCPClient.executeBytesByApacheHttpClient(url, "POST", SysHeader.toBytes(sysHeader)));
				}
				
				XLog.stdout("COR_OUT_URL: " + url);
				
			}
		}
	}

}
