package mapreduce.node.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class MessageThreadPool {

	private Thread[] workerList;
	static MessageThreadPool instance;
	public static MessageThreadPool getInstance(int n, final Class<?> processClass ){
		if(instance==null){
		instance=new MessageThreadPool();
		instance.workerList=new Thread[n];
			for(int i=0;i<n;i++){
				instance.workerList[i]=new Thread(new Runnable(){

					@Override
					public void run() {
						
						while(true){
							try {
								ServerSocketConnection.getNewMessage();
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
