package mapreduce.node.logic;

import mapreduce.node.connection.Message;

public interface MessageProcessor {
	public void process(Message message);
}
