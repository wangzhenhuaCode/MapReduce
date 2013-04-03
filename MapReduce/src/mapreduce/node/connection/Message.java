package mapreduce.node.connection;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	
	private String senderHost;
	private Integer senderPort;
	private String messageId;
	private Date timestamp;
}
