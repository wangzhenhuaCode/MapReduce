package mapreduce.node.logic;

import mapreduce.node.connection.Message;
import mapreduce.node.connection.TaskMessage;

public class SlaveMessageProcessor implements MessageProcessor {

	@Override
	public void process(Message message) {
		// TODO Auto-generated method stub

	}
	private void processTaskMessage(TaskMessage message){
		if(message.getTask() instanceof MapTask){
			
		}else{
			
		}
	}
	

}
