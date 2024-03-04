package com.dayheart.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class XLog {
	
	public static boolean isDebug = true;
	
	public static void stdout(String msg) {
		
		if(isDebug) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			StringBuilder sb = new StringBuilder( sdf.format(new Date()) + " " );
			sb.append(Thread.currentThread().getName() + " ");
			sb.append(Thread.currentThread().getStackTrace()[2] + " " );
			sb.append(msg);
			
			System.out.println(sb.toString());		
		}
	}

}
