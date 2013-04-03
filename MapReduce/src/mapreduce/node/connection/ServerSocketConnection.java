package mapreduce.node.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerSocketConnection {

		public static ServerSocketConnection instance=null;
		private Queue<Socket> messageQueue;
		private Thread listen;
		private ServerSocket server;
		private Integer port;
		public static ServerSocketConnection createServerSocketConnection(final Integer port) throws IOException{
			if(instance==null){
				instance=new ServerSocketConnection();
				instance.messageQueue=new LinkedList<Socket>();
				instance.server= new ServerSocket(port);
				instance.port=port;
				instance.listen=new Thread(new Runnable(){	
					@Override
					public void run() {
						
						
						
						while(true){
							Socket s=null;
							try {
								s=instance.server.accept();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							
							synchronized(instance.messageQueue){
								instance.messageQueue.add(s);
								instance.messageQueue.notify();
							}
							
						}
					}
					
				});
				instance.listen.start();
			}
			return instance;
		}
		public static ServerSocketConnection getInstance(){
			
				return instance;
			
		}
		public static Socket getNewSocket() throws InterruptedException{
			Socket s=null;
			synchronized(instance.messageQueue){
				while(instance.messageQueue.isEmpty()){
					instance.messageQueue.wait();
				}
				s=instance.messageQueue.poll();
			}
			return s;
		}
		public Integer getPort() {
			return port;
		}
	
}
