package mapreduce.node.connection;

import mapreduce.node.logic.Job;

public class JobMessage extends Message {
	private Job job;
	public JobMessage(String senderHost, Integer senderPort, Job job) {
		super(senderHost, senderPort);
		this.job=job;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}

	

}
