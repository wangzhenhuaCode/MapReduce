package mapreduce.sdk;

import java.io.File;
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
		File output=new File(conf.getConfiguration().get("mapreduce.output.path"));
		if(!conf.getConfiguration().containsKey("mapreduce.workingDirectory")){
			String workingDirectory=output.getAbsolutePath();
			workingDirectory=workingDirectory.substring(0,workingDirectory.length()-1);
			workingDirectory=workingDirectory+id.replace('.', '_');
			
			conf.setWorkingDirectory(workingDirectory+"/");
		}
		
		File directory=new File(conf.getConfiguration().get("mapreduce.workingDirectory"));
		directory.mkdir();
		
		if(!conf.getConfiguration().containsKey("mapreduce.blockSize")){
			conf.setSplitSize(1024L);
		}
		if(!conf.getConfiguration().containsKey("mapreduce.mapReduceRatio")){
			conf.setMaxMapReduceRatio(10);
		}
		if(!conf.getConfiguration().containsKey("mapreduce.job.name")){
			System.out.println("Job name required");
			return;
		}
		if(!conf.getConfiguration().containsKey("mapreduce.output.key.class")){
			System.out.println("Output key class required");
			return;
		}
		
		ObjectOutputStream out = new ObjectOutputStream(System.out);
		out.writeObject(conf);
		out.close();
	}
}
