package com.dayheart.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadCpuTime {
	
	public static long cpuTime(Thread thread) {
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		
		if(mxBean.isCurrentThreadCpuTimeSupported()) {
			return mxBean.getThreadCpuTime(thread.getId());
		}
		
		return 0L;
	}

}
