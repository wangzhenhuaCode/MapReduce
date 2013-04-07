package mapreduce.node.connection;

import mapreduce.node.logic.NodeStatus;

public class NodeMessage extends Message {
	public NodeMessage(String receiverHost, Integer receiverPort,NodeStatus node) {
		super( receiverHost, receiverPort);
		this.node=node;
	}

	private NodeStatus node;
	public NodeStatus getNode() {
		return node;
	}
	public void setNode(NodeStatus node) {
		this.node = node;
	}
	
	

}
