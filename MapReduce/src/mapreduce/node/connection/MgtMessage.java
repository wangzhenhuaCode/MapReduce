package mapreduce.node.connection;

import mapreduce.sdk.Reporter;

public class MgtMessage extends Message {
	public enum Operation{SHUT_DOWN,JOB_STATUS,KILL_JOB};
	private Operation operation;
	private String id;
	private Reporter report;
	
	public MgtMessage(String receiverHost, Integer receiverPort, Operation operation) {
		super(receiverHost, receiverPort);
		this.operation=operation;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Reporter getReport() {
		return report;
	}

	public void setReport(Reporter report) {
		this.report = report;
	}
	
	

}
