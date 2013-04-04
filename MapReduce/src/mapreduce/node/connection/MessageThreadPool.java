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
							Socket s=null;
							try {
								s=ServerSocketConnection.getNewSocket();
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
							ObjectInputStream in = null;
							try {
								in = new ObjectInputStream(s.getInputStream());
								Message message=(Message) in.readObject();
								String remoteIp=s.getInetAddress().getHostAddress();
								
								in.close();
								s.close();
								//MessageProcessor mp=(MessageProcessor) processClass.newInstance();
								
								
							}catch (IOException e) {
								
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								
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
