package mapreduce.node;


import java.util.Hashtable;
import java.util.List;

import mapreduce.node.configuration.Configuration;
import mapreduce.node.logic.Job;
import mapreduce.node.logic.NodeStatus;

public class NodeSystem { 
	public static Configuration configuration;
	public static Node node;
	public static List<NodeStatus> nodeList;
	public static Hashtable<String,Job> jobList;
}
