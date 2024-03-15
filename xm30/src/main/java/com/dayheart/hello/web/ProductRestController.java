package com.dayheart.hello.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dayheart.hello.domain.Product;
import com.dayheart.hello.service.UsingJdbcTemplateService;
import com.dayheart.hello.service.impl.UsingJdbcTemplateOfficeService;
import com.dayheart.hello.service.impl.UsingJdbcTemplateProductService;
import com.dayheart.tcp.TCPClient;
import com.dayheart.util.TierConfig;
import com.dayheart.util.Utils;
import com.dayheart.util.XLog;

import kisb.sb.tmsg.SysHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
public class ProductRestController {

    @Autowired
    //private ProductDAO productRepository;
    private UsingJdbcTemplateProductService usingJdbcTemplateProductService;
    
    @Autowired
    private UsingJdbcTemplateOfficeService usingJdbcTemplateOfficeService;
    
    @Autowired
    private TierConfig tierConf;
    
	@RequestMapping("/api/products")
    public List<Map<String, ?>> findAll() {
    	return usingJdbcTemplateProductService.findAll();
    }
    
    @RequestMapping("/api/product")
    //public Product retrieveByProduct(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {
    //public ResponseEntity<Product> retrieveByProduct(@RequestHeader("mfr_id") String mfrId, @RequestHeader("product_id") String productId) {
    public List<Map<String, ?>> retrieveByProduct(@RequestHeader("mfr_id") String mfrId, @RequestHeader("product_id") String productId) {
    	
    	Map<String, Object> sysHeader = new HashMap<String, Object>();
    	String sysCd = "PRD"; //sysCd(3) PRD, OFC, SAL, ORD, CST
    	SysHeader.setGUID(sysHeader, sysCd, "P"); //  Prod, Dev, Test
		SysHeader.setDMND(sysHeader, sysCd); // ex) CHL=D19
		
		// SYNC | ASYNC 세팅, 이후 전문에서 지속... 최종 FEP 에서 사용
		String sync = "S";
		SysHeader.setTRMST(sysHeader, sysCd, "S", sync); // Send/Recv, Sync/Async
		
		//SysHeader.setINFC(sysHeader, String.format("%s-%s", mfrId, productId), "N", "", String.format("%s-%s", mfrId, productId)); // INFC_ID(part), SVC_ID(eng)
		SysHeader.setINFC(sysHeader, "OFFICES", "N", "", String.format("%s-%s", mfrId, productId)); // INFC_ID(part), SVC_ID(eng)
		
		//System.out.println( Thread.currentThread().getStackTrace()[1] + " : " + "[" + new String( SysHeader.toBytes(header))  + "]");
		String url = null;
		String responseStr = null;
		if(tierConf.getMciProtocol().equalsIgnoreCase("http") || tierConf.getMciProtocol().equalsIgnoreCase("https")) {
			url = tierConf.getMciProtocol() + "://" + tierConf.getMciHost() + ":" + tierConf.getMciPort();
			
			if(tierConf.getUris("MCI")!=null) {
				int i = Utils.getRandomNumber(0, (tierConf.getUris("MCI").length));
				String[] uris = tierConf.getUris("MCI");
				url += uris[i];
				
				String uri = uris[i];
				//XLog.stdout("MCI_URI: " + url);
				
				if(uri.endsWith("json")) {
					responseStr = TCPClient.executeJsonByApacheHttpClient(url, "POST", SysHeader.toJsonString(sysHeader));
				} else {
					responseStr = new String( TCPClient.executeBytesByApacheHttpClient(url, "POST", SysHeader.toBytes(sysHeader)));
				}
				
			} else {
				url += tierConf.getMciUri();
				
				responseStr = new String( TCPClient.executeBytesByApacheHttpClient(url, "POST", SysHeader.toBytes(sysHeader)));
			}
		}
		XLog.stdout(String.format("%s-%s", mfrId, productId));
		//XLog.stdout(usingJdbcTemplateOfficeService.retrieveByProduct(mfrId, productId).toString());
    	
    	return usingJdbcTemplateProductService.retrieveByProduct(mfrId, productId);
    }
    
    
    @RequestMapping("/api/wholeProducts")
    public List<Map<String, ?>> wholeProducts() {
    	return usingJdbcTemplateProductService.findAll();
    }
    
}