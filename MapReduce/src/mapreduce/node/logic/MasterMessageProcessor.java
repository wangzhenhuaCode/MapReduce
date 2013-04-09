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
		}else if(message instanceof TaskMessage){
			try {
				processTaskMessage((TaskMessage)message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			processNodeMessage((NodeMessage)message);
		}
		
	}
	private void processJobMessage(JobMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		Job job=message.getJob();
		if(job.getStatus()==JobStatus.JOB_INIT){
			JobConf conf=job.getConf();
			String[] inputpath=conf.getConfiguration().get("mapreduce.input.path").split(",");
			File output=new File(conf.getConfiguration().get("mapreduce.output.path"));
			if(!conf.getConfiguration().containsKey("mapreduce.workingDirectory")){
				String workingDirectory=output.getParentFile().getAbsolutePath();
				workingDirectory=workingDirectory.substring(0,workingDirectory.length()-1);
				workingDirectory=workingDirectory+job.getJobId().replace('.', '_');
				
				conf.setWorkingDirectory(workingDirectory+"/");
			}
			
			File directory=new File(conf.getConfiguration().get("mapreduce.workingDirectory"));
			directory.mkdir();
			
			InputFormat inputInstance=(InputFormat) Class.forName(conf.getConfiguration().get("mapreduce.input.format")).newInstance();
			InputSplit[] inputsplit=inputInstance.getSplit(conf, inputpath);
			job.setTaskList(new ArrayList<Task>());
			for(int i=0;i<inputsplit.length;i++){
				MapTask task=new MapTask(job.getJobId(),"M+"+i);
				task.setInputSplit(inputsplit[i]);
				task.setConf(conf);
				task.setStatus(TaskStatus.BEGIN);
				job.getTaskList().add(task);
			}
			NodeSystem.jobList.put(job.getJobId(), job);
			NodeBalance.assignTask(job.getTaskList());
			
		}
	}
	private void processTaskMessage(TaskMessage message) throws IOException, ClassNotFoundException{
		
			if(message.getTask().getStatus().equals(Task.TaskStatus.END)){
				System.out.println("job:"+message.getTask().getJobId());
				Job job=NodeSystem.jobList.get(message.getTask().getJobId());
				
				ReduceTask reducetask=job.getAvailableReduce(message.getTask());
					if(job.getStatus().equals(JobStatus.JOB_FINALL_STATE)){
						
						reducetask.setConf(job.getConf());
						reducetask.setStatus(TaskStatus.JOB_FINAL);
						NodeBalance.assignTask(reducetask);
						System.out.println("Final");
					
					}else if(job.getStatus().equals(JobStatus.JOB_FINISHED)){
						finshJob(message);
					}else{
						if(reducetask.getSourceTaskList().size()==reducetask.getReduceNum()){
							reducetask.setConf(job.getConf());
							NodeBalance.assignTask(reducetask);
						}
					}
				
			}else if(message.getTask().getStatus().equals(Task.TaskStatus.JOB_FINISHED)){
				finshJob(message);
			}
			Task t=message.getTask();
			String nodeId=t.getNodeId();
			NodeStatus node=null;
			for(NodeStatus n:NodeSystem.nodeList){
				if(n.getNodeId().equals(nodeId)){
					node=n;
					break;
				}
			}
			node.getTaskList().remove(t);
		
	}
	private void processNodeMessage(NodeMessage message){
		NodeBalance.updateNode(message.getNode());
	}
	private void finshJob(TaskMessage message) throws IOException, ClassNotFoundException{
		Job job=NodeSystem.jobList.get(message.getTask().getJobId());
		JobConf conf=job.getConf();
		OutputCollection<WrapObject,Writable> collection=new OutputCollection<WrapObject,Writable>();
		collection.add(message.getTask().getOutput());
		collection.output(conf.getConfiguration().get("mapreduce.output.path"));
		job.setStatus(Job.JobStatus.JOB_FINISHED);
		System.out.println("Job Finish");
	}



}
