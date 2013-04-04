package mapreduce.node.configuration;

public class Configuration {
	private Boolean isMaster;
	private String masterHostName;
	private Integer masterPort;
	private Integer localPort;
	private String localHostName;
	private Integer capability;
	private Integer messageMaxPoolSize;
	public Boolean getIsMaster() {
		return isMaster;
	}
	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}
	public String getMasterHostName() {
		return masterHostName;
	}
	public void setMasterHostName(String masterHostName) {
		this.masterHostName = masterHostName;
	}
	public Integer getMasterPort() {
		return masterPort;
	}
	public void setMasterPort(Integer masterPort) {
		this.masterPort = masterPort;
	}
	public Integer getLocalPort() {
		return localPort;
	}
	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}
	public String getLocalHostName() {
		return localHostName;
	}
	public void setLocalHostName(String localHostName) {
		this.localHostName = localHostName;
	}
	public Integer getCapability() {
		return capability;
	}
	public void setCapability(Integer capability) {
		this.capability = capability;
	}
	public Integer getMessageMaxPoolSize() {
		return messageMaxPoolSize;
	}
	public void setMessageMaxPoolSize(Integer messageMaxPoolSize) {
		this.messageMaxPoolSize = messageMaxPoolSize;
	}
	
	
	
	

}
