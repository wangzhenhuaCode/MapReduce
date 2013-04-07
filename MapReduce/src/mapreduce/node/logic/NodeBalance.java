package mapreduce.node.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mapreduce.node.NodeSystem;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.connection.TaskMessage;
import mapreduce.sdk.InputSplit;

public class NodeBalance {

	public static void init(){
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
					while(true){
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<NodeStatus> disconnetNodelist=new ArrayList<NodeStatus>(); 
					synchronized(NodeSystem.nodeList){
						for(int i=0;i<NodeSystem.nodeList.size();i++){
							NodeStatus node=NodeSystem.nodeList.get(i);
							if(node.isUpdated()){
								node.setUpdated(false);
							}else{
								disconnetNodelist.add(node);
								NodeSystem.nodeList.remove(node);
							}
						}
					}
					for(NodeStatus node:disconnetNodelist){
						recover(node);
					}
					
				}
			}
			private void recover(NodeStatus node){
				for(Task t:node.getTaskList()){
					if(t.getStatus().equals(Task.TaskStatus.BEGIN)){
						assignTask(t);
					}
				}
			}
			
		});
		thread.start();
	}
	public static void updateNode(NodeStatus node){
		synchronized(NodeSystem.nodeList){
		for(NodeStatus n:NodeSystem.nodeList){
			if(node.getNodeId().equals(n.getNodeId())){
				n.setRunning(node.getRunning());
				n.setWaiting(node.getWaiting());
				n.setUpdated(true);
				return;
				
			}
		}
		node.setUpdated(true);
		NodeSystem.nodeList.add(node);
		}
	}
	public static void assignTask(List<Task> taskList){
		
		int inputnum=taskList.size();
		int nodenum=NodeSystem.nodeList.size();
		int cur=0;
		int count=0;
		sort();
		while(cur<inputnum){
			
				Task task=taskList.get(cur);
				NodeStatus node=NodeSystem.nodeList.get(count);
				task.setNodeId(node.getNodeId());
				task.setStatus(Task.TaskStatus.BEGIN);
				TaskMessage message =new TaskMessage(node.getConfiguration().getLocalHostName(),node.getConfiguration().getLocalPort(),task);
				try {
					ServerSocketConnection.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					continue;
				}
				node.addTask(task);
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
		NodeStatus node=NodeSystem.nodeList.get(cur);
		task.setNodeId(node.getNodeId());
		task.setStatus(Task.TaskStatus.BEGIN);
		TaskMessage message =new TaskMessage(node.getConfiguration().getLocalHostName(),node.getConfiguration().getLocalPort(),task);
		try {
			ServerSocketConnection.sendMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			cur++;
			continue;
		}
		node.addTask(task);
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
