package com.dayheart.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.dayheart.util.XLog;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest.Builder;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import kisb.sb.tmsg.SysHeader;

/**
 * TCP, HTTP sender
 * 
 * @author dayheart
 *
 */
public class TCPClient {
	
	
	/**
	 * HTTP Client 를 이용한 바이트 형식 요청 전송
	 * @param url 요청 URL
	 * @param method HTTP Method
	 * @param bytesEntity byte[] 형식의 HTTP 요청 바디
	 * @return 요청에 대한 HTTP 응답 
	 */
	public static byte[] executeBytesByApacheHttpClient(String url, String method, byte[] bytesEntity) {
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/octet-stream");
		//headers.put("Accept", "application/octet-stream");
		
		
		return executeByApacheHttpClient(url, method, headers, bytesEntity);
	}
	
	/**
	 * HTTP Client 를 이용한 JSON 문자형식 요청 전송
	 * @param url 요청 URL
	 * @param method HTTP method
	 * @param jsonString JSON 문자열 형식의 HTTP 요청 바디
	 * @return 요청에 대한 HTTP 응답 
	 */
	public static String executeJsonByApacheHttpClient(String url, String method, String jsonString) {
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		
		return executeByApacheHttpClient(url, method, headers, jsonString);
	}
	
	public static String executeXmlByApacheHttpClient(String url, String method, String xmlString) {
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/xml");
		headers.put("Accept", "application/xml");
		
		return executeByApacheHttpClient(url, method, headers, xmlString);
	}
	
	
	public static String executeByJerseyClient(String url, String method, String jsonString) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		
		return executeByJerseyClient(url, method, headers, jsonString);
	}
	
		
	public static String executeByJerseyClient(String url, String method, Map<String, String> headers, String stringEntity) {
		
		Client client = Client.create();
		
		WebResource webResource = client.resource(url);
		
		Set<String> keySet = headers.keySet();
		for(String key : keySet) {
			//httpGet.setHeader(key, headers.get(key));
			webResource.header(key, headers.get(key));
		}
		
		
		String result = "null";
		ClientResponse response = null;
		
		if(method.equalsIgnoreCase("POST")) {
			
			// case 2) not working
			/*
			webResource.header("Content-Type", "application/json");
			webResource.header("Accept", "application/json");
			response = webResource.post(ClientResponse.class, stringEntity);
			*/
			
			// case 1) working
			//response = webResource.type("application/json").post(ClientResponse.class, stringEntity);
			
			// case 3)
			response = webResource.header("Content-Type", "application/json").post(ClientResponse.class, stringEntity);
		}
		
		if (response.getStatus() != 201) {
			//throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		result = response.getEntity(String.class);
		
		
		XLog.stdout("BY_JERSEY [" + result + "]");
		
		return result;
	}
	
	
	/**
	 * HTTP Client 를 이용한 JSON 문자형식 요청 전송
	 * @param url 요청 URL
	 * @param method HTTP method
	 * @param headers HTTP 요청 헤더
	 * @param stringEntity 요청 바디 
	 * @return 요청에 대한 HTTP 응답 
	 */
	public static String executeByApacheHttpClient(String url, String method, Map<String, String> headers, String stringEntity) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		
		String result = "null";
		
		if(method.equalsIgnoreCase("GET")) {
			
			HttpGet httpGet = new HttpGet(url);
			
			Set<String> keySet = headers.keySet();
			for(String key : keySet) {
				httpGet.setHeader(key, headers.get(key));
			}
			
			try {
				response = httpClient.execute(httpGet);
				result = EntityUtils.toString(response.getEntity());
				
			} catch (ClientProtocolException cpe) {
				cpe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				try {if(response!=null) response.close();}  catch(Exception e) { e.printStackTrace(); }
				try {if(httpClient!=null) httpClient.close();}  catch(Exception e) { e.printStackTrace(); }
			}
			
		} else if(method.equalsIgnoreCase("POST")) {
			HttpPost httpPost = new HttpPost(url);
			
			Set<String> keySet = headers.keySet();
			for(String key : keySet) {
				httpPost.setHeader(key, headers.get(key));
			}
			
			StringEntity postString = null;
			try {
				postString =  new StringEntity(stringEntity);
				httpPost.setEntity(postString);
				response = httpClient.execute(httpPost);
				result = EntityUtils.toString(response.getEntity());
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {if(response!=null) response.close();}  catch(Exception e) { e.printStackTrace(); }
				try {if(httpClient!=null) httpClient.close();}  catch(Exception e) { e.printStackTrace(); }
			}
			
		}
		
		return result;
	}
	
	
	
	/**
	 * HTTP Client 를 이용한 바이트 형식 요청 전송
	 * @param url 전송 url
	 * @param method HTTP method
	 * @param headers HTTP request header
	 * @param bytesEntity byte[] 형식의 HTTP 요청 바디
	 * @return 요청에 대한 HTTP 응답
	 */
	public static byte[] executeByApacheHttpClient(String url, String method, Map<String, String> headers, byte[] bytesEntity) {
		
		XLog.stdout("METHOD [" + method + "]");
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		
		byte[] result = null;
		
		if(method.equalsIgnoreCase("GET")) {
			
			HttpGet httpGet = new HttpGet(url);
			
			Set<String> keySet = headers.keySet();
			for(String key : keySet) {
				httpGet.setHeader(key, headers.get(key));
			}
			
			try {
				response = httpClient.execute(httpGet);
				result = EntityUtils.toByteArray(response.getEntity());
				
			} catch (ClientProtocolException cpe) {
				cpe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				try {if(response!=null) response.close();}  catch(Exception e) { e.printStackTrace(); }
				try {if(httpClient!=null) httpClient.close();}  catch(Exception e) { e.printStackTrace(); }
			}
			
		} else if(method.equalsIgnoreCase("POST")) {
			HttpPost httpPost = new HttpPost(url);
			
			Set<String> keySet = headers.keySet();
			for(String key : keySet) {
				httpPost.setHeader(key, headers.get(key));
			}
			
			
			ByteArrayEntity bae = new ByteArrayEntity(bytesEntity);
			try {
				httpPost.setEntity(bae);
				
				response = httpClient.execute(httpPost);
				
				InputStream sin = response.getEntity().getContent();
				
				byte[] b_result = null;
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				byte[] buf = new byte[8192];
				int len = -1;
				while((len=sin.read(buf)) > 0) {
					//sb.append(new String(buf,0,len));
					byte[] tmp = new byte[len];
					System.arraycopy(buf, 0, tmp, 0, len);
					baos.write(tmp);
				}
				b_result = baos.toByteArray();
				
				XLog.stdout("APACHE_POST_RESPONSE " + url +" [" + new String(b_result) + "]");
				
				return b_result; 
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {if(response!=null) response.close();}  catch(Exception e) { e.printStackTrace(); }
				try {if(httpClient!=null) httpClient.close();}  catch(Exception e) { e.printStackTrace(); }
			}
			
		}
		
		return result;
	}
	
	
	/**
	 * 입력 스트림에서 데이터 읽기
	 * @param sin 입력 스트림
	 * @return 입력 스트림에서 읽은 내용
	 */
	public static byte[] retrieveBodyToBytes(InputStream sin) {
		//ServletInputStream sin = null;
		byte[] result = null;
		
		try {
			//sin = req.getInputStream();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte[] buf = new byte[8192];
			int len = -1;
			while((len=sin.read(buf)) > 0) {
				//sb.append(new String(buf,0,len));
				byte[] tmp = new byte[len];
				System.arraycopy(buf, 0, tmp, 0, len);
				baos.write(tmp);
			}
			result = baos.toByteArray();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if(sin != null) {
				try {
					sin.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		
		/*
		if( result != null)
			System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() + " [" + new String(result) + "]");
		*/
		//XLog.stdout("[" + new String(result) +"], len:" + result.length);
		
		//String flatJson =  SysHeader.flatJson(new String(result));
		//XLog.stdout("TO_FLAT_JSON[" + flatJson + "], len:" + flatJson.length());
		
		return result;
	}
	

}
