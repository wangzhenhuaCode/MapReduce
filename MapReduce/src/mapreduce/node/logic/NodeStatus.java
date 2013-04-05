package mapreduce.node.logic;

import mapreduce.node.configuration.Configuration;

public class NodeStatus {

	private Configuration configuration;
	private Integer running;
	private Integer waiting;
	private Integer NodeId;
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
	public Integer getNodeId() {
		return NodeId;
	}
	public void setNodeId(Integer nodeId) {
		NodeId = nodeId;
	}
	
}
