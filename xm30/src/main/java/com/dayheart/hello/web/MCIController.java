package com.dayheart.hello.web;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.dayheart.hello.kafka.KafkaProducer;
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
public class MCIController {
	
	private TransactionRamp txRamp = TransactionRamp.getInstance(false); // 싱글톤
	
	@Autowired
	private TierConfig tierConfig;
	
	// 2025.04.29 현대차증권 테스트
	@Autowired
	private KafkaProducer producer;
	
	public MCIController() {
		
	}
	
	@RequestMapping({"/mci/json"})
	public void handleJsonRequest(@RequestBody Map<String, Object> sysHeader) {
		XLog.stdout(String.format("MAP [%s]", sysHeader));
		
		executeRequest(sysHeader);
		//XLog.stdout(String.format("MAP [%s]", sysHeader));
	}
	
	@RequestMapping({"/mci/octet-stream"})
	public void handleBytesRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] bytesHeader = TCPClient.retrieveBodyToBytes(request.getInputStream());
		XLog.stdout(String.format("BYTES [%s]", new String(bytesHeader)));
		
		Map<String, Object> sysHeader = SysHeader.toMap(bytesHeader);
		
		executeRequest(sysHeader);
	}
	
	private void executeRequest(byte[] bytesheader) {
		String sysCd = "MCI"; //sysCd(3) PRD, OFC, SAL, ORD, CST
		// SYNC | ASYNC 세팅, 이후 전문에서 지속... 최종 FEP 에서 사용
		String sync = "S";
		SysHeader.setTRMST(bytesheader, sysCd, "S", sync); // Send/Recv, Sync/Async
		
		String egress = tierConfig.getEgress("MCI");
		String out = tierConfig.getOut("MCI");
		if(egress!=null && egress.length()>0) {
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
				/* 2025.03.10
				case "ESB" :
					SysHeader.setINFC(sysHeader, "OFFICES", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "COR" :
					SysHeader.setINFC(sysHeader, "ORDERS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "FEP" :
					SysHeader.setINFC(sysHeader, "CUSTOMERS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				default :
					SysHeader.setINFC(sysHeader, "SALESREPS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
				*/

				case "ESB" :
					SysHeader.setINFC(bytesheader, "OFFICES", "N", SysHeader.TMSG_APP_ID.getField(bytesheader), SysHeader.TMSG_SVC_ID.getField(bytesheader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "COR" :
					SysHeader.setINFC(bytesheader, "ORDERS", "N", SysHeader.TMSG_APP_ID.getField(bytesheader), SysHeader.TMSG_SVC_ID.getField(bytesheader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "FEP" :
					SysHeader.setINFC(bytesheader, "CUSTOMERS", "N", SysHeader.TMSG_APP_ID.getField(bytesheader), SysHeader.TMSG_SVC_ID.getField(bytesheader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "IGT" :
					SysHeader.setINFC(bytesheader, "PRODUCTS", "N", SysHeader.TMSG_APP_ID.getField(bytesheader), SysHeader.TMSG_SVC_ID.getField(bytesheader)); // INFC_ID(part), SVC_ID(eng)
					break;
				default :
					SysHeader.setINFC(bytesheader, "SALESREPS", "N", SysHeader.TMSG_APP_ID.getField(bytesheader), SysHeader.TMSG_SVC_ID.getField(bytesheader)); // INFC_ID(part), SVC_ID(eng)
				}
				
				protocol = tierConfig.getProtocol(outlet.toUpperCase());
				host = tierConfig.getHost(outlet.toUpperCase());
				port = tierConfig.getPort(outlet.toUpperCase());
				uri = tierConfig.getUri(outlet.toUpperCase());
				String[] uris = uri.split(",");
				
				int idx = Utils.getRandomNumber(0, (uris.length));
				uri = uris[idx];
				url = String.format("%s://%s:%d%s", protocol,host,port,uri);
				
				String responseStr = null;
				/*
				if(uri.endsWith("json")) {
					responseStr = TCPClient.executeJsonByApacheHttpClient(url, "POST", SysHeader.toJsonString(sysHeader));
				} else {
					responseStr = new String( TCPClient.executeBytesByApacheHttpClient(url, "POST", SysHeader.toBytes(sysHeader)));
				}
				*/
				
				Socket socket = null;
				
				byte [] header = new byte[4];
				int h_len = 2048;
				
				header[3] = (byte)(h_len >> 24);
				header[2] = (byte)(h_len >> 16);
				header[1] = (byte)(h_len >> 8);
				header[0] = (byte)(h_len);
				
				
				final String SERVER_IP = "10.10.52.7";
				//final String SERVER_IP = "172.169.10.85";
				final int SERVER_PORT = 15050;
				
				
				try {
					System.out.print("H_LEN:" + new String(header, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String msg = null;
				
				try {
					socket = new Socket();
					socket.setReuseAddress(true);
					//socket.setSoTimeout(60000);
					//socket.setSoLinger(true, 3000);
					socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
					System.out.println("CONNECT_SUCESS\n");
					
					OutputStream os = socket.getOutputStream();
					
					System.out.println("HEADER_WRITE");
					os.write(header, 0, header.length);
					
					
					
					byte[] body = new byte[2048];
					byte sp = (byte)32;
					Arrays.fill(body, sp);
					System.out.printf("BODY_INT [%s]\n", new String(body));
					byte[] m_body = bytesheader;
					
					System.arraycopy(m_body, 0, body, 0, m_body.length);
					System.out.printf("BODY_INT2 [%s]\n", new String(body));
					System.out.printf("BODY_LEN [%d]\n", m_body.length);
					System.out.printf("BODY_CTNT [%d]\n", body.length);
					System.out.println("BODY_WRITE");
					
					String NIL = "\0";
					int NIL_SIZE = NIL.getBytes().length;
					XLog.stdout(String.format("NIL_BYTES [%d]", NIL.getBytes().length));
					System.arraycopy(NIL.getBytes(), 0, body, body.length-2, NIL_SIZE);
					
					
					byte[] w_body = new byte[2048];
					Arrays.fill(body, sp);
					w_body[2047] = 0;
					//os.write(body, 0, body.length);
					//os.write(w_body, 0, w_body.length);
					//os.flush();
					
					ByteBuffer sndBuf = null;
					sndBuf = ByteBuffer.allocate(2048);
					sndBuf.order(ByteOrder.LITTLE_ENDIAN);
					byte [] t_body = bytesheader;
					XLog.stdout(String.format("T_BODY_LEN [%d]", t_body.length));
					XLog.stdout(String.format("T_BODY [%s]", new String(t_body)));
					sndBuf.put(t_body);
					sndBuf.put(new byte[2048-body.length]);
					t_body = sndBuf.array();
					XLog.stdout(String.format("SEND_BYTES [%s]", new String(t_body)));
					
					os.write(t_body);
					os.flush();
					
					System.out.println("READ..");
					InputStream is = socket.getInputStream();
					
					int rcvLen = -1;
					byte[] rcvBuf = new byte[8192];
					String rcvMsg = new String();
					
					do {
						rcvLen = is.read(rcvBuf);
						
						rcvMsg += new String(rcvBuf);
					} while(rcvLen > -1);
					
					System.out.printf("###RVC_MSG[%s]", rcvMsg);
					/*
					int rcvLength = is.read();
					System.out.printf("RCV_LEN [%d]\n", rcvLength );
					
					
					if(rcvLength>0) {
						byte rcvBytes[] = new byte[rcvLength];
						is.read(rcvBytes, 0, rcvLength);
						
						msg = new String(rcvBytes);
						System.out.println(String.format("RCV[%s]", msg));
					}
					*/
					
					
					
					
					
					os.close();
					is.close();
					
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				XLog.stdout("MCI_OUT_URL: " + url);
			}
		}
	}
	
	private void executeRequest(Map<String, Object> sysHeader) {
		String sysCd = "MCI"; //sysCd(3) PRD, OFC, SAL, ORD, CST
		// SYNC | ASYNC 세팅, 이후 전문에서 지속... 최종 FEP 에서 사용
		String sync = "S";
		SysHeader.setTRMST(sysHeader, sysCd, "S", sync); // Send/Recv, Sync/Async
		
		String egress = tierConfig.getEgress("MCI");
		String out = tierConfig.getOut("MCI");
		if(egress!=null && egress.length()>0) {
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
				/* 2025.03.10
				case "ESB" :
					SysHeader.setINFC(sysHeader, "OFFICES", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "COR" :
					SysHeader.setINFC(sysHeader, "ORDERS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "FEP" :
					SysHeader.setINFC(sysHeader, "CUSTOMERS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				default :
					SysHeader.setINFC(sysHeader, "SALESREPS", "N", "", SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
				*/

				case "ESB" :
					SysHeader.setINFC(sysHeader, "OFFICES", "N", SysHeader.TMSG_APP_ID.getField(sysHeader), SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "COR" :
					SysHeader.setINFC(sysHeader, "ORDERS", "N", SysHeader.TMSG_APP_ID.getField(sysHeader), SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "FEP" :
					SysHeader.setINFC(sysHeader, "CUSTOMERS", "N", SysHeader.TMSG_APP_ID.getField(sysHeader), SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					break;
				case "IGT" :
					SysHeader.setINFC(sysHeader, "PRODUCTS", "N", SysHeader.TMSG_APP_ID.getField(sysHeader), SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
					AdapterParameter adapterParameter = new AdapterParameter();
					adapterParameter.setRequestData(SysHeader.toBytes(sysHeader));
					
					host = tierConfig.getHost("IGT");
					port = tierConfig.getPort("IGT");
					
					XLog.stdout(String.format("IGT_IP [%s]", host));
					XLog.stdout(String.format("IGT_port [%d]", port));
					
					adapterParameter.put("serverIP", host);
					adapterParameter.put("port", port);
					
					SocketConnector connector = new SocketConnector();
					try {
						connector.callService(adapterParameter);
					} catch (IGateConnectorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default :
					SysHeader.setINFC(sysHeader, "SALESREPS", "N", SysHeader.TMSG_APP_ID.getField(sysHeader), SysHeader.TMSG_SVC_ID.getField(sysHeader)); // INFC_ID(part), SVC_ID(eng)
				}
				
				protocol = tierConfig.getProtocol(outlet.toUpperCase());
				
				if(protocol!=null && protocol.equals("http")) {
					host = tierConfig.getHost(outlet.toUpperCase());
					port = tierConfig.getPort(outlet.toUpperCase());
					uri = tierConfig.getUri(outlet.toUpperCase());
					String[] uris = uri.split(",");
					
					int idx = Utils.getRandomNumber(0, (uris.length));
					uri = uris[idx];
					url = String.format("%s://%s:%d%s", protocol,host,port,uri);
					
					String responseStr = null;
					
					if(uri.endsWith("json")) {
						responseStr = TCPClient.executeJsonByApacheHttpClient(url, "POST", SysHeader.toJsonString(sysHeader));
					} else {
						responseStr = new String( TCPClient.executeBytesByApacheHttpClient(url, "POST", SysHeader.toBytes(sysHeader)));
					}
					
					XLog.stdout("MCI_OUT_URL: " + url);
				} else if(protocol!=null && protocol.equals("kafka")) {
					producer.sendMessage(SysHeader.toJsonString(sysHeader));
				}
				/*
				Socket socket = null;
				
				byte [] header = new byte[4];
				int h_len = 2048;
				// Little Edian
				header[3] = (byte)(h_len >> 24);
				header[2] = (byte)(h_len >> 16);
				header[1] = (byte)(h_len >> 8);
				header[0] = (byte)(h_len);
				
				
				final String SERVER_IP = "10.10.52.7";
				//final String SERVER_IP = "172.169.10.85"; 
				final int SERVER_PORT = 15050;
				
				
				try {
					System.out.print("H_LEN:" + new String(header, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String msg = null;
				
				try {
					socket = new Socket();
					socket.setReuseAddress(true);
					//socket.setSoTimeout(10000);
					socket.setSoLinger(true, 3000);
					socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
					System.out.println("CONNECT_SUCESS\n");
					
					OutputStream os = socket.getOutputStream();
					
					System.out.println("HEADER_WRITE");
					os.write(header, 0, header.length);
					
					
					
					byte[] tlgrm = new byte[2048];
					byte sp = (byte)32;
					Arrays.fill(tlgrm, sp);
					byte[] sys_header = SysHeader.toBytes(sysHeader);
					
					if(tlgrm.length > sys_header.length) {
						System.arraycopy(sys_header, 0, tlgrm, 0, sys_header.length);
					} else {
						System.arraycopy(sys_header, 0, tlgrm, 0, tlgrm.length);
					}
					
					XLog.stdout(String.format("SND_BYTES[%s], len:%d", new String(tlgrm), sys_header.length));
					
					os.write(tlgrm);
					os.flush();
					
					InputStream is = socket.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					byte[] rcvBuf = new byte[1024];
					byte[] rcvMsg = new byte[8192];
					
					int read = -1;
					int j = 0;
					int totalBytes = 0;
					while(true) {
						read = bis.read(rcvBuf, 0, rcvBuf.length);
						System.out.printf("READ[%d]..:%d\n", j++, read);
						totalBytes += read;
						if(read < rcvBuf.length) {
							break;
						}
					}
					
					XLog.stdout(String.format("RCV_BYTES [%d]\n", totalBytes));
					
					os.close();
					bis.close();
					
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
			}
		}
	}
	
	
	//@RequestMapping({"/**"})
	@RequestMapping({"**/mcin/**"})
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		XLog.stdout(String.format("PATH [%s]", path));

		
		String contentType = request.getContentType();
		XLog.stdout(String.format("CONTENT-TYPE [%s]", contentType));
		
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
			
			
			
		} // end of contentType
		
	}

}
