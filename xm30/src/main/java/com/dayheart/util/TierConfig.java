package com.dayheart.util;

import java.lang.reflect.Field;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TierConfig {
	
	@Value("${MCI.PROTOCOL}")
	private String mciProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${MCI.HOST}")
	private String mciHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${MCI.PORT}")
	private int mciPort;
	
	@Value("${MCI.URI}")
	private String mciUri;
	private String[] mciUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${MCI.OUT}")
	private String mciOut;
	
	@Value("${MCI.EGRESS}")
	private String mciEgress;

	
	@Value("${ESB.PROTOCOL}")
	private String esbProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${ESB.HOST}")
	private String esbHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${ESB.PORT}")
	private int esbPort;
	
	//@Value("#{tier['ESB.URI']}")
	@Value("${ESB.URI}")
	private String esbUri;
	private String[] esbUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${ESB.OUT}")
	private String esbOut;
	
	@Value("${ESB.EGRESS}")
	private String esbEgress;
	
	
	@Value("${COR.PROTOCOL}")
	private String corProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${COR.HOST}")
	private String corHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${COR.PORT}")
	private int corPort;
	
	@Value("${COR.URI}")
	private String corUri;
	private String[] corUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${COR.OUT}")
	private String corOut;
	
	@Value("${COR.EGRESS}")
	private String corEgress;

	
	@Value("${EAI.PROTOCOL}")
	private String eaiProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${EAI.HOST}")
	private String eaiHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${EAI.PORT}")
	private int eaiPort;
	
	@Value("${EAI.URI}")
	private String eaiUri;
	private String[] eaiUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${EAI.OUT}")
	private String eaiOut;
	
	@Value("${EAI.EGRESS}")
	private String eaiEgress;
	
	
	@Value("${FEP.PROTOCOL}")
	private String fepProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${FEP.HOST}")
	private String fepHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${FEP.PORT}")
	private int fepPort;
	
	@Value("${FEP.URI}")
	private String fepUri;
	private String[] fepUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${FEP.OUT}")
	private String fepOut;
	
	@Value("${FEP.EGRESS}")
	private String fepEgress;
	
	@Value("${API.PROTOCOL}")
	private String apiProtocol;
	
	//@Value("#{tier['COR.HOST']}")
	@Value("${API.HOST}")
	private String apiHost;
	
	//@Value("#{tier['COR.PORT']}")
	@Value("${API.PORT}")
	private int apiPort;
	
	@Value("${API.URI}")
	private String apiUri;
	private String[] apiUris = null;
	
	//@Value("#{tier['COR.OUT']}")
	@Value("${API.OUT}")
	private String apiOut;
	
	@Value("${API.EGRESS}")
	private String apiEgress;
	
	
	private Properties props = System.getProperties();
	
	private static TierConfig instance;
	
	public static TierConfig getInstance() {
		return instance;
	}
	
	public TierConfig() {
		
		instance = this;
		// TODO Auto-generated constructor stub
		
		//Thread.dumpStack();
		/*
		Properties props = System.getProperties();
		
		String mciI = props.getProperty("MCI.PROTOCOL", this.mciProtocol);
		if(mciI!=null) {
			mciProtocol = mciI;
		}
		
		String mciH = props.getProperty("MCI.HOST");
		if(mciH!=null) {
			mciHost = mciH;
		} 
		
		String mciP = props.getProperty("MCI.PORT");
		if(mciP!=null) {
			try {
				mciPort = Integer.parseInt(mciP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String mciU = props.getProperty("MCI.URI");
		if(mciU!=null) {
			mciUri = mciU;
		}
				
		String mciO = props.getProperty("MCI.OUT");
		if(mciO!=null) {
			mciOut = mciO;
		}
		
		String mciE = props.getProperty("MCI.EGRESS");
		if(mciE!=null) {
			mciEgress = mciE;
		}
		
		// ESB
		String esbI = props.getProperty("ESB.PROTOCOL");
		if(esbI!=null) {
			esbProtocol = esbI;
		}
		
		String esbH = props.getProperty("ESB.HOST");
		if(esbH!=null) {
			esbHost = esbH;
		} 
		
		String esbP = props.getProperty("ESB.PORT");
		if(esbP!=null) {
			try {
				esbPort = Integer.parseInt(esbP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String esbU = props.getProperty("ESB.URI");
		if(esbU!=null) {
			esbUri = esbU;
		}
				
		String esbO = props.getProperty("ESB.OUT");
		if(esbO!=null) {
			esbOut = esbO;
		}
		
		String esbE = props.getProperty("ESB.EGRESS");
		if(esbE!=null) {
			esbEgress = esbE;
		}
		
		// COR
		String corI = props.getProperty("COR.PROTOCOL");
		if(corI!=null) {
			corProtocol = corI;
		}
		
		String corH = props.getProperty("COR.HOST");
		if(corH!=null) {
			corHost = corH;
		} 
		
		String corP = props.getProperty("COR.PORT");
		if(corP!=null) {
			try {
				corPort = Integer.parseInt(corP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String corU = props.getProperty("COR.URI");
		if(corU!=null) {
			corUri = corU;
		}
				
		String corO = props.getProperty("COR.OUT");
		if(corO!=null) {
			corOut = corO;
		}
		
		String corE = props.getProperty("COR.EGRESS");
		if(corE!=null) {
			corEgress = corE;
		}
		
		// EAI
		String eaiI = props.getProperty("EAI.PROTOCOL");
		if(eaiI!=null) {
			eaiProtocol = eaiI;
		}
		
		String eaiH = props.getProperty("EAI.HOST");
		if(eaiH!=null) {
			eaiHost = eaiH;
		} 
		
		String eaiP = props.getProperty("EAI.PORT");
		if(eaiP!=null) {
			try {
				eaiPort = Integer.parseInt(eaiP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String eaiU = props.getProperty("EAI.URI");
		if(eaiU!=null) {
			eaiUri = eaiU;
		}
				
		String eaiO = props.getProperty("EAI.OUT");
		if(eaiO!=null) {
			eaiOut = eaiO;
		}
		
		String eaiE = props.getProperty("EAI.EGRESS");
		if(eaiE!=null) {
			eaiEgress = eaiE;
		}
		
		// FEP
		String fepI = props.getProperty("FEP.PROTOCOL");
		if(fepI!=null) {
			fepProtocol = fepI;
		}
		
		String fepH = props.getProperty("FEP.HOST");
		if(fepH!=null) {
			fepHost = fepH;
		} 
		
		String fepP = props.getProperty("FEP.PORT");
		if(fepP!=null) {
			try {
				fepPort = Integer.parseInt(fepP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String fepU = props.getProperty("FEP.URI");
		if(fepU!=null) {
			fepUri = fepU;
		}
				
		String fepO = props.getProperty("FEP.OUT");
		if(fepO!=null) {
			fepOut = fepO;
		}
		
		String fepE = props.getProperty("FEP.EGRESS");
		if(fepE!=null) {
			fepEgress = fepE;
		}
		
		// API
		String apiI = props.getProperty("API.PROTOCOL");
		if(apiI!=null) {
			apiProtocol = apiI;
		}
		
		String apiH = props.getProperty("API.HOST");
		if(apiH!=null) {
			apiHost = apiH;
		} 
		
		String apiP = props.getProperty("API.PORT");
		if(apiP!=null) {
			try {
				apiPort = Integer.parseInt(apiP);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
			}
		}
		
		String apiU = props.getProperty("API.URI");
		if(apiU!=null) {
			apiUri = apiU;
		}
				
		String apiO = props.getProperty("API.OUT");
		if(apiO!=null) {
			apiOut = apiO;
		}
		
		String apiE = props.getProperty("API.EGRESS");
		if(apiE!=null) {
			apiEgress = apiE;
		}
		*/
		
	} // end of the Constructor

	public String getProtocol(String TIER) {
		String tiervalue = null;
		switch(TIER) {
		case "MCI" : 
			tiervalue = this.mciProtocol;
			break;
		case "ESB" :
			tiervalue = this.esbProtocol;
			break;
		case "COR" :
			tiervalue = this.corProtocol;
			break;
		case "EAI" :
			tiervalue = this.eaiProtocol;
			break;
		case "API" :
			tiervalue = this.apiProtocol;
			break;
		case "FEP" :
			tiervalue = this.fepProtocol;
			break;
		default :
			tiervalue = this.mciProtocol;
			break;
		
		}
		return props.getProperty(TIER.toUpperCase()+".POROTOCOL", tiervalue);
	}
	
	public String getMciProtocol() {
		return props.getProperty("MCI.PROTOCOL", this.mciProtocol);
	}
	
	public String getHost(String TIER) {
		String tiervalue = null;
		switch(TIER) {
		case "MCI" : 
			tiervalue = this.mciHost;
			break;
		case "ESB" :
			tiervalue = this.esbHost;
			break;
		case "COR" :
			tiervalue = this.corHost;
			break;
		case "EAI" :
			tiervalue = this.eaiHost;
			break;
		case "API" :
			tiervalue = this.apiHost;
			break;
		case "FEP" :
			tiervalue = this.fepHost;
			break;
		default :
			tiervalue = this.mciHost;
			break;
		
		}
		return props.getProperty(TIER.toUpperCase()+".HOST", tiervalue);
	}
	public String getMciHost() {
		return props.getProperty("MCI.HOST", this.mciHost);
		
	}

	public int getPort(String TIER) {
		int tiervalue = 0;
		switch(TIER) {
		case "MCI" : 
			tiervalue = this.mciPort;
			break;
		case "ESB" :
			tiervalue = this.esbPort;
			break;
		case "COR" :
			tiervalue = this.corPort;
			break;
		case "EAI" :
			tiervalue = this.eaiPort;
			break;
		case "API" :
			tiervalue = this.apiPort;
			break;
		case "FEP" :
			tiervalue = this.fepPort;
			break;
		default :
			tiervalue = this.mciPort;
			break;
		
		}
		String port = props.getProperty(TIER.toUpperCase()+".PORT");
		
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return tiervalue;
			}
		} 
		return tiervalue;
	}
	public int getMciPort() {
		String port = props.getProperty("MCI.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.mciPort;
			}
		}
		return this.mciPort;
	}

	// 2024.03.05
	public String getUri(String TIER) {
		String tiervalue = null;
		switch(TIER) {
		case "MCI" : 
			tiervalue = this.mciUri;
			break;
		case "ESB" :
			tiervalue = this.esbUri;
			break;
		case "COR" :
			tiervalue = this.corUri;
			break;
		case "EAI" :
			tiervalue = this.eaiUri;
			break;
		case "API" :
			tiervalue = this.apiUri;
			break;
		case "FEP" :
			tiervalue = this.fepUri;
			break;
		default :
			tiervalue = this.mciUri;
			break;
		
		}
		return props.getProperty(TIER.toUpperCase()+".URI", tiervalue);
	}
	
	public String getMciUri() {
		return props.getProperty("MCI.URI", this.mciUri);
	}
	
	
	public String[] getUris(String tier) {
		
		tier = tier.toLowerCase();
		
		try {
			Field urisField = this.getClass().getDeclaredField(tier + "Uris");
			
			urisField.setAccessible(true);
			Object uris  = urisField.get(this);
			
			Field uriField = this.getClass().getDeclaredField(tier + "Uri");
			uriField.setAccessible(true);
			Object uri = uriField.get(this);
			
			if(uris!=null) {
				return (String[])uris;
			} else if(uri!=null) {
				String s = (String)uri;
				if(s.indexOf(",")>-1) {
					urisField.set(this, s.split(","));
					return (String[])urisField.get(this);
				}
				urisField.set(this, new String[] { s });
				return (String[])urisField.get(this);
			}
			
		} catch (NoSuchFieldException e) {
			System.out.println(e.toString());
		} catch (IllegalAccessException illegalEx) {
			System.out.println(illegalEx.toString());
		}
		
		return null;
	}
	
	public String[] getMciUris() {
		if(mciUris!=null) {
			return this.mciUris;
		} else if(mciUri!=null) {
			if(mciUri.indexOf(",")>-1) {
				this.mciUris = mciUri.split(",");
				return this.mciUris;
			}
			this.mciUris = new String[] { this.mciUri };
			return this.mciUris;
		}
		return null;
	}

	public String getOut(String tier) {
		
		tier = tier.toLowerCase();
		
		try {
			Field field = this.getClass().getDeclaredField(tier + "Out");
			
			field.setAccessible(true);
			Object out  = field.get(this);
			
			return props.getProperty(tier.toUpperCase()+".OUT", (String)out);
			
		} catch (NoSuchFieldException e) {
			System.out.println(e.toString());
		} catch (IllegalAccessException illegalEx) {
			System.out.println(illegalEx.toString());
		}
		
		return null;
	}
	public String getMciOut() {
		return props.getProperty("MCI.OUT", this.mciOut);
	}

	public String getEgress(String tier) {
		
		tier = tier.toLowerCase();
		
		try {
			Field field = this.getClass().getDeclaredField(tier + "Egress");
			
			field.setAccessible(true);
			Object out  = field.get(this);
			
			return props.getProperty(tier.toUpperCase()+".EGRESS", (String)out);
			
		} catch (NoSuchFieldException e) {
			System.out.println(e.toString());
		} catch (IllegalAccessException illegalEx) {
			System.out.println(illegalEx.toString());
		}
		
		return null;
	}

	public String getMciEgress() {
		return props.getProperty("MCI.EGRESS", this.mciEgress);
	}

	public String getEsbProtocol() {
		return props.getProperty("ESB.PROTOCOL", this.esbProtocol);
	}

	public String getEsbHost() {
		return props.getProperty("ESB.HOST", this.esbHost);
	}

	public int getEsbPort() {
		String port = props.getProperty("ESB.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.esbPort;
			}
		}
		return this.esbPort;
	}

	public String getEsbUri() {
		return props.getProperty("ESB.URI", this.esbUri);
	}
	
	public String[] getEsbUris() {
		if(esbUris!=null) {
			return this.esbUris;
		} else if(esbUri!=null) {
			if(esbUri.indexOf(",")>-1) {
				this.esbUris = esbUri.split(",");
				return this.esbUris;
			}
			this.esbUris = new String[] { this.esbUri };
			return this.esbUris;
		}
		return null;
	}

	public String getEsbOut() {
		return props.getProperty("ESB.OUT", this.esbOut);
	}

	public String getEsbEgress() {
		return props.getProperty("ESB.EGRESS", this.esbEgress);
	}

	public String getCorProtocol() {
		return props.getProperty("COR.PROTOCOL", this.corProtocol);
	}

	public String getCorHost() {
		return props.getProperty("COR.HOST", this.corHost);
	}

	public int getCorPort() {
		String port = props.getProperty("COR.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.corPort;
			}
		}
		return this.corPort;
	}

	public String getCorUri() {
		return props.getProperty("COR.URI", this.corUri);
	}
	
	public String[] getCorUris() {
		if(corUris!=null) {
			return this.corUris;
		} else if(corUri!=null) {
			if(corUri.indexOf(",")>-1) {
				this.corUris = corUri.split(",");
				return this.corUris;
			}
			this.corUris = new String[] { this.corUri };
			return this.corUris;
		}
		return null;
	}

	public String getCorOut() {
		return props.getProperty("COR.OUT", this.corOut);
	}

	public String getCorEgress() {
		return props.getProperty("COR.EGRESS", this.corEgress);
	}

	public String getEaiProtocol() {
		return props.getProperty("EAI.PROTOCOL", this.eaiProtocol);
	}

	public String getEaiHost() {
		return props.getProperty("EAI.HOST", this.eaiHost);
	}

	public int getEaiPort() {
		String port = props.getProperty("EAI.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.eaiPort;
			}
		}
		return this.eaiPort;
	}

	public String getEaiUri() {
		return props.getProperty("EAI.URI", this.eaiUri);
	}
	
	public String[] getEaiUris() {
		if(eaiUris!=null) {
			return this.eaiUris;
		} else if(eaiUri!=null) {
			if(eaiUri.indexOf(",")>-1) {
				this.eaiUris = eaiUri.split(",");
				return this.eaiUris;
			}
			this.eaiUris = new String[] { this.eaiUri };
			return this.eaiUris;
		}
		return null;
	}

	public String getEaiOut() {
		return props.getProperty("EAI.OUT", this.eaiOut);
	}

	public String getEaiEgress() {
		return props.getProperty("EAI.EGRESS", this.eaiEgress);
	}

	public String getFepProtocol() {
		return props.getProperty("FEP.PROTOCOL", this.fepProtocol);
	}

	public String getFepHost() {
		return props.getProperty("FEP.HOST", this.fepHost);
	}

	public int getFepPort() {
		String port = props.getProperty("FEP.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.fepPort;
			}
		}
		return this.fepPort;
	}

	public String getFepUri() {
		return props.getProperty("FEP.URI", this.fepUri);
	}
	
	public String[] getFepUris() {
		if(fepUris!=null) {
			return this.fepUris;
		} else if(fepUri!=null) {
			if(fepUri.indexOf(",")>-1) {
				this.fepUris = fepUri.split(",");
				return this.fepUris;
			}
			this.fepUris = new String[] { this.fepUri };
			return this.fepUris;
		}
		return null;
	}

	public String getFepOut() {
		return props.getProperty("FEP.OUT", this.fepOut);
	}

	public String getFepEgress() {
		return props.getProperty("FEP.EGRESS", this.fepEgress);
	}

	public String getApiProtocol() {
		return props.getProperty("API.PROTOCOL", this.apiProtocol);
	}

	public String getApiHost() {
		return props.getProperty("API.HOST", this.apiHost);
	}

	public int getApiPort() {
		String port = props.getProperty("API.PORT");
		if(port!=null) {
			try {
				return Integer.parseInt(port);
			}catch (NumberFormatException nfe) {
				// TODO: handle exception
				System.out.println(nfe);
				return this.apiPort;
			}
		}
		return this.apiPort;
	}

	public String getApiUri() {
		return props.getProperty("API.URI", this.apiUri);
	}
	
	public String[] getApiUris() {
		if(apiUris!=null) {
			return this.apiUris;
		} else if(apiUri!=null) {
			if(apiUri.indexOf(",")>-1) {
				this.apiUris = apiUri.split(",");
				return this.apiUris;
			}
			this.apiUris = new String[] { this.apiUri };
			return this.apiUris;
		}
		return null;
	}

	public String getApiOut() {
		return props.getProperty("API.OUT", this.apiOut);
	}

	public String getApiEgress() {
		return props.getProperty("API.EGRESS", this.apiEgress);
	}

	
	
}



/*
java.lang.Exception: Stack trace
	at java.lang.Thread.dumpStack(Thread.java:1336)
	at com.dayheart.util.TierRepository.<init>(TierRepository.java:34)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:148)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:89)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateBean(AbstractAutowireCapableBeanFactory.java:1069)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1021)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:504)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:475)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:304)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:228)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:300)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:195)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:703)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:760)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:482)
	at org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(FrameworkServlet.java:658)
	at org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:624)
	at org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:672)
	at org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(FrameworkServlet.java:543)
	at org.springframework.web.servlet.FrameworkServlet.initServletBean(FrameworkServlet.java:484)
	at org.springframework.web.servlet.HttpServletBean.init(HttpServletBean.java:136)
	at javax.servlet.GenericServlet.init(GenericServlet.java:143)
	at org.apache.catalina.core.StandardWrapper.initServlet(StandardWrapper.java:1106)
	at org.apache.catalina.core.StandardWrapper.loadServlet(StandardWrapper.java:1063)
	at org.apache.catalina.core.StandardWrapper.allocate(StandardWrapper.java:747)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:116)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
	at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:670)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:926)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1790)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

*/