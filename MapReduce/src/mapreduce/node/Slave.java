package mapreduce.node;

import java.io.IOException;

import mapreduce.node.connection.NodeMessage;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.logic.MapReduceThreadPool;
import mapreduce.node.logic.NodeStatus;

public class Slave extends Node {

	public Slave(){
		try {
			run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread update=new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					NodeStatus node=new NodeStatus();
					node.setConfiguration(NodeSystem.configuration);
					node.setNodeId(NodeSystem.configuration.getLocalHostName()+"_"+NodeSystem.configuration.getLocalPort());
					node.setRunning(MapReduceThreadPool.getWorkingNum());
					node.setWaiting(MapReduceThreadPool.getWaitingNum());
					NodeMessage message=new NodeMessage(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort(),node);
					try {
						ServerSocketConnection.sendMessage(message);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
			
		});
		update.start();
	}
	
}
