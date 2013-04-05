package mapreduce.node.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


import mapreduce.node.NodeSystem;
import mapreduce.node.connection.JobMessage;
import mapreduce.node.connection.Message;
import mapreduce.node.connection.NodeMessage;
import mapreduce.node.connection.TaskMessage;
import mapreduce.node.logic.Job.JobStatus;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.InputSplit;
import mapreduce.sdk.JobConf;


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
		if(job.getStatus()==JobStatus.JOB_INIT){
			JobConf conf=job.getConf();
			String[] inputpath=conf.getConfiguration().get("mapreduce.input.path").split(",");
			InputFormat inputInstance=(InputFormat) Class.forName(conf.getConfiguration().get("mapreduce.input.format")).newInstance();
			InputSplit[] inputsplit=inputInstance.getSplit(conf, inputpath);
			job.setTaskList(new ArrayList<Task>());
			for(int i=0;i<inputsplit.length;i++){
				MapTask task=new MapTask(job.getJobId(),"M+"+i);
				task.setInputSplit(inputsplit[i]);
				job.getTaskList().add(task);
			}
			NodeBalance.assignTask(job.getTaskList());
			NodeSystem.jobList.put(job.getJobId(), job);
		}
	}
	private void processTaskMessage(TaskMessage message){
		if(message.getTask() instanceof MapTask){
			if(message.getTask().getStatus().equals(Task.TaskStatus.END)){
				Job job=NodeSystem.jobList.get(message.getTask().getJobId());
				ReduceTask reducetask=job.getAvailableReduce(message.getTask());
				if(reducetask==null){
					NodeStatus node=NodeBalance.getLeastBusyNode();
					//Magic Number
					//
					//Magic Number
					reducetask=new ReduceTask(message.getTask().getJobId(),"R-"+(new Date()).getTime(),5);
					reducetask.setNode(node);
					reducetask.setStatus(Task.TaskStatus.BEGIN);
					job.getTaskList().add(reducetask);
				}
				
				
			}
		}else{
			
		}
	}
	private void processNodeMessage(NodeMessage message){
		
	}



}
