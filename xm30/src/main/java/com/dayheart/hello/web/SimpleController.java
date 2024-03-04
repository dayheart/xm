package com.dayheart.hello.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dayheart.hello.persistence.repo.Prop;


@Controller
public class SimpleController {
	
	@RequestMapping("/home") 
	public String home(Model model) {
		return "home";
	}
	
	@RequestMapping("/props")
	public String propsPage(Model model) {
		
		Properties properties = System.getProperties();
		
		Enumeration<?> names = properties.propertyNames();
		
		ArrayList<Prop> props = new ArrayList<Prop>();
		
		for(;names.hasMoreElements();) {
			String name = (String)names.nextElement();
			String value = properties.getProperty(name, "");
			
			Prop prop = new Prop(name, value);
			props.add(prop);
		}
		model.addAttribute("props", props);
		
		return "props";
	}
	
	@RequestMapping("/jarcheck")
	public String jarcheckPage(@RequestParam(value = "reqName", defaultValue = "javax.servlet.http.HttpServlet") String reqName, Model model) {
		reqName = reqName.trim();
		
		model.addAttribute("reqName", reqName);
		
		reqName = reqName.replace('.', '/').trim();
		reqName = "/" + reqName + ".class";
		
		java.net.URL classUrl = this.getClass().getResource(reqName);
		
		String result = (classUrl==null)?String.format("%s is not found", reqName):String.format("%s: [%s]", reqName, classUrl.getFile());
		
		model.addAttribute("result", result);
		
		return "jarcheck";
	}

}
