package mapreduce.node.connection;

import mapreduce.node.logic.NodeStatus;

public class NodeMessage extends Message {
	public enum NodeMessageType{NEW_NODE,NODE_UPDATE}
	public NodeMessage(String receiverHost, Integer receiverPort,NodeStatus node) {
		super( receiverHost, receiverPort);
		this.node=node;
	}

	private NodeStatus node;
	private NodeMessageType type;
	public NodeStatus getNode() {
		return node;
	}
	public void setNode(NodeStatus node) {
		this.node = node;
	}
	public NodeMessageType getType() {
		return type;
	}
	public void setType(NodeMessageType type) {
		this.type = type;
	}
	
	

}
