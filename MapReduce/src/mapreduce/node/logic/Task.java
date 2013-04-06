package mapreduce.node.logic;

import java.io.Serializable;


import mapreduce.sdk.JobConf;

public class Task implements Serializable {
	public enum TaskStatus{BEGIN,END,JOB_FINAL,JOB_FINISHED}
	private String jobId;
	private String taskId;
	private TaskStatus status;
	private String output;
	private JobConf conf;
	private transient NodeStatus node;
	
	public Task(String jobId, String taskId) {
		super();
		this.jobId = jobId;
		this.taskId = taskId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}



	public NodeStatus getNode() {
		return node;
	}

	public void setNode(NodeStatus node) {
		this.node = node;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public JobConf getConf() {
		return conf;
	}

	public void setConf(JobConf conf) {
		this.conf = conf;
	}
	
	
	
}
