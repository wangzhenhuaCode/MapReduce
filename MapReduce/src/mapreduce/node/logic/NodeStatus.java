package mapreduce.node.logic;

import java.util.List;

import mapreduce.node.configuration.Configuration;

public class NodeStatus {

	private Configuration configuration;
	private Integer running;
	private Integer waiting;
	private String NodeId;
	private transient boolean updated;
	private transient List<Task> taskList;
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public Integer getRunning() {
		return running;
	}
	public void setRunning(Integer running) {
		this.running = running;
	}
	public Integer getWaiting() {
		return waiting;
	}
	public void setWaiting(Integer waiting) {
		this.waiting = waiting;
	}
	public String getNodeId() {
		return NodeId;
	}
	public void setNodeId(String nodeId) {
		NodeId = nodeId;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public List<Task> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	public void addTask(Task task){
		task.setNodeId(NodeId);
		
	}
	
}
