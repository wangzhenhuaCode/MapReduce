package mapreduce.node;

import mapreduce.node.configuration.Configuration;
import mapreduce.node.connection.ServerSocketConnection;

public class Node {

	public static Node createNode(){
		
		Node node=null;
		
		if(NodeSystem.configuration.getIsMaster()){
			node=new Master();
		}else{
			node=new Slave();
		}
		
		return node;
	}
	public void connect()throws Exception{
		
	}
	
}
