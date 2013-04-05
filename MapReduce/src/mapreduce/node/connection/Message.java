package mapreduce.node.connection;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

import mapreduce.node.NodeSystem;

public class Message implements Serializable{
	
	private String senderHost;
	private Integer senderPort;
	private String messageId;
	private Date timestamp;
	private String receiverHost;
	private Integer receiverPort;
	
	
	public Message(String senderHost, Integer senderPort, String receiverHost,
			Integer receiverPort) {
		timestamp=new Date();
		messageId=senderHost+"_"+timestamp.getTime();
		this.senderHost = senderHost;
		this.senderPort = senderPort;
		this.receiverHost = receiverHost;
		this.receiverPort = receiverPort;
	}
	
	public Message(String receiverHost, Integer receiverPort) {
		this.senderHost = NodeSystem.configuration.getLocalHostName();
		this.senderPort = NodeSystem.configuration.getLocalPort();
		this.receiverHost = receiverHost;
		this.receiverPort = receiverPort;
	}

	public String getSenderHost() {
		return senderHost;
	}
	public void setSenderHost(String senderHost) {
		this.senderHost = senderHost;
	}
	public Integer getSenderPort() {
		return senderPort;
	}
	public void setSenderPort(Integer senderPort) {
		this.senderPort = senderPort;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getReceiverHost() {
		return receiverHost;
	}

	public void setReceiverHost(String receiverHost) {
		this.receiverHost = receiverHost;
	}

	public Integer getReceiverPort() {
		return receiverPort;
	}

	public void setReceiverPort(Integer receiverPort) {
		this.receiverPort = receiverPort;
	}
}
