package mapreduce.node.connection;

import mapreduce.node.logic.Job;

public class JobMessage extends Message {
	public JobMessage(String senderHost, Integer senderPort,
			String receiverHost, Integer receiverPort, Job job) {
		super(senderHost, senderPort, receiverHost, receiverPort);
		this.job=job;
	}
	private Job job;
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}

	

}
