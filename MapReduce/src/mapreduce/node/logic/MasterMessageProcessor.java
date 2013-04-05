package mapreduce.node.logic;

import java.io.IOException;
import java.util.Map;

import mapreduce.node.connection.JobMessage;
import mapreduce.node.connection.Message;
import mapreduce.node.logic.Job.Status;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.JobConf;
import mapreduce.sdk.RecordReader;
import mapreduce.sdk.TextInputFormat;
import mapreduce.sdk.WrapObject;

public class MasterMessageProcessor implements MessageProcessor {

	@Override
	public void process(Message message) {
		if(message instanceof JobMessage){
			JobMessage m=(JobMessage)message;
			try {
				processJobMessage(m);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void processJobMessage(JobMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		Job job=message.getJob();
		if(job.getStatus()==Status.JOB_INIT){
			JobConf conf=job.getConf();
			String[] inputpath=conf.getConfiguration().get("mapreduce.input.path").split(",");
			InputFormat inputInstance=(InputFormat) Class.forName(conf.getConfiguration().get("mapreduce.input.format")).newInstance();
			
		}
	}



}
