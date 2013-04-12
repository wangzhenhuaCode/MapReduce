package mapreduce.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

import java.io.ObjectInputStream;

import java.net.InetAddress;
import java.net.Socket;

import java.util.Date;

import mapreduce.node.connection.JobMessage;



public class JobClient {

	public static void runJob(JobConf conf){
		JobClient client=new JobClient();
		try {
			client.submitJob(conf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}
	public void submitJob(JobConf conf) throws Exception {
		String localHost=InetAddress.getLocalHost().getHostName();
		String id=(new Date()).getTime() + "_" + localHost;
		conf.setJobId(id);
		File output=new File(conf.getConfiguration().get("mapreduce.output.path"));
		if(!conf.getConfiguration().containsKey("mapreduce.workingDirectory")){
			String workingDirectory=output.getParentFile().getAbsolutePath()+"/";
			
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
			throw new Exception("Job name required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.output.key.class")){
			throw new Exception("Output key class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.output.value.class")){
			throw new Exception("Output value class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.map.class")){
			throw new Exception("Map class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.combiner.class")){
			throw new Exception("Combin class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.reduce.class")){
			throw new Exception("Reduce class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.input.format")){
			throw new Exception("Input format class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.output.format")){
			throw new Exception("Output format class required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.input.path")){
			throw new Exception("Input path required");
			
		}
		if(!conf.getConfiguration().containsKey("mapreduce.output.path")){
			throw new Exception("Output path required");
			
		}
		File jarFile = new File(JobClient.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		String path=jarFile.getParentFile().getAbsolutePath();
		File f=new File(path+"/config.cg");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		MasterInfo info= (MasterInfo) in.readObject();
		in.close();
		JobMessage message=new JobMessage(info.getMasterHost(),info.getMasterPort(),conf);
		Socket socket = new Socket(info.getMasterHost(),info.getMasterPort());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(message);
		out.close();
		socket.close();
		f.delete();
	}
}
