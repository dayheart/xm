package kisb.sb.tmsg;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author dayheart
 * 
 * STOCK LOAN FEP HEADER tot. length : 200
 *
 */
public enum FepHeader {
	
	/* 전문전체길이 */
	H_LEN              (0,4, "전체전문길이", "STOCKLOAN Length"),
	
	H_COMCODE (4, 4, "Common code", "STOCKLOAN Common code"),
	
	H_TXCODE             (8, 4, "Tx code", "Stock loan txcode"),
	H_TXGB             (12, 4, "Req res gubun", "STOCKLOAN TX Gubun"),
	H_TRID            (16, 8, "Program id" ,"STOCKLOAN Tr id"),
	H_TRDATE             (24, 8, "Tr date", "STOCKLOAN Tr date"), 
	H_TRTIME 			(32, 8, "Tr time", "STOCKLOAN Tr time"),
	H_SEONO 		(40, 10, "System Seq.", "STOCKLOAN Sys seq."),
	H_LKSEQNO 		(50, 10, "FEP Seq.", "STOCKLOAN FEP seq."),
	H_ID			(60, 10, "User Id", ""),
	H_USERSSN		(70, 13, "주민번호", ""),
	H_CHECK			(83, 1, "Login check", "STOCKLOAN Login YN"), 
	H_ERRCD 		(84, 4, "Response Cdoe", "STOCKLOAN Res Code"), // 0000 == Success
	H_ERRMSG		(88, 80, "Response Msg", "STOCKLOAN Res Msg"),
	H_STS 			(168, 1, "Status", "STOCKLOAN Status"),
	H_INFO			(169, 10, "Interface Id", "STOCKLOAN Interface Id"),
	H_FILLER		(179, 21, "Filler", "");
	
	public static final int SYS_HEADER_LENGTH = 1057;
	private int offset;
	private int len;
	private String ko;
	private String desc;
	
	private FepHeader(int offset, int len, String ko, String desc) {
		this.offset = offset;
		this.len = len;
		this.ko = ko;
		this.desc = desc;
	}
	
	public int offset() { return offset; }
	public int length() { return len; }
	// FOR SUBSTRING
	public int endIndex() { return offset + len; }
	
	
	public String getField(byte[] data) {
		if(this.endIndex() > data.length)
			return "";
		
		byte[] field = new byte[this.len];
		System.arraycopy(data, offset, field, 0, len);
		
		return new String(field);
	}
	
	public String getField(byte[] data, String encode) {
		if(this.endIndex() > data.length)
			return "";
		
		byte[] field = new byte[this.len];
		System.arraycopy(data, offset, field, 0, len);
		
		try {
			return new String(field, encode);
		} catch (Exception e) { // UnsupportedEncodingException
			return new String(field);
		}
	}
	
	public byte[] getFieldByte(byte[] data) {
		byte[] field = new byte[this.len];
		
		if(this.endIndex() <= data.length)
			System.arraycopy(data, offset, field, 0, len);
		
		return field;
	}
	
	public String getFieldSubstring(String data) {
		if(this.endIndex() > data.length())
			return "";
		
		return data.substring(offset, this.endIndex());
	}
	
	
	public Object getFieldObject(Map<String,?> map) {
		
		if(map.get(this.name()) == null)
			return new Object() {};
				
		return map.get(this.name());
	}
	
	public String getField(Map<String,?> map) {
		
		if(map.get(this.name()) == null)
			return "";
				
		return map.get(this.name()).toString();
	}
	
	public String getFieldJson(String json) {
		String value = "";
		
		json = json.replace(" ", "");
		json = json.replace("\r\n", "");
		
		System.out.println("HEADER_FOUND[" + this.name() + "]");
		System.out.println("HEADER_JSON [" + json + "]");
		
		
		int offset = -1;
		String jsonToken = "\":\""; // change the CASE RSPNS_TMSG_OCRN_DT":null
		int tokenLength = jsonToken.length();
		if( json.indexOf(this.name())>-1 ) {
			offset = json.indexOf(this.name()) + this.name().length() + tokenLength;
			
			System.out.println("HEADER_OFFSET [" + this.name() +"], offset:" + offset);
			
			if( json.indexOf("null", offset-1) != (offset-1) ) {
				value = json.substring(offset, json.indexOf("\"", offset));
			}
		}
		
		return value;
	}
	
	
	public static String getPrintString(byte[] data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder();
		
		for(FepHeader field : FepHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(FepHeader field : FepHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getField(data) + "], len:" + field.getField(data).length() + "\n");
		}
		
		return sb.toString();
	}
	
	public static String getPrintString(String data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder();
		
		for(FepHeader field : FepHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(FepHeader field : FepHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getFieldSubstring(data) + "], len:" + field.getFieldSubstring(data).length() + "\n");
		}
		
		return sb.toString();
	}
	
	public static String getPrintString(Map<String,?> map) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder();
		
		for(FepHeader field : FepHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(FepHeader field : FepHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getField(map) + "], len:" + field.getField(map).length() + "\n");
		}
		
		return sb.toString();
	}
	
	/***** TEST *****/
	public void setField(byte[] field, byte[] data) {
		               //src, srcPos, dest, destPos, len
		
		if(field.length < len)
			System.arraycopy(field, 0, data, offset, field.length);
		else
			System.arraycopy(field, 0, data, offset, len);
	}
	
	public void setField(Object ojb, Map<String, Object> map) {
		map.put(this.name(), ojb);
	}
	
	
	
}
