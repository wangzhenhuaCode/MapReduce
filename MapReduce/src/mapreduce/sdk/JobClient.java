package mapreduce.sdk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Date;

public class JobClient {

	public static void runJob(JobConf conf) throws IOException{
		JobClient client=new JobClient();
		client.submitJob(conf);
	}
	public void submitJob(JobConf conf) throws IOException{
		String localHost=InetAddress.getLocalHost().getHostName();
		String id=(new Date()).getTime() + "_" + localHost;
		conf.setJobId(id);
		ObjectOutputStream out = new ObjectOutputStream(System.out);
		out.writeObject(conf);
		out.close();
	}
}
