package mapreduce.sdk;

import java.io.Serializable;

public class MasterInfo implements Serializable {
	private String MasterHost;
	private Integer MasterPort;
	public String getMasterHost() {
		return MasterHost;
	}
	public void setMasterHost(String masterHost) {
		MasterHost = masterHost;
	}
	public Integer getMasterPort() {
		return MasterPort;
	}
	public void setMasterPort(Integer masterPort) {
		MasterPort = masterPort;
	}
	public MasterInfo(String masterHost, Integer masterPort) {
	
		MasterHost = masterHost;
		MasterPort = masterPort;
	}
	
	
}
