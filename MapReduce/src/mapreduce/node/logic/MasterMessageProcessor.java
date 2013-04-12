package mapreduce.node.logic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;



import mapreduce.node.NodeSystem;
import mapreduce.node.connection.JobMessage;
import mapreduce.node.connection.Message;
import mapreduce.node.connection.MgtMessage;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.connection.MgtMessage.Operation;
import mapreduce.node.connection.NodeMessage;
import mapreduce.node.connection.TaskMessage;
import mapreduce.node.logic.Job.JobStatus;
import mapreduce.node.logic.Task.TaskStatus;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.InputSplit;
import mapreduce.sdk.JobConf;
import mapreduce.sdk.OutputFormat;
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
		}else if(message instanceof NodeMessage){
			processNodeMessage((NodeMessage)message);
		}else if(message instanceof MgtMessage){
			try {
				processMgtMessage((MgtMessage)message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void processJobMessage(JobMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
			JobConf conf=message.getJobConf();
			Job job=new Job(conf,Job.JobStatus.JOB_RUNNING,conf.getConfiguration().get("mapreduce.job.id"));
			String[] inputpath=conf.getConfiguration().get("mapreduce.input.path").split(",");
			URL myurl[]={new URL("file:"+conf.getConfiguration().get("mapreduce.jar"))};
			URLClassLoader loader=new URLClassLoader(myurl);
			InputFormat inputInstance=(InputFormat) loader.loadClass(conf.getConfiguration().get("mapreduce.input.format")).newInstance();
			InputSplit[] inputsplit=inputInstance.getSplit(conf, inputpath);
			for(int i=0;i<inputsplit.length;i++){
				MapTask task=new MapTask(job.getJobId(),"M+"+i);
				task.setInputSplit(inputsplit[i]);
				task.setConf(conf);
				task.setStatus(TaskStatus.BEGIN);
				job.getTaskList().add(task);
			}
			NodeSystem.jobList.put(job.getJobId(), job);
			job.getReport().log("Map to "+job.getTaskList().size()+" tasks");
			NodeBalance.assignTask(job.getTaskList());
			
		
	}
	private void processTaskMessage(TaskMessage message) throws IOException, ClassNotFoundException{
		Job job=NodeSystem.jobList.get(message.getTask().getJobId());
		if(job.getStatus().equals(JobStatus.JOB_TERMINATED)){
			removeTask(message.getTask());
			return;
		}
			if(message.getTask().getStatus().equals(Task.TaskStatus.END)){
			
				
				ReduceTask reducetask=job.getAvailableReduce(message.getTask());
					if(job.getStatus().equals(JobStatus.JOB_FINALL_STATE)){
						
						reducetask.setConf(job.getConf());
						reducetask.setStatus(TaskStatus.JOB_FINAL);
						NodeBalance.assignTask(reducetask);
						
					
					}else if(job.getStatus().equals(JobStatus.JOB_FINISHED)){
						try {
							finshJob(message);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						if(reducetask.getSourceTaskList().size()==reducetask.getReduceNum()){
							reducetask.setConf(job.getConf());
							NodeBalance.assignTask(reducetask);
						}
					}
				
			}else if(message.getTask().getStatus().equals(Task.TaskStatus.JOB_FINISHED)){
				try {
					finshJob(message);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(message.getTask().getStatus().equals(Task.TaskStatus.ERROR)){
				JobConf conf=job.getConf();
				deleteTemp(conf.getConfiguration().get("mapreduce.workingDirectory"));
				job.getReport().systemLog(message.getTask().getReporter().getLog());
				job.getTaskList().clear();
				job.getReport().log("Job terminated");

				job.setStatus(Job.JobStatus.JOB_TERMINATED);
			}
			removeTask(message.getTask());
			
		
	}
	private void processNodeMessage(NodeMessage message){
		NodeBalance.updateNode(message.getNode());
	}
	private void processMgtMessage(MgtMessage message) throws IOException{
		if(message.getOperation().equals(Operation.JOB_LIST)){
			RemoteReport report=new RemoteReport();
			Iterator<Map.Entry<String, Job>> it=NodeSystem.jobList.entrySet().iterator();
			report.systemLog("Job Id\tJob Name\tJob Status\n");
			while(it.hasNext()){
				Job job=it.next().getValue();
				report.systemLog(job.getJobId()+"\t"+job.getConf().getConfiguration().get("mapreduce.job.name")+"\t"+job.getStatus()+"\n");
			}
			message.setReport(report);
			message.swapAddress();
			ServerSocketConnection.sendMessage(message);
		}else if(message.getOperation().equals(Operation.JOB_REPORT)){
			String id=message.getId();
			RemoteReport report;
			if(NodeSystem.jobList.containsKey(id)){
				Job job=NodeSystem.jobList.get(id);
				report=(RemoteReport) job.getReport();
			}else{
				report=new RemoteReport();
				report.systemLog("Couldn't find job");
			}
			message.setReport(report);
			message.swapAddress();
			ServerSocketConnection.sendMessage(message);
		}else if(message.getOperation().equals(Operation.KILL_JOB)){
			RemoteReport report=new RemoteReport();
			String id=message.getId();
			if(NodeSystem.jobList.containsKey(id)){
				Job job=NodeSystem.jobList.get(id);
				if(!job.getStatus().equals(JobStatus.JOB_FINISHED)){
					JobConf conf=job.getConf();
					deleteTemp(conf.getConfiguration().get("mapreduce.workingDirectory"));
					
					job.getTaskList().clear();
					job.getReport().log("Job terminated");
					
					job.setStatus(Job.JobStatus.JOB_TERMINATED);
					report.systemLog("Job killed");
				}else{
					report.systemLog("Job has finished");
				}
			}else{
				report.systemLog("Couldn't find job");
			}
			message.setReport(report);
			message.swapAddress();
			ServerSocketConnection.sendMessage(message);
		}else if(message.getOperation().equals(Operation.SHUT_DOWN)){
			System.exit(0);
		}
	}
	private void removeTask(Task task){
		String nodeId=task.getNodeId();
		NodeStatus node=null;
		for(NodeStatus n:NodeSystem.nodeList){
			if(n.getNodeId().equals(nodeId)){
				node=n;
				break;
			}
		}
		node.getTaskList().remove(task);
	}
	private void finshJob(TaskMessage message) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Job job=NodeSystem.jobList.get(message.getTask().getJobId());
		JobConf conf=job.getConf();
		OutputCollection<WrapObject,Writable> collection=new OutputCollection<WrapObject,Writable>();
		collection.add(message.getTask().getOutput());
		URL myurl[]={new URL("file:"+conf.getConfiguration().get("mapreduce.jar"))};
		URLClassLoader loader=new URLClassLoader(myurl);
		OutputFormat output=(OutputFormat) loader.loadClass(conf.getConfiguration().get("mapreduce.output.format")).newInstance();
		collection.output(conf.getConfiguration().get("mapreduce.output.path"),output);
		deleteTemp(conf.getConfiguration().get("mapreduce.workingDirectory"));
		job.getTaskList().clear();
		job.getReport().log("Job finished");
		job.setStatus(Job.JobStatus.JOB_FINISHED);
		
	}
	private void deleteTemp(String path){
		File file=new File(path);
		for(File f:file.listFiles()){
			f.delete();
		}
		file.delete();
	}


}
