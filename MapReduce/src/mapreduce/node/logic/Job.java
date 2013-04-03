package mapreduce.node.logic;

import java.io.Serializable;

import mapreduce.sdk.JobConf;

public class Job implements Serializable {
	public static enum Status{JOB_INIT};
	private JobConf conf;
	private Status status;
	public JobConf getConf() {
		return conf;
	}
	public void setConf(JobConf conf) {
		this.conf = conf;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Job(JobConf conf, Status jobInit) {
		
		this.conf = conf;
		this.status = jobInit;
	}
	
	
}
