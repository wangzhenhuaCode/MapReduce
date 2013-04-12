package mapreduce.node.connection;

import mapreduce.sdk.JobConf;



public class JobMessage extends Message {
	public JobMessage(
			String receiverHost, Integer receiverPort, JobConf conf) {
		super( receiverHost, receiverPort);
		this.jobConf=conf;
	}
	private JobConf jobConf;
	public JobConf getJobConf() {
		return jobConf;
	}
	public void setJobConf(JobConf jobconf) {
		this.jobConf = jobconf;
	}
	
	

	

}
