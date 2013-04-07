package mapreduce.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import mapreduce.node.connection.JobMessage;
import mapreduce.node.logic.Job;
import mapreduce.sdk.JobConf;

public class Agent {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		ObjectInputStream in = new ObjectInputStream(System.in);
		JobConf conf=(JobConf)in.readObject();
		in.close();
		Job job=new Job(conf,Job.JobStatus.JOB_INIT,conf.getConfiguration().get("mapreduce.job.id"));
		JobMessage message=new JobMessage(args[0],Integer.valueOf(args[1]),job);
		Socket socket = new Socket(args[0],Integer.valueOf(args[1]));
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(message);
		out.close();
	}

}
