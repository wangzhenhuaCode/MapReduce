package mapreduce.node.logic;

import java.util.LinkedList;
import java.util.Queue;

import mapreduce.node.connection.TaskMessage;

public class MapReduceThreadPool {

	private static Thread[] workers;
	private static int capability;
	private static Queue<TaskMessage> taskQueue;
	public static void init(int capability){
		MapReduceThreadPool.capability=capability;
		taskQueue=new LinkedList<TaskMessage>();
		workers=new Thread[capability];
		for(int i=0;i<capability;i++){
			workers[i]=new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true){
						while(taskQueue.isEmpty())
						
					}
					
				}
				
			});
			workers[i].start();
		}
		
	}
}
