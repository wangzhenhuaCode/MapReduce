package mapreduce.node.logic;

import java.io.IOException;

import mapreduce.node.connection.Message;
import mapreduce.node.connection.TaskMessage;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.RecordReader;
import mapreduce.sdk.Writable;

public class SlaveMessageProcessor implements MessageProcessor {

	@Override
	public void process(Message message) {
		// TODO Auto-generated method stub

	}
	private void processTaskMessage(TaskMessage message) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(message.getTask() instanceof MapTask){
			MapTask task=(MapTask)message.getTask();
			Class inputFormatClass=Class.forName(task.getConf().getConfiguration().get("mapreduce.input.format"));
			InputFormat instance=(InputFormat) inputFormatClass.newInstance();
			RecordReader<Writable<Object>,Writable<Object>> reader=instance.getRecordReader(task.getInputSplit(), task.getConf(), null);
			Writable<Object> key=reader.createKey();
			Writable<Object> value=reader.createValue();
			while(reader.next(key, value)){
				key=reader.createKey();
				value=reader.createValue();
			}
		}else{
			
		}
	}
	

}
