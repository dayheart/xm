package kisb.sb.tmsg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import com.dayheart.tcp.TCPClient;
import com.dayheart.util.XLog;

// 추상클래스로~
// 
/**
 * 전문 중계 역할, 전문 형태 추가하여 확장할 경우 추상 클래스로 변경 후 각 전문 처리 클래스를 상속해서 만들기
 * @author dayheart
 *
 */
public class TelegramMessageUtil {
	
	private String mciProtocol;
	private String mciHost;
	private int mciPort;
	
	private String esbProtocol;
	private String esbHost;
	private int esbPort;
	
	private String eaiProtocol;
	private String eaiHost;
	private int eaiPort;
	
	private String corProtocol;
	private String corHost;
	private int corPort;
	
	private String fepProtocol;
	private String fepHost;
	private int fepPort;
	
	private String apiProtocol;
	private String apiHost;
	private int apiPort;
	
	private static TelegramMessageUtil instance;
	
	/**
	 *  synchronize 방식의 싱글톤
	 * @return
	 */
	public static synchronized TelegramMessageUtil getInstance() {
		if(instance == null) {
			instance = new TelegramMessageUtil();
		}
		return instance;
	}
	
	/**
	 * 싱글톤 생성자
	 */
	private TelegramMessageUtil() {
		loadProperties(0);
	}
	
	/**
	 * 홀더 방식을 이용한 싱글톤 생성
	 * @author dayheart
	 *
	 */
	private static class SingletonHolder {
		
		private static final TelegramMessageUtil instance = new TelegramMessageUtil();
	}
	
	/**
	 * 싱글톤 방식의 객체 생성
	 * @param fast 홀더 방식을 이용한 싱글톤 객체 반환
	 * @return 싱글토 객체 반환
	 */
	public static TelegramMessageUtil getInstance(boolean fast) {
		
		TelegramMessageUtil rt = null;
		
		String dbg = null;
		
		if(fast) {
			rt = SingletonHolder.instance;
			dbg = "#### HOLDER SINGLETON";
		} else {
			rt = getInstance();
			dbg = "#### SYNCHRONIZED SINGLETON";
		}
		
		XLog.stdout( dbg + " [" + rt + "]");
		
		return rt;
	}
	
	/**
	 * 프러퍼티 로딩
	 * @param i 0:현재 클래스의 클래스로더를 이용한 프러퍼티 파일 읽기
	 */
	private void loadProperties(int i) {
		
		switch(i) {
		
			case 0: 
				loadPropertiesByClassLoader();
				break;
			
			default:
				break;
		}
	}
	
	
	/**
	 * E2E 각 티어에 대한 전송 프로토콜 및 IP, port 설정값 읽기
	 */
	private void loadPropertiesByClassLoader() {
		String configFilePath = "config" + File.separator + "tier.properties";
		
		//XLog.stdout(configFilePath);
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		
		Properties prop = new Properties();
		try {
			prop.load(is);
			
			XLog.stdout(prop.toString());
			
			mciProtocol = prop.getProperty("MCI.PROTOCOL");
			mciHost = prop.getProperty("MCI.HOST");
			mciPort =  Integer.parseInt( prop.getProperty("MCI.PORT") );
			
			esbProtocol = prop.getProperty("ESB.PROTOCOL");
			esbHost = prop.getProperty("ESB.HOST");
			esbPort =  Integer.parseInt( prop.getProperty("ESB.PORT") );
			
			eaiProtocol = prop.getProperty("EAI.PROTOCOL");
			eaiHost = prop.getProperty("EAI.HOST");
			eaiPort =  Integer.parseInt( prop.getProperty("EAI.PORT") );
			
			corProtocol = prop.getProperty("COR.PROTOCOL");
			corHost = prop.getProperty("COR.HOST");
			corPort =  Integer.parseInt( prop.getProperty("COR.PORT") );
			
			fepProtocol = prop.getProperty("FEP.PROTOCOL");
			fepHost = prop.getProperty("FEP.HOST");
			fepPort =  Integer.parseInt( prop.getProperty("FEP.PORT") );
			
			apiProtocol = prop.getProperty("API.PROTOCOL");
			apiHost = prop.getProperty("API.HOST");
			apiPort =  Integer.parseInt( prop.getProperty("API.PORT") );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * E2E 전문 HTTP 프로토콜 요청
	 * @param url 요청 URL
	 * @param method HTTP 메소드
	 * @param req_tmsg HTTP 요청 바디
	 * @return HTTP 응답
	 */
	public byte[] transmit(String url, String method, byte[] req_tmsg) {
		
		byte[] response;
		
		response = TCPClient.executeBytesByApacheHttpClient(url, method, req_tmsg);
		// check null
		
		return response; 
	}

	
	/**
	 *  전송 메시지 작성
	 * @param src_header 표준헤더
	 * @param sysCd 송신연계시스템구분코드(현재 시스템)
	 * @param rspnsCd 요청응답구분코드, S:요청, R:응답
	 * @param syncCd 전문동기화구분코드, S:동기, A:비동기, M:메시지푸시, D:데이터푸시, D:더미, W:웹소켓정보
	 * @param src_body 전문 바디
	 * @return 전송할 메시지
	 */
	public byte[] setSendMessageInfo(byte[] src_header, String sysCd, String rspnsCd, String syncCd, byte[] src_body) {
		
		byte[] req_tmsg;
		
		SysHeader.increasePrgNo(src_header);
		
		if(src_body !=null) {
			req_tmsg = new byte[SysHeader.HEADER_LENGTH + src_body.length];
			System.arraycopy(src_header, 0, req_tmsg, 0, src_header.length);
			System.arraycopy(src_body, 0, req_tmsg, SysHeader.HEADER_LENGTH, src_body.length);
		} else {
			req_tmsg = new byte[SysHeader.HEADER_LENGTH];
			System.arraycopy(src_header, 0, req_tmsg, 0, src_header.length);
		}
		
		SysHeader.setTRMST(req_tmsg, sysCd, rspnsCd, syncCd);
		
		XLog.stdout("UPDATE_TMSG [" + new String(req_tmsg) + "], len:" + req_tmsg.length);
		return req_tmsg;
	}
	
	/**
	 * 응답 헤더 값 세팅
	 * @param rcv_tmsg 수신 전문
	 * @param rsltCd 전문처리결과 구분코드. 0:정상, 1:시스템오류, 2:Timeout 
	 * @param sysCd 오류발생연계시스템구분코드
	 * @param errMsgId 전문오류메시지ID
	 * @param errCmpoId 화면필수입력오류컴포넌트ID
	 */
	public void setRcvMessageInfo(byte[] rcv_tmsg, String rsltCd, String sysCd, String errMsgId, String errCmpoId) {
		SysHeader.setRSPNS(rcv_tmsg, rsltCd, sysCd, errMsgId, errCmpoId);		
	}
}
