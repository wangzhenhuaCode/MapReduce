package mapreduce.node.connection;

public class NodeMessage extends Message {
	public NodeMessage(String senderHost, Integer senderPort,
			String receiverHost, Integer receiverPort) {
		super(senderHost, senderPort, receiverHost, receiverPort);
		// TODO Auto-generated constructor stub
	}

	public static enum NodeMessageStatus{NEW_NODE,NODE_UPDATE}
	
	

}
