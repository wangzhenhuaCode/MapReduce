package mapreduce.node.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import mapreduce.node.logic.MessageProcessor;


public class MessageThreadPool {

	private Thread[] workerList;
	static MessageThreadPool instance;
	static MessageProcessor messageProcessor;
	public static MessageThreadPool createInstance(int n,MessageProcessor processor ){
		if(instance==null){
		instance=new MessageThreadPool();
		messageProcessor=processor;
		instance.workerList=new Thread[n];
			for(int i=0;i<n;i++){
				instance.workerList[i]=new Thread(new Runnable(){

					@Override
					public void run() {
						
						while(true){
							try {
								Message message=ServerSocketConnection.getNewMessage();
								messageProcessor.process(message);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					
				});
				instance.workerList[i].start();
			}
			
		}
		return instance;
	}


}
