package com.inzent.igate.connector;

public class Connector {
	
	private final String id;
	private final String systemId;
	private final String adapterId;
	
	private Status status;
	public enum Status {
		Down, stoping, Error, Fail, Warn, Normal;
	}
	
	private final boolean sessionWaitWarning;
	private final boolean sessionCountWarning;
	private final boolean sessionInuseWarning;
	private final boolean threadCountWarning;
	private final boolean threadInuseWarning;
	private final boolean ideWarning;
	private final int sessionWait;
	private final int sessionWaitMax;
	private final int sessionCount;
	private final int sessionInuse;
	private final int sessionMax;
	private final int threadCount;
	private final int threadInuse;
	private final int threadMax;
	
	public Connector(String id, String systemId, String adapterId, Status status, boolean sessionWaitWarning,
			boolean sessionCountWarning, boolean sessionInuseWarning, boolean threadCountWarning,
			boolean threadInuseWarning, boolean ideWarning, int sessionWait, int sessionWaitMax, int sessionCount,
			int sessionInuse, int sessionMax, int threadCount, int threadInuse, int threadMax) {
		super();
		this.id = id;
		this.systemId = systemId;
		this.adapterId = adapterId;
		this.status = status;
		this.sessionWaitWarning = sessionWaitWarning;
		this.sessionCountWarning = sessionCountWarning;
		this.sessionInuseWarning = sessionInuseWarning;
		this.threadCountWarning = threadCountWarning;
		this.threadInuseWarning = threadInuseWarning;
		this.ideWarning = ideWarning;
		this.sessionWait = sessionWait;
		this.sessionWaitMax = sessionWaitMax;
		this.sessionCount = sessionCount;
		this.sessionInuse = sessionInuse;
		this.sessionMax = sessionMax;
		this.threadCount = threadCount;
		this.threadInuse = threadInuse;
		this.threadMax = threadMax;
	}

	public String getId() {
		return id;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getAdapterId() {
		return adapterId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isSessionWaitWarning() {
		return sessionWaitWarning;
	}

	public boolean isSessionCountWarning() {
		return sessionCountWarning;
	}

	public boolean isSessionInuseWarning() {
		return sessionInuseWarning;
	}

	public boolean isThreadCountWarning() {
		return threadCountWarning;
	}

	public boolean isThreadInuseWarning() {
		return threadInuseWarning;
	}

	public boolean isIdeWarning() {
		return ideWarning;
	}

	public int getSessionWait() {
		return sessionWait;
	}

	public int getSessionWaitMax() {
		return sessionWaitMax;
	}

	public int getSessionCount() {
		return sessionCount;
	}

	public int getSessionInuse() {
		return sessionInuse;
	}

	public int getSessionMax() {
		return sessionMax;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public int getThreadInuse() {
		return threadInuse;
	}

	public int getThreadMax() {
		return threadMax;
	}
	
	

}
