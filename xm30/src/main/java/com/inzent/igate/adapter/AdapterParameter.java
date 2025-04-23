package com.inzent.igate.adapter;

import java.util.HashMap;
import java.util.Map;

public class AdapterParameter {
	
	private Map<Object, Object> data = new HashMap<Object, Object>();
	
	public void put(Object key, Object val) {
		data.put(key, val);
	}
	
	public Object get(Object key) {
		return data.get(key);
	}
	
	public Object getRequestData() {
		return data.get("request.data");
	}
	public void setRequestData(Object requestData) {
		data.put("request.data", requestData);
	}
	
	public void setRequestData(byte[] b) {
		data.put("request.data", b);
	}
	
	public Object getResponseData() {
		return data.get("response.data");
	}
	public void setResponseData(Object responseData) {
		data.put("response.data", responseData);
	}	
}
