package mapreduce.node;

import java.util.ArrayList;
import java.util.Hashtable;

import mapreduce.node.connection.MessageThreadPool;
import mapreduce.node.connection.ServerSocketConnection;
import mapreduce.node.logic.Job;
import mapreduce.node.logic.MasterMessageProcessor;
import mapreduce.node.logic.MessageProcessor;
import mapreduce.node.logic.NodeBalance;
import mapreduce.node.logic.NodeStatus;

public class Master extends Node {
	public Master(){
		super();
		try {
			ServerSocketConnection.createServerSocketConnection(NodeSystem.configuration.getLocalPort());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeSystem.jobList=new Hashtable<String,Job>();
		NodeSystem.nodeList=new ArrayList<NodeStatus>();
		NodeBalance.init();
		MessageProcessor processor=new MasterMessageProcessor();
		MessageThreadPool.createInstance(NodeSystem.configuration.getMessageMaxPoolSize(), processor);
		
		
	}
}
