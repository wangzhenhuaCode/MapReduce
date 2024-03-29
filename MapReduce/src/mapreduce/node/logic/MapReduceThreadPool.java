package mapreduce.node.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mapreduce.node.NodeSystem;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.connection.TaskMessage;
import mapreduce.node.logic.Task.TaskStatus;
import mapreduce.sdk.InputFormat;
import mapreduce.sdk.JobConf;
import mapreduce.sdk.Mapper;
import mapreduce.sdk.RecordReader;
import mapreduce.sdk.Reducer;
import mapreduce.sdk.WrapObject;
import mapreduce.sdk.Writable;

public class MapReduceThreadPool {

	private static Thread[] workers;
	private static int capability;
	private static Queue<TaskMessage> taskQueue;
	private static Integer working;
	public static void init(int capability){
		MapReduceThreadPool.capability=capability;
		working=0;
		taskQueue=new LinkedList<TaskMessage>();
		workers=new Thread[capability];
		for(int i=0;i<capability;i++){
			workers[i]=new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true){
						
						TaskMessage message=null;
						synchronized(taskQueue){
							while(taskQueue.isEmpty()){
								try {
									taskQueue.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							message=taskQueue.poll();
						}
						synchronized(working){
							working++;
						}
						try{
							
						if(message.getTask() instanceof MapTask){
							MapTask task=(MapTask)message.getTask();
							JobConf conf=task.getConf();
							task.getReporter().log("Begin map");
							
							String mapClassAddr=conf.getConfiguration().get("mapreduce.map.class");
							String combineClassAddr=conf.getConfiguration().get("mapreduce.combiner.class");
							URL myurl[]={new URL("file:"+conf.getConfiguration().get("mapreduce.jar"))};
							URLClassLoader loader=new URLClassLoader(myurl);
							Class mapClass=loader.loadClass(mapClassAddr);
							Class combineClass=loader.loadClass(combineClassAddr);
							Class inputFormatClass=loader.loadClass(task.getConf().getConfiguration().get("mapreduce.input.format"));
							InputFormat instance=(InputFormat) inputFormatClass.newInstance();
							RecordReader<WrapObject,WrapObject> reader=instance.getRecordReader(task.getInputSplit(), task.getConf(), null);
							
							Class outputKeyClass=loader.loadClass(conf.getConfiguration().get("mapreduce.output.key.class"));
							Class outputValueClass=loader.loadClass(conf.getConfiguration().get("mapreduce.output.value.class"));
							
							Mapper map=(Mapper) mapClass.newInstance();
							
							
							WrapObject key=reader.createKey();
							Class keyClass=key.getClass();
							WrapObject value=reader.createValue();
							Class valueClass=value.getClass();
							OutputCollection<WrapObject,Writable> outputCollection=new OutputCollection<WrapObject,Writable>();
							while(reader.next(key, value)){
								if(key.getValue()!=null){
									
									map.map(keyClass.cast(key),valueClass.cast(value), outputCollection, task.getReporter());
									
								}
								key=reader.createKey();
								value=reader.createValue();
								
							}
							Reducer combine=(Reducer)combineClass.newInstance();
							OutputCollection<WrapObject,Writable> combineCollection=new OutputCollection<WrapObject,Writable>();
							List<Writable> list=new ArrayList<Writable>();
							WrapObject k=null;
							outputCollection.sort();
							for(int i=0;i<outputCollection.getOutputList().size();i++){
								if(k==null){
									k=outputCollection.getOutputList().get(i).key;
									list.add(outputCollection.getOutputList().get(i).value);
								}else{
									if(!k.equals(outputCollection.getOutputList().get(i).key)){
										
											combine.reduce(k, list.iterator(), combineCollection, task.getReporter());
										
										list=new ArrayList<Writable>();
										k=outputCollection.getOutputList().get(i).key;
							
										list.add(outputCollection.getOutputList().get(i).value);
										
									}else{
										list.add(outputCollection.getOutputList().get(i).value);
									}
									
								}
							}
							
							if(k!=null){
								
									combine.reduce(k, list.iterator(), combineCollection, null);
								
							}
							
							String output=conf.getConfiguration().get("mapreduce.workingDirectory")+task.getJobId()+"_"+task.getTaskId();
							
								combineCollection.serialize(output);
							
							task.setOutput(output);
							task.setInputSplit(null);
							task.setStatus(Task.TaskStatus.END);
							task.getReporter().log("End map");
							TaskMessage message2=new TaskMessage(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort(),task);
							ServerSocketConnection.sendMessage(message2);
							
						}else{
							ReduceTask task=(ReduceTask)message.getTask();
							JobConf conf=task.getConf();
							task.getReporter().log("Begin reduce");
							String reduceClassAddr=conf.getConfiguration().get("mapreduce.reduce.class");
							URL myurl[]={new URL("file:"+conf.getConfiguration().get("mapreduce.jar"))};
							URLClassLoader loader=new URLClassLoader(myurl);
							Class reduceClass=loader.loadClass(reduceClassAddr);
							Reducer reduce=(Reducer) reduceClass.newInstance();
							OutputCollection<WrapObject,Writable> combineCollection=new OutputCollection<WrapObject,Writable>();
							OutputCollection<WrapObject,Writable> reduceCollection=new OutputCollection<WrapObject,Writable>();
							List<Writable> list=new ArrayList<Writable>();
							WrapObject k=null;
							for(Task t:task.getSourceTaskList()){
								combineCollection.add(t.getOutput());
							}
							task.setSourceTaskList(null);
							combineCollection.sort();
							
							for(int i=0;i<combineCollection.getOutputList().size();i++){
								if(k==null){
									k=combineCollection.getOutputList().get(i).key;
									list.add(combineCollection.getOutputList().get(i).value);
								}else{
									if(!k.equals(combineCollection.getOutputList().get(i).key)){
										
											reduce.reduce(k, list.iterator(), reduceCollection, task.getReporter());
										
										list=new ArrayList<Writable>();
										k=combineCollection.getOutputList().get(i).key;
									
										list.add(combineCollection.getOutputList().get(i).value);
										
									}else{
										list.add(combineCollection.getOutputList().get(i).value);
									}
									
								}
							}
							if(k!=null){
								
									reduce.reduce(k, list.iterator(), reduceCollection, null);
								
							}
							
							String output=conf.getConfiguration().get("mapreduce.workingDirectory")+task.getJobId()+"_"+task.getTaskId();
							
								reduceCollection.serialize(output);
							
							task.setOutput(output);
							
							if(task.getStatus().equals(Task.TaskStatus.JOB_FINAL)){
								task.setStatus(Task.TaskStatus.JOB_FINISHED);
								
							}else{
								task.setStatus(Task.TaskStatus.END);
							}
							task.getReporter().log("End reduce");
							TaskMessage message2=new TaskMessage(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort(),task);
							ServerSocketConnection.sendMessage(message2);
							
						}
						
						}catch(Exception e){
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							PrintStream ps = new PrintStream(os);
							e.printStackTrace(ps);
							Task task=message.getTask();
							task.getReporter().log("Error:"+os.toString());
							task.setStatus(TaskStatus.ERROR);
							TaskMessage message2=new TaskMessage(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort(),task);
							try {
								ServerSocketConnection.sendMessage(message2);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						synchronized(working){
							working--;
						}
					}
						
					
				}
				
			});
			workers[i].start();
		}
		
	}
	public static void newTask(TaskMessage message){
		synchronized(taskQueue){
			taskQueue.add(message);
			taskQueue.notify();
		}
	}
	public static int getWorkingNum(){
		return working;
	}
	public static int getWaitingNum(){
		return taskQueue.size();
	}
}
