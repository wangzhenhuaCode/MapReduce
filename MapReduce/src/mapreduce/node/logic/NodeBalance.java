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
			
		});
		thread.start();
	}
	public static void updateNode(NodeStatus node,Boolean isNew){
		synchronized(NodeSystem.nodeList){
		for(NodeStatus n:NodeSystem.nodeList){
			if(node.getNodeId().equals(n.getNodeId())){
				n.setRunning(node.getRunning());
				n.setWaiting(node.getWaiting());
				n.setUpdated(true);
				if(isNew){
					recover(n);
				}
				return;
				
			}
		}
		node.setUpdated(true);
		node.setTaskList(new ArrayList<Task>());
		NodeSystem.nodeList.add(node);
		NodeSystem.nodeList.notifyAll();
		}
	}
	private static void recover(NodeStatus node){
		List<Task> tasklist=new ArrayList<Task>();
		for(Task t:node.getTaskList()){
			if(t.getStatus().equals(Task.TaskStatus.BEGIN)){
				tasklist.add(t);
			}
		}
		assignTask(tasklist);
	}
	public static void assignTask(List<Task> taskList){
		
			int inputnum=taskList.size();
			int cur=0;
			sort();
			while(cur<inputnum){
				Task task=null;
					try{
					task=taskList.get(cur);
					}catch(Exception e){
						return;
					}
					NodeStatus node=null;
					synchronized(NodeSystem.nodeList){
						while(NodeSystem.nodeList.isEmpty()){
							try {
								NodeSystem.nodeList.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						node=NodeSystem.nodeList.get(cur%NodeSystem.nodeList.size());
					}
					task.setNodeId(node.getNodeId());
					if(task.getStatus()==null)
						task.setStatus(Task.TaskStatus.BEGIN);
					TaskMessage message =new TaskMessage(node.getConfiguration().getLocalHostName(),node.getConfiguration().getLocalPort(),task);
					try {
						ServerSocketConnection.sendMessage(message);
					} catch (IOException e) {
						synchronized(NodeSystem.nodeList){
							NodeSystem.nodeList.remove(node);
						}
						continue;
					}
					node.addTask(task);
					cur++;
		
				
			}
		
	
	}
	public static void assignTask(Task task){
			while(true){
				NodeStatus node=getLeastBusyNode();
				task.setNodeId(node.getNodeId());
				if(task.getStatus()==null)
					task.setStatus(Task.TaskStatus.BEGIN);
				TaskMessage message =new TaskMessage(node.getConfiguration().getLocalHostName(),node.getConfiguration().getLocalPort(),task);
				try {
					ServerSocketConnection.sendMessage(message);
				} catch (IOException e) {
					synchronized(NodeSystem.nodeList){
						NodeSystem.nodeList.remove(node);
					}
					continue;
				}
				node.addTask(task);
				break;
			}
			
		
	}
	public static NodeStatus getLeastBusyNode(){
		sort();
		NodeStatus node=null;
		while(node==null){
			try{
				node=NodeSystem.nodeList.get(0);
			}catch(Exception e){
				node=null;
				synchronized(NodeSystem.nodeList){
					try {
						NodeSystem.nodeList.wait();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		return node;
		
		
	}
	private static void sort(){
		synchronized(NodeSystem.nodeList){
		Collections.sort(NodeSystem.nodeList, new Comparator<NodeStatus>(){

			@Override
			public int compare(NodeStatus arg0, NodeStatus arg1) {
				// TODO Auto-generated method stub
				if(arg0.getConfiguration().getCapability()-arg0.getTaskList().size()>arg1.getConfiguration().getCapability()-arg1.getTaskList().size()){
					return 1;
				}else if(arg0.getConfiguration().getCapability()-arg0.getTaskList().size()<arg1.getConfiguration().getCapability()-arg1.getTaskList().size()){
					return -1;
				}else
					return 0;
			}
			
		});
		}
	}
}
