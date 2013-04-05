package mapreduce.node.logic;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mapreduce.node.NodeSystem;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.connection.TaskMessage;
import mapreduce.sdk.InputSplit;

public class NodeBalance {

	public static NodeBalance instance;
	public static void init(){
		
	}
	public static void assignTask(List<Task> taskList){
		
		int inputnum=taskList.size();
		int nodenum=NodeSystem.nodeList.size();
		int cur=0;
		int count=0;
		sort();
		while(cur<inputnum){
			
				Task task=taskList.get(cur);
				task.setNode(NodeSystem.nodeList.get(count));
				task.setStatus(Task.TaskStatus.BEGIN);
				TaskMessage message =new TaskMessage(task.getNode().getConfiguration().getLocalHostName(),task.getNode().getConfiguration().getLocalPort(),task);
				try {
					ServerSocketConnection.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					continue;
				}
				cur++;
				count++;
				if(count==nodenum)
					count=0;
			
		}
		
	
	}
	public static void assignTask(Task task){
		int cur=0;
		while(true){
		sort();
		task.setNode(NodeSystem.nodeList.get(cur));
		task.setStatus(Task.TaskStatus.BEGIN);
		TaskMessage message =new TaskMessage(task.getNode().getConfiguration().getLocalHostName(),task.getNode().getConfiguration().getLocalPort(),task);
		try {
			ServerSocketConnection.sendMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			cur++;
			continue;
		}
		break;
	}
	}
	public static NodeStatus getLeastBusyNode(){
		sort();
		return NodeSystem.nodeList.get(0);
		
	}
	private static void sort(){
		synchronized(NodeSystem.nodeList){
		Collections.sort(NodeSystem.nodeList, new Comparator<NodeStatus>(){

			@Override
			public int compare(NodeStatus arg0, NodeStatus arg1) {
				// TODO Auto-generated method stub
				if(arg0.getConfiguration().getCapability()-arg0.getRunning()-arg0.getWaiting()>arg1.getConfiguration().getCapability()-arg1.getRunning()-arg1.getWaiting()){
					return 1;
				}else if(arg0.getConfiguration().getCapability()-arg0.getRunning()-arg0.getWaiting()<arg1.getConfiguration().getCapability()-arg1.getRunning()-arg1.getWaiting()){
					return -1;
				}else
					return 0;
			}
			
		});
		}
	}
}