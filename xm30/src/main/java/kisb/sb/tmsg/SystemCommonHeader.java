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

//import com.exem.ext.XM_SYS_HEADER;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author dayheart
 * 
 * KISB SYSTEM HEADER tot. length : 1057
 *
 */
public enum SystemCommonHeader {
	
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
	OGTRAN_GUID                (55, 40, "원거래GUID", ""), // org 40 exclude tmsg_prg_no
	
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
	private int offset;
	private int len;
	private String ko;
	private String desc;
	
	private SystemCommonHeader(int offset, int len, String ko, String desc) {
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
		
		// System.out.println("HEADER_FOUND[" + this.name() + "]"); 
		// System.out.println("HEADER_JSON [" + json + "]"); 
		
		
		int offset = -1;
		String jsonToken = "\":\""; // change the CASE RSPNS_TMSG_OCRN_DT":null
		int tokenLength = jsonToken.length();
		if( json.indexOf(this.name())>-1 ) {
			offset = json.indexOf(this.name()) + this.name().length() + tokenLength;
			
			//System.out.println("HEADER_OFFSET [" + this.name() +"], offset:" + offset);
			
			if( json.indexOf("null", offset-1) != (offset-1) ) {
				value = json.substring(offset, json.indexOf("\"", offset));
			}
		}
		
		return value;
	}
	
	
	public static String getPrintJsonString(String data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder("\n");
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
			int diff = max - field.name().length();
			sb.append(field.name());
			for(int i=0; i<diff;i++) {
				sb.append(" ");
			}
			sb.append("[" + field.getFieldJson(data) + "], len:" + field.getFieldJson(data).length() + "\n");
		}
		
		return sb.toString();
		
	}
	
	
	public static String getPrintString(byte[] data) {
		
		int max = 0;
		StringBuilder sb = new StringBuilder();
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
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
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
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
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
			int nameLen = field.name().length();
			if(nameLen > max)
				max = nameLen;
		}
		
		for(SystemCommonHeader field : SystemCommonHeader.values()) {
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
	
	/**
	 * 거래 전문 출력 테스트
	 * @param args
	 */
	public static void t_main(String[] args) {
		
		int msg_len = 2048;
		byte[] byte_telegram = new byte[msg_len];
		Map<String, Object> map_telegram = new HashMap<String, Object>();
		
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
		SystemCommonHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH.getBytes(), byte_telegram);
		SystemCommonHeader.ALL_TMSG_LNTH.setField(ALL_TMSG_LNTH, map_telegram);
		
		
		/* 표준전문 기본정보 */	
		SystemCommonHeader.TMSG_VER_SECD.setField(TMSG_VER_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_VER_SECD.setField(TMSG_VER_SECD, map_telegram);
		
		SystemCommonHeader.ENVR_INFO_SECD.setField(ENVR_INFO_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.ENVR_INFO_SECD.setField(ENVR_INFO_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_ENCR_SECD.setField(TMSG_ENCR_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_ENCR_SECD.setField(TMSG_ENCR_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_COMPS_SECD.setField(TMSG_COMPS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_COMPS_SECD.setField(TMSG_COMPS_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_LANG_SECD.setField(TMSG_LANG_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_LANG_SECD.setField(TMSG_LANG_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_CRT_LINK_SYS_SECD.setField(TMSG_CRT_LINK_SYS_SECD, map_telegram);
		
		SystemCommonHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT.getBytes(), byte_telegram);
		SystemCommonHeader.FIRST_TMSG_CRT_DT.setField(FIRST_TMSG_CRT_DT, map_telegram);
		
		SystemCommonHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_CRT_ENVR_INFO_SECD.setField(TMSG_CRT_ENVR_INFO_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_CRT_SNO.setField(TMSG_CRT_SNO, map_telegram);
		
		SystemCommonHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_PRG_NO.setField(TMSG_PRG_NO, map_telegram);
		
		SystemCommonHeader.OGTRAN_GUID.setField(OGTRAN_GUID.getBytes(), byte_telegram);
		SystemCommonHeader.OGTRAN_GUID.setField(OGTRAN_GUID, map_telegram);
		
		
		/* 요청시스템정보 */
		SystemCommonHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_DMND_IPADR.setField(TMSG_DMND_IPADR, map_telegram);
		
		SystemCommonHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_DMND_MADR.setField(TMSG_DMND_MADR, map_telegram);
		
		SystemCommonHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.FIRST_DMND_LINK_SYS_SECD.setField(FIRST_DMND_LINK_SYS_SECD, map_telegram);
		
		SystemCommonHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT.getBytes(), byte_telegram);
		SystemCommonHeader.FIRST_TMSG_DMND_DT.setField(FIRST_TMSG_DMND_DT, map_telegram);
		
		SystemCommonHeader.FIRST_UUID.setField(FIRST_UUID.getBytes(), byte_telegram);
		SystemCommonHeader.FIRST_UUID.setField(FIRST_UUID, map_telegram);
		
		
		/* 표준전문 송신정보 */
		SystemCommonHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TRSMT_LINK_SYS_SECD.setField(TRSMT_LINK_SYS_SECD, map_telegram);
		
		SystemCommonHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO.getBytes(), byte_telegram);
		SystemCommonHeader.TRSMT_NODE_NO.setField(TRSMT_NODE_NO, map_telegram);
		
		SystemCommonHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.DMND_RSPNS_SECD.setField(DMND_RSPNS_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_SYNCZ_SECD.setField(TMSG_SYNCZ_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_TRSMT_DT.setField(TMSG_TRSMT_DT, map_telegram);
		
		/* 표준전문 라우팅정보 */
		SystemCommonHeader.INFC_ID.setField(INFC_ID.getBytes(), byte_telegram);
		SystemCommonHeader.INFC_ID.setField(INFC_ID, map_telegram);
		
		SystemCommonHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TRNSC_PRCS_SECD.setField(TRNSC_PRCS_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_APP_ID.setField(TMSG_APP_ID.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_APP_ID.setField(TMSG_APP_ID, map_telegram);
		
		SystemCommonHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_SVC_ID.setField(TMSG_SVC_ID, map_telegram);
		
		
		/* 표준전문 응답전문 */
		SystemCommonHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT.getBytes(), byte_telegram);
		SystemCommonHeader.RSPNS_TMSG_OCRN_DT.setField(RSPNS_TMSG_OCRN_DT, map_telegram);
		
		SystemCommonHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_PRCS_RSLT_SECD.setField(TMSG_PRCS_RSLT_SECD, map_telegram);
		
		SystemCommonHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD.getBytes(), byte_telegram);
		SystemCommonHeader.ERR_OCRN_LINK_SYS_SECD.setField(ERR_OCRN_LINK_SYS_SECD, map_telegram);
		
		SystemCommonHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID.getBytes(), byte_telegram);
		SystemCommonHeader.TMSG_ERR_MSG_ID.setField(TMSG_ERR_MSG_ID, map_telegram);
		
		SystemCommonHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID.getBytes(), byte_telegram);
		SystemCommonHeader.SCRN_ESNTL_INPT_ERR_CMPO_ID.setField(SCRN_ESNTL_INPT_ERR_CMPO_ID, map_telegram);
		
		
		/* 연결정보 */
		SystemCommonHeader.LINK_NODE_NO.setField(LINK_NODE_NO.getBytes(), byte_telegram);
		SystemCommonHeader.LINK_NODE_NO.setField(LINK_NODE_NO, map_telegram);
		
		SystemCommonHeader.CLNT_IPADR.setField(CLNT_IPADR.getBytes(), byte_telegram);
		SystemCommonHeader.CLNT_IPADR.setField(CLNT_IPADR, map_telegram);
		
		SystemCommonHeader.LINK_SESS_ID.setField(LINK_SESS_ID.getBytes(), byte_telegram);
		SystemCommonHeader.LINK_SESS_ID.setField(LINK_SESS_ID, map_telegram);
		
		SystemCommonHeader.WB_SOCKT_ID.setField(WB_SOCKT_ID.getBytes(), byte_telegram);
		SystemCommonHeader.WB_SOCKT_ID.setField(WB_SOCKT_ID, map_telegram);
		
		SystemCommonHeader.SSO_TOKEN_ID.setField(SSO_TOKEN_ID.getBytes(), byte_telegram);
		SystemCommonHeader.SSO_TOKEN_ID.setField(SSO_TOKEN_ID, map_telegram);
		
		SystemCommonHeader.INSTT_TMSG_MNG_NO.setField(INSTT_TMSG_MNG_NO.getBytes(), byte_telegram);
		SystemCommonHeader.INSTT_TMSG_MNG_NO.setField(INSTT_TMSG_MNG_NO, map_telegram);
		
		SystemCommonHeader.WRK_SECD_S4.setField(WRK_SECD_S4.getBytes(), byte_telegram);
		SystemCommonHeader.WRK_SECD_S4.setField(WRK_SECD_S4, map_telegram);
		
		SystemCommonHeader.PUB_KEY.setField(PUB_KEY.getBytes(), byte_telegram);
		SystemCommonHeader.PUB_KEY.setField(PUB_KEY, map_telegram);
		
		SystemCommonHeader.DLNG_ITTP_CODE_S4.setField(DLNG_ITTP_CODE_S4.getBytes(), byte_telegram);
		SystemCommonHeader.DLNG_ITTP_CODE_S4.setField(DLNG_ITTP_CODE_S4, map_telegram);
		
		SystemCommonHeader.AP_CLIENT_ID.setField(AP_CLIENT_ID.getBytes(), byte_telegram);
		SystemCommonHeader.AP_CLIENT_ID.setField(AP_CLIENT_ID, map_telegram);
		
		String string_telegram = new String(byte_telegram);
		
		ObjectMapper objMapper = new ObjectMapper();
		String jsonString_telegram = "";
		try {
			//String json = objMapper.writeValueAsString(map);
			jsonString_telegram = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map_telegram);
		} catch (JsonProcessingException jsonProcEx) {
			jsonProcEx.printStackTrace();
		}
		
		
		/////////////////////////////////////////////////////////////////////////////////
		///// 테스트 전문 출력 
		/////////////////////////////////////////////////////////////////////////////////
		System.out.println("#### Orignal telegram #####");
		System.out.println("[" + string_telegram + "], len: " + string_telegram.length());
		System.out.println("#### JSON telegram #####");
		System.out.println("[" + jsonString_telegram + "], len: " + string_telegram.length());
		
		
		/////////////////////////////////////////////////////////////
		///// 테스트 전문 출력 함수 사용 법 
		////////////////////////////////////////////////////////////
		/*
		System.out.println("##### byte[] parsing #####");
		System.out.println(XM_SYS_HEADER.getPrintString(byte_telegram));
		System.out.println("##### String parsing #####");
		System.out.println(XM_SYS_HEADER.getPrintString(string_telegram));
		System.out.println("##### HashMap parsing #####");
		System.out.println(XM_SYS_HEADER.getPrintString(map_telegram));
		*/
		System.out.println("##### json String parsing #####");
		System.out.println(SystemCommonHeader.getPrintJsonString(jsonString_telegram));
		
		
		///////////////////////////////////////////////////////////
		////// 전문 각 타입별 필드값 추출 예시
		///////////////////////////////////////////////////////////
		/* byte[] 형태 전문에서 개별 필드값 추출 예 */
		//System.out.printf("OGTRAN_GUID from byte[]  [%s]\n", XM_SYS_HEADER.OGTRAN_GUID.getField(byte_telegram));
		/* String 형태 전문에서 개별 필드값 추출 예 */
		//System.out.printf("OGTRAN_GUID from String  [%s]\n",  XM_SYS_HEADER.OGTRAN_GUID.getFieldSubstring(string_telegram));
		/* HashMap 형태 전문에서 개별 필드값 추출 예 */
		//System.out.printf("OGTRAN_GUID from HashMap [%s]\n",  XM_SYS_HEADER.OGTRAN_GUID.getField(map_telegram));
		/* JSON Strig 형태 전문에서 개별 필드값 추출 예 */
		//System.out.printf("OGTRAN_GUID from JSONStr [%s]\n",  XM_SYS_HEADER.OGTRAN_GUID.getFieldJson(jsonString_telegram));
		
		//System.out.printf("OGTRAN_GUID from byte[]  [%s]\n", XM_SYS_HEADER.OGTRAN_GUID.getDMethod(new Object() {}));
		
	} // end of main

}
