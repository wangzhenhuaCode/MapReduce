package mapreduce.node;

import java.io.IOException;

import mapreduce.node.connection.MessageThreadPool;
import mapreduce.node.connection.NodeMessage;
import mapreduce.node.connection.NodeMessage.NodeMessageType;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.logic.MapReduceThreadPool;
import mapreduce.node.logic.MessageProcessor;
import mapreduce.node.logic.NodeStatus;
import mapreduce.node.logic.SlaveMessageProcessor;

public class Slave extends Node {

	public Slave(){
		super();
		try {
			ServerSocketConnection.createServerSocketConnection(NodeSystem.configuration.getLocalPort());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageProcessor processor=new SlaveMessageProcessor();
		MessageThreadPool.createInstance(NodeSystem.configuration.getMessageMaxPoolSize(), processor);
		MapReduceThreadPool.init(NodeSystem.configuration.getCapability());
		Thread update=new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NodeMessageType type=NodeMessageType.NEW_NODE;
				while(true){
					NodeStatus node=new NodeStatus();
					node.setConfiguration(NodeSystem.configuration);
					node.setNodeId(NodeSystem.configuration.getLocalHostName()+"_"+NodeSystem.configuration.getLocalPort());
					node.setRunning(MapReduceThreadPool.getWorkingNum());
					node.setWaiting(MapReduceThreadPool.getWaitingNum());
					NodeMessage message=new NodeMessage(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort(),node);
					message.setType(type);
					try {
						ServerSocketConnection.sendMessage(message);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("Disconnected with Master");
						System.exit(0);
					}
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					type=NodeMessageType.NODE_UPDATE;
					
					
				}
			}
			
		});
		update.start();
	}
	
}
