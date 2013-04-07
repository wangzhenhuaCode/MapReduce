package mapreduce.node.logic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


import mapreduce.node.NodeSystem;
import mapreduce.node.connection.JobMessage;
import mapreduce.node.connection.Message;
import mapreduce.node.connection.NodeMessage;
import mapreduce.node.connection.TaskMessage;
import mapreduce.node.logic.Job.JobStatus;
import mapreduce.node.logic.Task.TaskStatus;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.InputSplit;
import mapreduce.sdk.JobConf;
import mapreduce.sdk.WrapObject;
import mapreduce.sdk.Writable;




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
			File output=new File(conf.getConfiguration().get("mapreduce.output.path"));
			if(!conf.getConfiguration().containsKey("mapreduce.workingDirectory")){
				String workingDirectory=output.getParent();
				workingDirectory=workingDirectory+job.getJobId();
				conf.setWorkingDirectory(workingDirectory);
			}
			File directory=new File(conf.getConfiguration().get("mapreduce.output.path"));
			directory.createNewFile();
			
			InputFormat inputInstance=(InputFormat) Class.forName(conf.getConfiguration().get("mapreduce.input.format")).newInstance();
			InputSplit[] inputsplit=inputInstance.getSplit(conf, inputpath);
			job.setTaskList(new ArrayList<Task>());
			for(int i=0;i<inputsplit.length;i++){
				MapTask task=new MapTask(job.getJobId(),"M+"+i);
				task.setInputSplit(inputsplit[i]);
				task.setConf(conf);
				job.getTaskList().add(task);
			}
			NodeBalance.assignTask(job.getTaskList());
			NodeSystem.jobList.put(job.getJobId(), job);
		}
	}
	private void processTaskMessage(TaskMessage message) throws IOException, ClassNotFoundException{
		
			if(message.getTask().getStatus().equals(Task.TaskStatus.END)){
				Job job=NodeSystem.jobList.get(message.getTask().getJobId());
				synchronized(job){
				ReduceTask reducetask=job.getAvailableReduce(message.getTask());
				if(!job.getStatus().equals(JobStatus.JOB_FINALL_STATE)){
					
					if(reducetask==null){
						
						//Magic Number
						//
						//Magic Number
						reducetask=new ReduceTask(message.getTask().getJobId(),"R-"+(new Date()).getTime(),5);
						reducetask.setStatus(Task.TaskStatus.BEGIN);
						job.getTaskList().add(reducetask);
					}
					if(reducetask.getSourceTaskList().size()==reducetask.getReduceNum()){
						NodeStatus node=NodeBalance.getLeastBusyNode();
						reducetask.setNode(node);
						reducetask.setConf(job.getConf());
						NodeBalance.assignTask(reducetask);
					}
				
				}else{
					reducetask.setStatus(TaskStatus.JOB_FINAL);
					NodeStatus node=NodeBalance.getLeastBusyNode();
					reducetask.setNode(node);
					NodeBalance.assignTask(reducetask);
				}
				}
			}else if(message.getTask().getStatus().equals(Task.TaskStatus.JOB_FINISHED)){
				Job job=NodeSystem.jobList.get(message.getTask().getJobId());
				JobConf conf=job.getConf();
				OutputCollection<WrapObject,Writable> collection=new OutputCollection<WrapObject,Writable>();
				collection.add(message.getTask().getOutput());
				collection.output(conf.getConfiguration().get("mapreduce.output.path"));
				job.setStatus(Job.JobStatus.JOB_FINISHED);
			}
		
	}
	private void processNodeMessage(NodeMessage message){
		
	}



}
