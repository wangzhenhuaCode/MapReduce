package mapreduce.node.connection;

import mapreduce.node.logic.Task;

public class TaskMessage extends Message {

	public TaskMessage(String senderHost, Integer senderPort,
			String receiverHost, Integer receiverPort, Task task) {
		super(senderHost, senderPort, receiverHost, receiverPort);
		this.task=task;
	}
	public TaskMessage(String receiverHost, Integer receiverPort, Task task) {
		super( receiverHost, receiverPort);
		this.task=task;
	}

	private Task task;

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	

}
