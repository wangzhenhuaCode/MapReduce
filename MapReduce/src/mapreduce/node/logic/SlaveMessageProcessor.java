package mapreduce.node.logic;


import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


import mapreduce.node.NodeSystem;
import mapreduce.node.connection.Message;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.connection.TaskMessage;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.JobConf;
import mapreduce.sdk.Mapper;
import mapreduce.sdk.OutputCollector;
import mapreduce.sdk.RecordReader;
import mapreduce.sdk.Reducer;
import mapreduce.sdk.WrapObject;
import mapreduce.sdk.Writable;

public class SlaveMessageProcessor implements MessageProcessor {

	@Override
	public void process(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof TaskMessage){
			processTaskMessage((TaskMessage)message);
		}

	}
	private void processTaskMessage(TaskMessage message) {
		MapReduceThreadPool.newTask(message);
	}
	

}
