package mapreduce.node;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


import mapreduce.node.connection.MgtMessage;


public class MgtAgent {
	static Integer localPort;
	static String masterHost;
	static Integer masterPort;
	static Integer mgtPort;
	public static void main(String[] args) throws UnknownHostException, IOException{
		localPort=Integer.valueOf(args[0]);
		masterHost=args[1];
		masterPort=Integer.valueOf(args[2]);
		mgtPort=Integer.valueOf(args[3]);
		String operation=args[4];
		if(operation.equals("shutDown")){
			MgtMessage message=new MgtMessage("127.0.0.1",localPort,MgtMessage.Operation.SHUT_DOWN);
			Socket socket=new Socket(message.getReceiverHost(),message.getReceiverPort());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.close();
		}else if(operation.equals("jobMgt")){
			
		}
	}
	public void jobMgt() throws IOException, ClassNotFoundException{
		String command;
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while(!(command=in.readLine()).equals("exit")){
			String[] arg=command.split("\\s+");
			if(arg[0].equals("listJob")){
				MgtMessage message=new MgtMessage(masterHost,masterPort,MgtMessage.Operation.JOB_STATUS);
				waitForMessage(message);
				
			}else if(arg[0].equals("killJob")){
				if(arg.length!=2){
					System.out.println();
					System.out.println("Incorrect command");
				}else{
					MgtMessage message=new MgtMessage(masterHost,masterPort,MgtMessage.Operation.KILL_JOB);
					message.setId(arg[1]);
					waitForMessage(message);
					
				}
				
			}else if(arg[0].equals("reportJob")){
				if(arg.length!=2){
					System.out.println();
					System.out.println("Incorrect command");
				}else{
					MgtMessage message=new MgtMessage(masterHost,masterPort,MgtMessage.Operation.JOB_STATUS);
					message.setId(arg[1]);
					waitForMessage(message);
				}
				
			}else{
				System.out.println();
				System.out.println("Incorrect command");
			}
			
		}
	}
	public static void waitForMessage(MgtMessage message) throws IOException, ClassNotFoundException{
		ServerSocket server=new ServerSocket(mgtPort);
		Socket socket=new Socket(message.getReceiverHost(),message.getReceiverPort());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(message);
		out.close();
		Socket s=server.accept();
		ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
		MgtMessage returnMessage=(MgtMessage) oin.readObject();
		oin.close();
		System.out.println(returnMessage.getReport().getLog());
	}
}
