package mapreduce.node.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		public static Message getNewMessage() throws InterruptedException, IOException, ClassNotFoundException{
			Socket s=null;
			synchronized(instance.messageQueue){
				while(instance.messageQueue.isEmpty()){
					instance.messageQueue.wait();
				}
				s=instance.messageQueue.poll();
			}
			ObjectInputStream in = null;
		
				in = new ObjectInputStream(s.getInputStream());
				Message message=(Message) in.readObject();
				String remoteIp=s.getInetAddress().getHostAddress();
				
				in.close();
				s.close();
				
			
			return message;
		}
		public Integer getPort() {
			return port;
		}
		public static void sendMessage(Message message) throws IOException{
			Socket socket=new Socket(message.getReceiverHost(),message.getReceiverPort());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.close();
			socket.close();
		}
	
}
