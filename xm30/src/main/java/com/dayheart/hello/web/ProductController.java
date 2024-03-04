package com.dayheart.hello.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dayheart.util.TierConfig;
import com.dayheart.util.XLog;

@Controller
public class ProductController {
	
	@Autowired
	private TierConfig tier;
	
	@RequestMapping("/chl/products")
	public String homePage(HttpServletRequest request, Model model) {
		
		String contextPath = request.getContextPath();
		System.out.println(String.format("CONTEXT_PATH[%s]", contextPath));
		model.addAttribute("context_path", contextPath);
		
		XLog.stdout(String.format("ESB_URI [%s]", tier.getEsbUri()));
		
		return "hello_products";
	}
}
