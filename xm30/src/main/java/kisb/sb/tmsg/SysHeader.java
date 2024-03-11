package kisb.sb.tmsg;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dayheart.util.XLog;
import com.dayheart.util.ThreadCpuTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * 
 * @author dayheart
 * 
 * KISB SYSTEM HEADER tot. length : 1057
 *
 */
public enum SysHeader {
	
	/* 전문전체길이 */
	ALL_TMSG_LNTH              (0,8, "전체전문길이", "전체전문길이8byte, JSON생략"),
	
	/* 표준전문 기본정보 */
	TMSG_VER_SECD              (8, 2, "전문버전구분코드", "표준전문의 버전 ex, 01, 02"),
	ENVR_INFO_SECD             (10, 1, "환경정보구분코드", "L:localhost, D:개발, T:테스트, P:운영, R:DR"),
	TMSG_ENCR_SECD             (11, 1, "전문암호화구분", "기본 0, 암호화 사용치 않음"),
	TMSG_COMPS_SECD            (12, 1, "전문압축구분코드", "기본 0, 압축 사용치 않음"),
	TMSG_LANG_SECD             (13, 2, "전문언어구분코드", "KO, EN, JP, CN"),
	
	/* 표준전문 글로벌id */
	TMSG_CRT_LINK_SYS_SECD     (15, 3, "전문연계시스템구분코드", "시스템구분코드"),
	FIRST_TMSG_CRT_DT          (18, 17, "최초전문생성일시", "YYYYMMDDHHMMSSTTT"),
	TMSG_CRT_ENVR_INFO_SECD    (35, 1, "전문생성환경정보구분코드", "L:localhost, D:개발, T:테스트, P:운영, R:DR"),
	TMSG_CRT_SNO               (36, 16, "전문생성일련번호", ""),
	TMSG_PRG_NO                (52, 3, "전문진행번호", ""),
	OGTRAN_GUID                (55, 40, "원거래GUID", ""), // org 40 include tmsg_prg_no
	
	/* 요청시스템정보 */
	TMSG_DMND_IPADR            (95, 15, "전문요청IP주소", ""),
	TMSG_DMND_MADR             (110, 17, "전문요청MAC어드레스", ""),
	FIRST_DMND_LINK_SYS_SECD   (127, 3, "최초요청연계시스템구분코드", ""),
	FIRST_TMSG_DMND_DT         (130, 17, "최초전문요청일시", "YYYYMMDDHHMMSSTTT"),
	FIRST_UUID                 (147, 40, "요청 고유ID", "UUID"),
	
	/* 표준전문 송신정보 */
	TRSMT_LINK_SYS_SECD        (187, 3, "송신연계시스템구분코드", ""),
	TRSMT_NODE_NO              (190, 11, "송신노드번호", ""),
	DMND_RSPNS_SECD            (201, 1, "요청응답구분코드", "S:요청, R:응답"),
	TMSG_SYNCZ_SECD            (202, 1, "전문동기화구분코드", "S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보"),
	TMSG_TRSMT_DT              (203, 17, "전문송신일시", "YYYYMMDDHHMMSSTTT"),
	
	/* 표준전문 라우팅정보 */
	INFC_ID                    (220, 20, "인터페이스ID", "MCI/EAI 호출을 위한 인터페이스ID"),
	TRNSC_PRCS_SECD            (240, 1, "트랜잭션처리구분코드", "N:Normal, R:Rollback"),
	TMSG_APP_ID                (241, 50, "전문애플리케이션ID", "수신시스템에서 불러질 실제적인 최종 애플리케이션ID"),
	TMSG_SVC_ID                (291, 50, "전문서비스ID", "수신시스템에서 불러질 실제적인 최종 서비스ID"),
	
	/* 표준전문 응답전문 */
	RSPNS_TMSG_OCRN_DT         (341, 17, "응답전문발생일시", ""),
	TMSG_PRCS_RSLT_SECD        (358, 1, "전문처리결과 구분코드", "0:정상, 1:시스템오류, 2:Timeout"),
	ERR_OCRN_LINK_SYS_SECD     (359, 3, "오류발생연계시스템구분코드", "응답 전무의 오류 발생 시 시스템 코드"),
	TMSG_ERR_MSG_ID            (362, 10, "전문오류메시지ID", "최초 오류 발생시 대표 오류 메시지코드를 설정"),
	SCRN_ESNTL_INPT_ERR_CMPO_ID(372, 50, "화면필수입력오류컴포넌트ID", ""),
	
	/* 연결정보 */
	LINK_NODE_NO               (422, 8, "연계노드번호", "연계서버에서 송시시 노드 정보 설정 ex, IGATE+MCI노드ID(1)+MCI서버(1)"),
	CLNT_IPADR                 (430, 24, "클라이언트IP주소", ""),
	LINK_SESS_ID               (454, 8, "연계세션ID", ""),
	WB_SOCKT_ID                (462, 50, "웹소켓ID", ""),
	SSO_TOKEN_ID               (512, 400, "SSO토큰ID", ""),
	INSTT_TMSG_MNG_NO          (912, 20, "기관전문관리번호", ""),
	WRK_SECD_S4                (932, 4, "업무구분코드S4", ""),
	PUB_KEY                    (936, 66, "중앙회 공개 키", ""),
	DLNG_ITTP_CODE_S4          (1002, 4, "거래종별코드S4", ""),
	AP_CLIENT_ID               (1006, 40, "API Client ID", "중앙회 CLIENT ID"),
	
	/* 예비 */
	SYS_FILLER                 (1046, 11, "전문시스템공통부예비필드", "");
	
	public static final int SYS_HEADER_LENGTH = 1057;
	
	public static final int HEADER_LENGTH = SysHeader.values()[SysHeader.values().length-1].offset() + SysHeader.values()[SysHeader.values().length-1].length();
	private int offset;
	private int len;
	private String ko;
	private String desc;
	
	//private static final Logger logger = LogManager.getLogger(SysHeader.class);
	
	private SysHeader(int offset, int len, String ko, String desc) {
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
	
	
	
	public String getFieldJsonRaw(String json) {
		
		return "";
	}
	
	public static String flatJson(String prettyJson) {
		prettyJson = prettyJson.replace(" ", ""); // loop
		prettyJson = prettyJson.replace("\r\n", ""); // loop
		
		return prettyJson;
	}
	
	public String getFieldJson(String flatJson) {
		String value = "";
		int offset = -1;
		String jsonToken = "\":\""; // change the CASE RSPNS_TMSG_OCRN_DT":null
		int tokenLength = jsonToken.length();
		if( flatJson.indexOf(this.name())>-1 ) {
			offset = flatJson.indexOf(this.name()) + this.name().length() + tokenLength;
			
			//System.out.println("HEADER_OFFSET [" + this.name() +"], offset:" + offset);
			
			if( flatJson.indexOf("null", offset-1) != (offset-1) ) {
				value = flatJson.substring(offset, flatJson.indexOf("\"", offset));
			}
		}
		
		return value;
	}
	
	/**
	 * 
	 * @param prettyJson json 스트링을 사전에 replace 하고, jsonToken 을 파라미터로 받는 방식으로 변경 필요. 2022.6.30
	 * @return
	 */
	public String getFieldPrettyJson(String prettyJson) {
		String value = "";
		
		prettyJson = prettyJson.replace(" ", ""); // loop
		prettyJson = prettyJson.replace("\r\n", ""); // loop
		
		// System.out.println("HEADER_FOUND[" + this.name() + "]"); 
		// System.out.println("HEADER_JSON [" + json + "]"); 
		
		
		int offset = -1;
		String jsonToken = "\":\""; // change the CASE RSPNS_TMSG_OCRN_DT":null
		int tokenLength = jsonToken.length();
		if( prettyJson.indexOf(this.name())>-1 ) {
			offset = prettyJson.indexOf(this.name()) + this.name().length() + tokenLength;
			
			//System.out.println("HEADER_OFFSET [" + this.name() +"], offset:" + offset);
			
			if( prettyJson.indexOf("null", offset-1) != (offset-1) ) {
				value = prettyJson.substring(offset, prettyJson.indexOf("\"", offset));
			}
		}
		
		return value;
	}
	
	
	public static String getPrintJsonString(String data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder("\n");
		
		for(SysHeader field : SysHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SysHeader field : SysHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getFieldPrettyJson(data) + "], len:" + field.getFieldPrettyJson(data).length() + "\n");
		}
		
		return sb.toString();
		
	}
	
	
	public static String getPrintString(byte[] data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder();
		
		for(SysHeader field : SysHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SysHeader field : SysHeader.values()) {
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
		
		for(SysHeader field : SysHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SysHeader field : SysHeader.values()) {
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
		
		for(SysHeader field : SysHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SysHeader field : SysHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getField(map) + "], len:" + field.getField(map).length() + "\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * @deprecated see HEADER_LENGTH
	 * @return 시스템 헤더 총 길이
	 */
	public static int getLength() {
		
		int length = 0;
		
		for(SysHeader field : SysHeader.values()) {
			length += field.len;
		}
		
		return length;
	}
	
	public static byte[] toBytes(Map<String, ?> map) {
		byte[] rt = new byte[SysHeader.getLength()];
		
		for(SysHeader field : SysHeader.values()) {
			Object value = map.get(field.name());
			if(value!=null) {
				
				String str = null;
				if (value instanceof String) {
					str = (String) value;
				} else {
					str = value.toString();
				}
				
				if(str!=null)
					System.arraycopy(str.getBytes(), 0, rt, field.offset, str.getBytes().length);
				
			}
		}
		
		return rt;
	}
	
	
	
	/**
	 *  Flat JSON 문자열을 byte[] 전문으로
	 * @param flatJson
	 * @return byte[] 형태 전문
	 */
	public static byte[] toBytes(String flatJson) {
		
		long starttime = System.currentTimeMillis();
		
		byte[] rt = new byte[SysHeader.getLength()];
		
		for(SysHeader field : SysHeader.values()) {
			String value = field.getFieldJson(flatJson);
			if(value!=null) {
				System.arraycopy(value.getBytes(), 0, rt, field.offset, value.getBytes().length);
			}
		}
		
		long endtime = System.currentTimeMillis();
		
		XLog.stdout("elapsed time: " + (endtime-starttime));
		
		return rt;
	}
	
	
	/**
	 * pretty JSON 스트링 매번 파싱 시 오버헤드 측정용 함수
	 * @param prettyJson
	 * @return
	 */
	public static byte[] toBytesPretty(String prettyJson) {
		long starttime = System.currentTimeMillis();
		
		byte[] rt = new byte[SysHeader.getLength()];
		
		for(SysHeader field : SysHeader.values()) {
			String value = field.getFieldJson( flatJson(prettyJson));
			if(value!=null) {
				System.arraycopy(value.getBytes(), 0, rt, field.offset, value.getBytes().length);
			}
		}
		
		long endtime = System.currentTimeMillis();
		
		XLog.stdout("elapsed time: " + (endtime-starttime));
		
		return rt;
	}
	
	
	public static Map<String, Object> toMap(byte[] telegram) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(SysHeader field : SysHeader.values()) {
			map.put(field.name(), field.getField(telegram).trim());
		}
		
		return map;
	}
	
	public static String toJsonString(Map<String, Object> map) {
		ObjectMapper objMapper = new ObjectMapper();
		
		String json = "";
		
		try {
			//String json = objMapper.writeValueAsString(map);
			json = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
			
		} catch (JsonProcessingException jsonProcEx) {
			jsonProcEx.printStackTrace();
		}
		return json;
	}
	
	@JacksonXmlRootElement(localName = "STANDARD_TELEGRAM")
	public static class Telegram {
		private Map<String, Object> system_header;
		
		public Map<String, Object> getHeader() {
			return Collections.unmodifiableMap(system_header);
		}
		
		public void setHeader(Map<String, Object> header) {
			this.system_header = header;
		}
	}
	
	public static String toXmlString(Map<String, Object> map) {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		
		Telegram tlgm = new Telegram();
		tlgm.setHeader(map);
		
		
		String xml = "";
		try {
			xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tlgm);
			XLog.stdout(String.format("XML[%s]", xml));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xml;
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
	
	
	/**** GENERATE *****/
	
	
	/**
	 * 전문 진행 번호 증가 함수
	 * @param sysHeader 전문 진행 번호를 증가시킬 헤더
	 */
	public static void increasePrgNo(byte[] sysHeader) {
		String s_no = SysHeader.TMSG_PRG_NO.getField(sysHeader);
		
		int i_no = Integer.parseInt(s_no);
		//XLog.stdout(SysHeader.TMSG_PRG_NO.name() + " [" + i_no + "]");
		
		++i_no;
		
		s_no = String.format("%03d", i_no);
		
		SysHeader.TMSG_PRG_NO.setField(s_no.getBytes(), sysHeader);
		//XLog.stdout(SysHeader.TMSG_PRG_NO.name() + " [" + SysHeader.TMSG_PRG_NO.getField(sysHeader) + "]");
		
	}
	
	/**
	 * 전문 진행 번호 증가 함수
	 * @param sysHeader 전문 진행 번호를 증가시킬 헤더
	 */
	public static void increasePrgNo(Map<String, Object> sysHeader) {
		String s_no = SysHeader.TMSG_PRG_NO.getField(sysHeader);
		
		int i_no = Integer.parseInt(s_no);
		//XLog.stdout(SysHeader.TMSG_PRG_NO.name() + " [" + i_no + "]");
		
		++i_no;
		
		s_no = String.format("%03d", i_no);
		
		SysHeader.TMSG_PRG_NO.setField(s_no, sysHeader);
	}
	
	/**
	 * 
	 * @param telegram 전송할 전문 배열
	 * @param sysCd 전문 생성 시스템 구분 코드 3자리, ex) MDI
	 * @param envCd L:localhost, D:개발, T:테스트, P:운영, R:DR
	 */
	public static void setGUID(byte[] telegram, String sysCd, String envCd) {
		
		java.util.Date date = new Date();
		long now = date.getTime();
		int sno = (int)(Math.random() * 1000000000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		/* 표준전문 기본정보 */		
		String TMSG_CRT_LINK_SYS_SECD = sysCd;
		String FIRST_TMSG_CRT_DT = sdf.format(date); // 최초 전문 생성 일시
		String TMSG_CRT_ENVR_INFO_SECD = envCd;
		String TMSG_CRT_SNO = String.format("%016d", sno);
		String TMSG_PRG_NO = "001";
		String OGTRAN_GUID = TMSG_CRT_LINK_SYS_SECD + FIRST_TMSG_CRT_DT + TMSG_CRT_ENVR_INFO_SECD + TMSG_CRT_SNO + TMSG_PRG_NO ;
		
		SysHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT.getBytes(), telegram);
		SysHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD.getBytes(), telegram);
		SysHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO.getBytes(), telegram);
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO.getBytes(), telegram);
		SysHeader.OGTRAN_GUID.setField(OGTRAN_GUID.getBytes(), telegram);		
	}
	
	/**
	 * 
	 * @param map 전송할 전문 해시맵
	 * @param sysCd 전문 생성 시스템 구분 코드 3자리, ex) MDI
	 * @param envCd L:localhost, D:개발, T:테스트, P:운영, R:DR
	 */
	public static void setGUID(Map<String, Object> map, String sysCd, String envCd) {
		java.util.Date date = new Date();
		long now = date.getTime();
		int sno = (int)(Math.random() * 1000000000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		/* 표준전문 기본정보 */		
		String TMSG_CRT_LINK_SYS_SECD = sysCd;
		String FIRST_TMSG_CRT_DT = sdf.format(date); // 최초 전문 생성 일시
		String TMSG_CRT_ENVR_INFO_SECD = envCd;
		String TMSG_CRT_SNO = String.format("%016d", sno);
		String TMSG_PRG_NO = "001";
		String OGTRAN_GUID = TMSG_CRT_LINK_SYS_SECD + FIRST_TMSG_CRT_DT + TMSG_CRT_ENVR_INFO_SECD + TMSG_CRT_SNO + TMSG_PRG_NO ;
		
		SysHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD, map);
		SysHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT, map);
		SysHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD, map);
		SysHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO, map);
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO, map);
		SysHeader.OGTRAN_GUID.setField(OGTRAN_GUID, map);
	}
	
	
	/**
	 * 요청 시스템 정보 세팅
	 * @param telegram 전송할 전문 배열
	 * @param sysCd 최초요청연계시스템구분코드
	 */
	public static void setDMND(byte[] telegram, String sysCd) {
		
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String ipstr = "null"; 
		InetAddress ip = null;
		StringBuilder macSb = new StringBuilder();
		try {
			ip = InetAddress.getLocalHost();
			ipstr = ip.getHostAddress();
			
			NetworkInterface nic = NetworkInterface.getByInetAddress(ip);
			byte[] mac = nic.getHardwareAddress();
			
			for(int i=0; i<mac.length; i++) {
				macSb.append( String.format("%02X%s", mac[i], (i<mac.length-1)?"-":"") );
			}
			
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (UnknownHostException uke) {
			uke.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* 요청시스템정보 */
		String TMSG_DMND_IPADR = ipstr;
		String TMSG_DMND_MADR = macSb.toString();
		String FIRST_DMND_LINK_SYS_SECD = sysCd;
		String FIRST_TMSG_DMND_DT = sdf.format(date);
		String FIRST_UUID = UUID.randomUUID().toString();
		
		SysHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR.getBytes(), telegram);
		SysHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR.getBytes(), telegram);
		SysHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT.getBytes(), telegram);
		SysHeader.FIRST_UUID.setField(FIRST_UUID.getBytes(), telegram);		
	}
	
	/**
	 * 요청 시스템 정보 세팅
	 * @param map 전송할 전문 해시맵
	 * @param sysCd 최초요청연계시스템구분코드
	 */
	public static void setDMND(Map<String, Object> map, String sysCd) {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String ipstr = "null"; 
		InetAddress ip = null;
		StringBuilder macSb = new StringBuilder();
		try {
			ip = InetAddress.getLocalHost();
			
			System.out.println(String.format("LOCALHOST_ADDR:[%s]", ip));
			ipstr = ip.getHostAddress();
			NetworkInterface nic = NetworkInterface.getByInetAddress(ip);
			System.out.println(String.format("LOCALHOST_NETIF:[%s]", nic));
			
			byte[] mac = nic.getHardwareAddress();
			// mac == null 127.0.0.1
			if(mac!=null) {
				for(int i=0; i<mac.length; i++) {
					macSb.append( String.format("%02X%s", mac[i], (i<mac.length-1)?"-":"") );
				}
			}
			
			
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (UnknownHostException uke) {
			uke.printStackTrace();
		}
		
		/* 요청시스템정보 */
		String TMSG_DMND_IPADR = ipstr;
		String TMSG_DMND_MADR = macSb.toString();
		String FIRST_DMND_LINK_SYS_SECD = sysCd;
		String FIRST_TMSG_DMND_DT = sdf.format(date);
		String FIRST_UUID = UUID.randomUUID().toString();
		
		SysHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR, map);
		SysHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR, map);
		SysHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD, map);
		SysHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT, map);
		SysHeader.FIRST_UUID.setField(FIRST_UUID, map);	
	}
	
	
	/**
	 * 표준전문 송신정보
	 * @param telegram 전문 배열
	 * @param sysCd 송신연계시스템구분코드
	 * @param rspnsCd 요청응답구분코드, S:요청, R:응답
	 * @param syncCd 전문동기화구분코드, S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
	 */
	public static void setTRMST(byte[] telegram, String sysCd, String rspnsCd, String syncCd) {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String TRSMT_LINK_SYS_SECD = sysCd;
		
		String TRSMT_NODE_NO = "UnknownHost";
		
		try {
			TRSMT_NODE_NO = InetAddress.getLocalHost().getHostName().toUpperCase();
		} catch (UnknownHostException e) {
			TRSMT_NODE_NO = e.getMessage().toUpperCase();
		}
		
		String DMND_RSPNS_SECD = rspnsCd; // S:요청, R:응답
		String TMSG_SYNCZ_SECD = syncCd; // S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
		String TMSG_TRSMT_DT = sdf.format(date);
		
		SysHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO.getBytes(), telegram);
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD.getBytes(), telegram);
		SysHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD.getBytes(), telegram);
		SysHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT.getBytes(), telegram);
		
	}
	
	/**
	 * 표준전문 송신정보
	 * @param map 전문 해시맵
	 * @param sysCd 송신연계시스템구분코드
	 * @param rspnsCd 요청응답구분코드, S:요청, R:응답
	 * @param syncCd 전문동기화구분코드, S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
	 */
	public static void setTRMST(Map<String, Object> map, String sysCd, String rspnsCd, String syncCd) {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String TRSMT_LINK_SYS_SECD = sysCd;
		
		String TRSMT_NODE_NO = "UnknownHost";
		
		try {
			TRSMT_NODE_NO = InetAddress.getLocalHost().getHostName().toUpperCase();
		} catch (UnknownHostException e) {
			TRSMT_NODE_NO = e.getMessage().toUpperCase();
		}
		
		String DMND_RSPNS_SECD = rspnsCd; // S:요청, R:응답
		String TMSG_SYNCZ_SECD = syncCd; // S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
		String TMSG_TRSMT_DT = sdf.format(date);
		
		SysHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD, map);
		SysHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO, map);
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD, map);
		SysHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD, map);
		SysHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT, map);
	}
	
	
	/**
	 * 표준전문 라우팅 정보 
	 * @param telegram 전문 배열
	 * @param infcId 인터페이스ID
	 * @param prcsCd 트랜잭션처리구분코드. N:Normal, R:Rollback
	 * @param appId 전문애플리케이션ID
	 * @param svcId 전문서비스
	 */
	public static void setINFC(byte[] telegram, String infcId, String prcsCd, String appId, String svcId) {
		
		String INFC_ID = infcId;
		String TRNSC_PRCS_SECD = prcsCd;
		String TMSG_APP_ID = appId;
		String TMSG_SVC_ID = svcId;
		
		SysHeader.INFC_ID.setField(INFC_ID.getBytes(), telegram);
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD.getBytes(), telegram);
		SysHeader.TMSG_APP_ID.setField(TMSG_APP_ID.getBytes(), telegram);
		SysHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID.getBytes(), telegram);
	}
	
	
	/**
	 * 표준전문 라우팅 정보 
	 * @param map 전문 해시맵
	 * @param infcId 인터페이스ID
	 * @param prcsCd 트랜잭션처리구분코드. N:Normal, R:Rollback
	 * @param appId 전문애플리케이션ID
	 * @param svcId 전문서비스
	 */
	public static void setINFC(Map<String, Object> map, String infcId, String prcsCd, String appId, String svcId) {
		
		String INFC_ID = infcId;
		String TRNSC_PRCS_SECD = prcsCd;
		String TMSG_APP_ID = appId;
		String TMSG_SVC_ID = svcId;
		
		SysHeader.INFC_ID.setField(INFC_ID, map);
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD, map);
		SysHeader.TMSG_APP_ID.setField(TMSG_APP_ID, map);
		SysHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID, map);
	}
	
	/**
	 * 표준전문 응답전문
	 * @param telegram 전문 배열
	 * @param rsltCd 전문처리결과 구분코드. 0:정상, 1:시스템오류, 2:Timeout 
	 * @param sysCd 오류발생연계시스템구분코드
	 * @param errMsgId 전문오류메시지ID
	 * @param errCmpoId 화면필수입력오류컴포넌트ID
	 */
	public static void setRSPNS(byte[] telegram, String rsltCd, String sysCd, String errMsgId, String errCmpoId) {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String RSPNS_TMSG_OCRN_DT = sdf.format(date);
		String TMSG_PRCS_RSLT_SECD = rsltCd;
		String ERR_OCRN_LINK_SYS_SECD = sysCd;
		String TMSG_ERR_MSG_ID = errMsgId;
		String SCRN_ESNTL_INPT_ERR_CMPO_ID = errCmpoId;
		
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT.getBytes(), telegram);
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD.getBytes(), telegram);
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID.getBytes(), telegram);
		SysHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID.getBytes(), telegram);
	}
	
	/**
	 * 표준전문 응답전문
	 * @param map 전문 해시맵
	 * @param rsltCd 전문처리결과 구분코드. 0:정상, 1:시스템오류, 2:Timeout 
	 * @param sysCd 오류발생연계시스템구분코드
	 * @param errMsgId 전문오류메시지ID
	 * @param errCmpoId 화면필수입력오류컴포넌트ID
	 */
	public static void setRSPNS(Map<String, Object> map, String rsltCd, String sysCd, String errMsgId, String errCmpoId) {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String RSPNS_TMSG_OCRN_DT = sdf.format(date);
		String TMSG_PRCS_RSLT_SECD = rsltCd;
		String ERR_OCRN_LINK_SYS_SECD = sysCd;
		String TMSG_ERR_MSG_ID = errMsgId;
		String SCRN_ESNTL_INPT_ERR_CMPO_ID = errCmpoId;
		
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT, map);
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD, map);
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD, map);
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID, map);
		SysHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID, map);
	}
	

	
	public static void t_main(String[] args) {
		
		System.out.println("HEADER_LEN: " + SysHeader.getLength() );
		
		int headerLen = SysHeader.getLength();
		byte[] b_header = new byte[headerLen];
		Map<String, Object> m_header = new HashMap<String, Object>();
		
		
		SysHeader.setGUID(b_header, "WAS", "D");
		SysHeader.setDMND(b_header, "WAS");
		
		SysHeader.setGUID(m_header, "WAS", "L");
		SysHeader.setDMND(m_header, "WAS");
		
		System.out.println("header\n[" + m_header.toString() + "]");
		
		
		int msg_len = 2048;
		byte[] telegram = new byte[msg_len];
		Map<String, Object> map = new HashMap<String, Object>();
		
		StringBuilder sb = new StringBuilder(); // 시스템구분코드
		
		java.util.Date date = new Date();
		long now = date.getTime();
		int sno = (int)(Math.random() * 1000000000);
		
		String time = Long.toString(now);
		System.out.println("STRING [" + time + "], len: "+ time.length()); // STRING [1645408492740], len: 13
		time = Long.toHexString(now);
		System.out.println("HEXSTR [" + time + "], len: "+ time.length()); // HEXSTR [17f19fd1cc4], len: 11
		time = Long.toOctalString(now);
		System.out.println("OCTSTR [" + time + "], len: "+ time.length()); // OCTSTR [27743177216304], len: 14
		time = Long.toBinaryString(now);
		System.out.println("BINSTR [" + time + "], len: "+ time.length()); // BINSTR [10111111100011001111111010001110011000100], len: 41
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		/* 전문전체길이 */
		String ALL_TMSG_LNTH = String.format("%08d", msg_len);
		
		String TMSG_VER_SECD = "01";
		String ENVR_INFO_SECD = "D";
		String TMSG_ENCR_SECD = "0";
		String TMSG_COMPS_SECD = "0";
		String TMSG_LANG_SECD = "KO";
		
		/* 표준전문 기본정보 */		
		String TMSG_CRT_LINK_SYS_SECD = "MDI";
		String FIRST_TMSG_CRT_DT = sdf.format(date); // 최초 전문 생성 일시
		String TMSG_CRT_ENVR_INFO_SECD = "D";
		String TMSG_CRT_SNO = String.format("%016d", sno);
		String TMSG_PRG_NO = "001";
		String OGTRAN_GUID = TMSG_CRT_LINK_SYS_SECD + FIRST_TMSG_CRT_DT + TMSG_CRT_ENVR_INFO_SECD + TMSG_CRT_SNO + TMSG_PRG_NO ;
		
		
		String ipstr = "null"; 
		InetAddress ip = null;
		StringBuilder macSb = new StringBuilder();
		try {
			ip = InetAddress.getLocalHost();
			ipstr = ip.getHostAddress();
			NetworkInterface nic = NetworkInterface.getByInetAddress(ip);
			byte[] mac = nic.getHardwareAddress();
			
			for(int i=0; i<mac.length; i++) {
				macSb.append( String.format("%02X%s", mac[i], (i<mac.length-1)?"-":"") );
			}
			
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (UnknownHostException uke) {
			uke.printStackTrace();
		}
		
		/* 요청시스템정보 */
		String TMSG_DMND_IPADR = ipstr;
		String TMSG_DMND_MADR = macSb.toString();
		String FIRST_DMND_LINK_SYS_SECD = "MDI";
		String FIRST_TMSG_DMND_DT = FIRST_TMSG_CRT_DT;
		String FIRST_UUID = UUID.randomUUID().toString();
		
		/* 표준전문 송신정보 */
		String TRSMT_LINK_SYS_SECD = "MCI";
		String TRSMT_NODE_NO = "";
		String DMND_RSPNS_SECD = "S"; // S:요청, R:응답
		String TMSG_SYNCZ_SECD = "S"; // S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
		String TMSG_TRSMT_DT = FIRST_TMSG_CRT_DT;
		
		/* 표준전문 라우팅정보 */
		String INFC_ID = "OPENING";
		String TRNSC_PRCS_SECD = "N";
		String TMSG_APP_ID = "";
		String TMSG_SVC_ID = "";
		
		/* 표준전문 응답전문 */
		String RSPNS_TMSG_OCRN_DT = FIRST_TMSG_CRT_DT;
		String TMSG_PRCS_RSLT_SECD = "0"; // 0:정상, 1:시스템오류, 2:Timeout
		String ERR_OCRN_LINK_SYS_SECD = ""; // "오류발생연계시스템구분코드", "응답 전무의 오류 발생 시 시스템 코드"
		String TMSG_ERR_MSG_ID = ""; // "전문오류메시지ID", "최초 오류 발생시 대표 오류 메시지코드를 설정";
		String SCRN_ESNTL_INPT_ERR_CMPO_ID = ""; // 화면필수입력오류컴포넌트ID
		
		/* 연결정보 */
		String LINK_NODE_NO = "IGATE"; //연계서버에서 송시시 노드 정보 설정 ex, IGATE+MCI노드ID(1)+MCI서버(1)
		String CLNT_IPADR = ipstr; // 클라이언트IP주소
		String LINK_SESS_ID = ""; // 연계세션ID
		String WB_SOCKT_ID = ""; // 웹소켓ID
		String SSO_TOKEN_ID = ""; // SSO토큰ID
		String INSTT_TMSG_MNG_NO = ""; // 기관전문관리번호
		String WRK_SECD_S4 = ""; // 업무구분코드S4
		String PUB_KEY = ""; // 중앙회 공개 키
		String DLNG_ITTP_CODE_S4 = ""; // 거래종별코드S4
		String AP_CLIENT_ID = ""; // "API Client ID", "중앙회 CLIENT ID"
						
		//String TMSG_CRT_SNO
		/* 전문전체길이 */
		SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), telegram);
		SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH, map);
		
		
		/* 표준전문 기본정보 */	
		SysHeader.TMSG_VER_SECD.setField(TMSG_VER_SECD.getBytes(), telegram);
		SysHeader.TMSG_VER_SECD.setField(TMSG_VER_SECD, map);
		
		SysHeader.ENVR_INFO_SECD.setField(ENVR_INFO_SECD.getBytes(), telegram);
		SysHeader.ENVR_INFO_SECD.setField(ENVR_INFO_SECD, map);
		
		SysHeader.TMSG_ENCR_SECD.setField(TMSG_ENCR_SECD.getBytes(), telegram);
		SysHeader.TMSG_ENCR_SECD.setField(TMSG_ENCR_SECD, map);
		
		SysHeader.TMSG_COMPS_SECD.setField(TMSG_COMPS_SECD.getBytes(), telegram);
		SysHeader.TMSG_COMPS_SECD.setField(TMSG_COMPS_SECD, map);
		
		SysHeader.TMSG_LANG_SECD.setField(TMSG_LANG_SECD.getBytes(), telegram);
		SysHeader.TMSG_LANG_SECD.setField(TMSG_LANG_SECD, map);
		
		SysHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD, map);
		
		SysHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT.getBytes(), telegram);
		SysHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT, map);
		
		SysHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD.getBytes(), telegram);
		SysHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD, map);
		
		SysHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO.getBytes(), telegram);
		SysHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO, map);
		
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO.getBytes(), telegram);
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO, map);
		
		SysHeader.OGTRAN_GUID.setField(OGTRAN_GUID.getBytes(), telegram);
		SysHeader.OGTRAN_GUID.setField(OGTRAN_GUID, map);
		
		
		/* 요청시스템정보 */
		SysHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR.getBytes(), telegram);
		SysHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR, map);
		
		SysHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR.getBytes(), telegram);
		SysHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR, map);
		
		SysHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD, map);
		
		SysHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT.getBytes(), telegram);
		SysHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT, map);
		
		SysHeader.FIRST_UUID.setField(FIRST_UUID.getBytes(), telegram);
		SysHeader.FIRST_UUID.setField(FIRST_UUID, map);
		
		
		/* 표준전문 송신정보 */
		SysHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD, map);
		
		SysHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO.getBytes(), telegram);
		SysHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO, map);
		
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD.getBytes(), telegram);
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD, map);
		
		SysHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD.getBytes(), telegram);
		SysHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD, map);
		
		SysHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT.getBytes(), telegram);
		SysHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT, map);
		
		/* 표준전문 라우팅정보 */
		SysHeader.INFC_ID.setField(INFC_ID.getBytes(), telegram);
		SysHeader.INFC_ID.setField(INFC_ID, map);
		
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD.getBytes(), telegram);
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD, map);
		
		SysHeader.TMSG_APP_ID.setField(TMSG_APP_ID.getBytes(), telegram);
		SysHeader.TMSG_APP_ID.setField(TMSG_APP_ID, map);
		
		SysHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID.getBytes(), telegram);
		SysHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID, map);
		
		
		/* 표준전문 응답전문 */
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT.getBytes(), telegram);
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT, map);
		
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD.getBytes(), telegram);
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD, map);
		
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD.getBytes(), telegram);
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD, map);
		
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID.getBytes(), telegram);
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID, map);
		
		SysHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID.getBytes(), telegram);
		SysHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID, map);
		
		
		/* 연결정보 */
		SysHeader.LINK_NODE_NO.setField(LINK_NODE_NO.getBytes(), telegram);
		SysHeader.LINK_NODE_NO.setField(LINK_NODE_NO, map);
		
		SysHeader.CLNT_IPADR.setField(CLNT_IPADR.getBytes(), telegram);
		SysHeader.CLNT_IPADR.setField(CLNT_IPADR, map);
		
		SysHeader.LINK_SESS_ID.setField(LINK_SESS_ID.getBytes(), telegram);
		SysHeader.LINK_SESS_ID.setField(LINK_SESS_ID, map);
		
		SysHeader.WB_SOCKT_ID.setField(WB_SOCKT_ID.getBytes(), telegram);
		SysHeader.WB_SOCKT_ID.setField(WB_SOCKT_ID, map);
		
		SysHeader.SSO_TOKEN_ID.setField(SSO_TOKEN_ID.getBytes(), telegram);
		SysHeader.SSO_TOKEN_ID.setField(SSO_TOKEN_ID, map);
		
		SysHeader.INSTT_TMSG_MNG_NO.setField(INSTT_TMSG_MNG_NO.getBytes(), telegram);
		SysHeader.INSTT_TMSG_MNG_NO.setField(INSTT_TMSG_MNG_NO, map);
		
		SysHeader.WRK_SECD_S4.setField(WRK_SECD_S4.getBytes(), telegram);
		SysHeader.WRK_SECD_S4.setField(WRK_SECD_S4, map);
		
		SysHeader.PUB_KEY.setField(PUB_KEY.getBytes(), telegram);
		SysHeader.PUB_KEY.setField(PUB_KEY, map);
		
		SysHeader.DLNG_ITTP_CODE_S4.setField(DLNG_ITTP_CODE_S4.getBytes(), telegram);
		SysHeader.DLNG_ITTP_CODE_S4.setField(DLNG_ITTP_CODE_S4, map);
		
		SysHeader.AP_CLIENT_ID.setField(AP_CLIENT_ID.getBytes(), telegram);
		SysHeader.AP_CLIENT_ID.setField(AP_CLIENT_ID, map);
		
		String str = new String(telegram);
		
		
		/*
		System.out.printf("ALLT_MSG_LNTH (%s):     [%s]\n",  SysHeader.ALL_TMSG_LNTH.ko, SysHeader.ALL_TMSG_LNTH.getField(data));
		System.out.printf("FIRST_TMSG_CRT_DT (%s): [%s]\n",  SysHeader.FIRST_TMSG_CRT_DT.ko, SysHeader.FIRST_TMSG_CRT_DT.getFieldSubstring(str));
		System.out.printf("ALLT_MSG_LNTH (%s):     [%s]\n",  SysHeader.ALL_TMSG_LNTH.ko, SysHeader.ALL_TMSG_LNTH.getFieldObject(map));
		*/
		
		System.out.println("#### telgram #####");
		System.out.println("[" + str + "], len: " + str.length());
		
		System.out.println("##### byte[] #####");
		System.out.println(SysHeader.getPrintString(telegram));
		System.out.println("##### String #####");
		System.out.println(SysHeader.getPrintString(str));
		System.out.println("##### HashMap #####");
		System.out.println(SysHeader.getPrintString(map));
		
		
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			//String json = objMapper.writeValueAsString(map);
			String json = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
			
			System.out.println("#### JSON TELEGRAM #####");
			System.out.println("[" + json + "], len: " + str.length());
			
			System.out.println("##### JSON VIEW #####");
			System.out.println(SysHeader.getPrintJsonString(json));
			
		} catch (JsonProcessingException jsonProcEx) {
			jsonProcEx.printStackTrace();
		}
		
		/*
		
		byte[] sysHeader = new byte[SysHeader.SYS_HEADER_LENGTH]; 
		SysHeader.setTransmissionHeader(sysHeader, 1529, null, "MDI", 1, "MCI", "S", "RESUME", "127.0.0.1");
		System.out.println("##### REQ_SYS_HEADER #####");
		System.out.println(SysHeader.getPrintString(sysHeader));
		SysHeader.setResponseHeader(sysHeader, 1887, 2, "N", "0", "", "");
		System.out.println("##### RES_SYS_HEADER #####");
		System.out.println(SysHeader.getPrintString(sysHeader));
		
		*/
	}
	
	
	// TMSG_CRT_LINK_SYS_SECD     (15, 3, "전문연계시스템구분코드", "시스템구분코드"),
	public static void setTransmissionHeader( byte[] sysHeader
			, int len // 전체전문길이8byte, JSON생략
			, String ogtran_guid // 원거래GUID
			, String tmsg_crt_link_sys_secd // 시스템구분코드 ex) MDI
			, int tmsg_prg_no // 전문진행번호
			, String trsmt_link_sys_secd // 송신연계시스템구분코드 ex) MCI
			, String tmsg_syncz_secd // S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
			, String infc_id
			, String clnt_ipadr
			) {
		
		if( len < SysHeader.SYS_HEADER_LENGTH )
			len = SysHeader.SYS_HEADER_LENGTH;
		
		int msg_len = len;
		
		java.util.Date date = new Date();
		int sno = (int)(Math.random() * 1000000000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		/* 전문전체길이 */
		String ALL_TMSG_LNTH = String.format("%08d", msg_len);
		
		String TMSG_VER_SECD = "01";
		String ENVR_INFO_SECD = "D";
		String TMSG_ENCR_SECD = "0";
		String TMSG_COMPS_SECD = "0";
		String TMSG_LANG_SECD = "KO";
		
		/* 표준전문 기본정보 */		
		String TMSG_CRT_LINK_SYS_SECD = tmsg_crt_link_sys_secd; // MDI
		String FIRST_TMSG_CRT_DT = sdf.format(date); // 최초 전문 생성 일시
		String TMSG_CRT_ENVR_INFO_SECD = "D";
		String TMSG_CRT_SNO = String.format("%016d", sno);
		String TMSG_PRG_NO = String.format("%03d", tmsg_prg_no);
		
		String OGTRAN_GUID;
		if(ogtran_guid != null)
			OGTRAN_GUID = ogtran_guid;
		else
			OGTRAN_GUID = TMSG_CRT_LINK_SYS_SECD + FIRST_TMSG_CRT_DT + TMSG_CRT_ENVR_INFO_SECD + TMSG_CRT_SNO + TMSG_PRG_NO ;
		
		
		String ipstr = "null"; 
		InetAddress ip = null;
		StringBuilder macSb = new StringBuilder();
		try {
			ip = InetAddress.getLocalHost();
			ipstr = ip.getHostAddress();
			NetworkInterface nic = NetworkInterface.getByInetAddress(ip);
			byte[] mac = nic.getHardwareAddress();
			
			for(int i=0; i<mac.length; i++) {
				macSb.append( String.format("%02X%s", mac[i], (i<mac.length-1)?"-":"") );
			}
			
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (UnknownHostException uke) {
			uke.printStackTrace();
		}
		
		/* 요청시스템정보 */
		String TMSG_DMND_IPADR = ipstr;
		String TMSG_DMND_MADR = macSb.toString();
		String FIRST_DMND_LINK_SYS_SECD = tmsg_crt_link_sys_secd; //MDI
		String FIRST_TMSG_DMND_DT = FIRST_TMSG_CRT_DT;
		String FIRST_UUID = UUID.randomUUID().toString();
		
		/* 표준전문 송신정보 */
		String TRSMT_LINK_SYS_SECD = trsmt_link_sys_secd; // MCI
		String TRSMT_NODE_NO = "";
		String DMND_RSPNS_SECD = "S"; // S:요청, R:응답
		String TMSG_SYNCZ_SECD = tmsg_syncz_secd; // S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
		String TMSG_TRSMT_DT = FIRST_TMSG_CRT_DT;
		
		/* 표준전문 라우팅정보 */
		String INFC_ID = infc_id;
		String TRNSC_PRCS_SECD = ""; // N:Normal, R:Rollback
		String TMSG_APP_ID = "";
		String TMSG_SVC_ID = "";
		
		/* 표준전문 응답전문 */
		String RSPNS_TMSG_OCRN_DT = "";
		String TMSG_PRCS_RSLT_SECD = ""; // 0:정상, 1:시스템오류, 2:Timeout
		String ERR_OCRN_LINK_SYS_SECD = ""; // "오류발생연계시스템구분코드", "응답 전무의 오류 발생 시 시스템 코드"
		String TMSG_ERR_MSG_ID = ""; // "전문오류메시지ID", "최초 오류 발생시 대표 오류 메시지코드를 설정";
		String SCRN_ESNTL_INPT_ERR_CMPO_ID = ""; // 화면필수입력오류컴포넌트ID
		
		/* 연결정보 */
		String LINK_NODE_NO = "IGATE"; //연계서버에서 송시시 노드 정보 설정 ex, IGATE+MCI노드ID(1)+MCI서버(1)
		String CLNT_IPADR = clnt_ipadr; // 클라이언트IP주소
		String LINK_SESS_ID = ""; // 연계세션ID
		String WB_SOCKT_ID = ""; // 웹소켓ID
		String SSO_TOKEN_ID = ""; // SSO토큰ID
		String INSTT_TMSG_MNG_NO = ""; // 기관전문관리번호
		String WRK_SECD_S4 = ""; // 업무구분코드S4
		String PUB_KEY = ""; // 중앙회 공개 키
		String DLNG_ITTP_CODE_S4 = ""; // 거래종별코드S4
		String AP_CLIENT_ID = ""; // "API Client ID", "중앙회 CLIENT ID"
						
		//String TMSG_CRT_SNO
		/* 전문전체길이 */
		SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), sysHeader);
		
		
		/* 표준전문 기본정보 */	
		SysHeader.TMSG_VER_SECD.setField(TMSG_VER_SECD.getBytes(), sysHeader);
		
		SysHeader.ENVR_INFO_SECD.setField(ENVR_INFO_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_ENCR_SECD.setField(TMSG_ENCR_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_COMPS_SECD.setField(TMSG_COMPS_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_LANG_SECD.setField(TMSG_LANG_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD.getBytes(), sysHeader);
		
		SysHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT.getBytes(), sysHeader);
		
		SysHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO.getBytes(), sysHeader);
		
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO.getBytes(), sysHeader);
		
		SysHeader.OGTRAN_GUID.setField(OGTRAN_GUID.getBytes(), sysHeader);
		
		
		/* 요청시스템정보 */
		SysHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR.getBytes(), sysHeader);
		
		SysHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR.getBytes(), sysHeader);
		
		SysHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD.getBytes(), sysHeader);
		
		SysHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT.getBytes(), sysHeader);
		
		SysHeader.FIRST_UUID.setField(FIRST_UUID.getBytes(), sysHeader);
		
		
		/* 표준전문 송신정보 */
		SysHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD.getBytes(), sysHeader);
		
		SysHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO.getBytes(), sysHeader);
		
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT.getBytes(), sysHeader);
		
		/* 표준전문 라우팅정보 */
		SysHeader.INFC_ID.setField(INFC_ID.getBytes(), sysHeader);
		
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_APP_ID.setField(TMSG_APP_ID.getBytes(), sysHeader);
		
		SysHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID.getBytes(), sysHeader);
		
		
		/* 표준전문 응답전문 */
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT.getBytes(), sysHeader);
		
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD.getBytes(), sysHeader);
		
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD.getBytes(), sysHeader);
		
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID.getBytes(), sysHeader);
		
		SysHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID.getBytes(), sysHeader);
		
		
		/* 연결정보 */
		SysHeader.LINK_NODE_NO.setField(LINK_NODE_NO.getBytes(), sysHeader);
		
		SysHeader.CLNT_IPADR.setField(CLNT_IPADR.getBytes(), sysHeader);
		
		SysHeader.LINK_SESS_ID.setField(LINK_SESS_ID.getBytes(), sysHeader);
		
		SysHeader.WB_SOCKT_ID.setField(WB_SOCKT_ID.getBytes(), sysHeader);
		
		SysHeader.SSO_TOKEN_ID.setField(SSO_TOKEN_ID.getBytes(), sysHeader);
		
		SysHeader.INSTT_TMSG_MNG_NO.setField(INSTT_TMSG_MNG_NO.getBytes(), sysHeader);
		
		SysHeader.WRK_SECD_S4.setField(WRK_SECD_S4.getBytes(), sysHeader);
		
		SysHeader.PUB_KEY.setField(PUB_KEY.getBytes(), sysHeader);
		
		SysHeader.DLNG_ITTP_CODE_S4.setField(DLNG_ITTP_CODE_S4.getBytes(), sysHeader);
		
		SysHeader.AP_CLIENT_ID.setField(AP_CLIENT_ID.getBytes(), sysHeader);		
	}
	
	
	public static void setResponseHeader( byte[] sysHeader
			, int len // 전체전문길이8byte, JSON생략
			, int tmsg_prg_no // 전문진행번호, 누산결과 입력
			, String trnsc_prcs_secd // // N:Normal, R:Rollback
			, String tmsg_prcs_rslt_secd // 0:정상, 1:시스템오류, 2:Timeout
			, String err_ocrn_link_sys_secd  // "오류발생연계시스템구분코드", "응답 전무의 오류 발생 시 시스템 코드"
			, String tmsg_err_msg_id // "전문오류메시지ID", "최초 오류 발생시 대표 오류 메시지코드를 설정";
			) {
		
		if( len < SysHeader.SYS_HEADER_LENGTH )
			len = SysHeader.SYS_HEADER_LENGTH;
		
		int msg_len = len;
		
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		/* 전문전체길이 */
		String ALL_TMSG_LNTH = String.format("%08d", msg_len);
		
		/* 표준전문 기본정보 */		
		String TMSG_PRG_NO = String.format("%03d", tmsg_prg_no);
				
		/* 요청시스템정보 */
		
		/* 표준전문 송신정보 */
		String DMND_RSPNS_SECD = "R"; // S:요청, R:응답
		
		/* 표준전문 라우팅정보 */
		String TRNSC_PRCS_SECD = trnsc_prcs_secd; // N:Normal, R:Rollback
		
		/* 표준전문 응답전문 */
		String RSPNS_TMSG_OCRN_DT = sdf.format(date);
		String TMSG_PRCS_RSLT_SECD = tmsg_prcs_rslt_secd; // 0:정상, 1:시스템오류, 2:Timeout
		String ERR_OCRN_LINK_SYS_SECD = err_ocrn_link_sys_secd; // "오류발생연계시스템구분코드", "응답 전무의 오류 발생 시 시스템 코드"
		String TMSG_ERR_MSG_ID = tmsg_err_msg_id; // "전문오류메시지ID", "최초 오류 발생시 대표 오류 메시지코드를 설정";
		
		/* 연결정보 */
						
		//String TMSG_CRT_SNO
		/* 전문전체길이 */
		SysHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), sysHeader);
		
		
		/* 표준전문 기본정보 */	
		SysHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO.getBytes(), sysHeader);
		
		
		/* 요청시스템정보 */
		
		
		/* 표준전문 송신정보 */
		SysHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD.getBytes(), sysHeader);
		
		/* 표준전문 라우팅정보 */
		SysHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD.getBytes(), sysHeader);
		
		
		/* 표준전문 응답전문 */
		SysHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT.getBytes(), sysHeader);
		SysHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD.getBytes(), sysHeader);
		SysHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD.getBytes(), sysHeader);
		SysHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID.getBytes(), sysHeader);
		
				
		/* 연결정보 */
		
	}

}
