package mapreduce.node.connection;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	
	private String senderHost;
	private Integer senderPort;
	private String messageId;
	private Date timestamp;
	
	public Message(String senderHost, Integer senderPort) {
		this.senderHost = senderHost;
		this.senderPort = senderPort;
		timestamp=new Date();
		messageId=senderHost+"_"+timestamp.getTime();
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
}
