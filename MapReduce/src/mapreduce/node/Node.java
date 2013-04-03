package mapreduce.node;

import mapreduce.node.configuration.Configuration;

public class Node {
	private Configuration configuration;
	public static Node createNode(Configuration configuration){
		Node node=null;
		if(configuration.getIsMaster()){
			node=new Master();
		}else{
			node=new Slave();
		}
		node.configuration=configuration;
		return node;
	}
	public void run()throws Exception{
		
	}
	
}