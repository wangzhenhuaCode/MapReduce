package mapreduce.sdk;

public class JobClient {

	public static void runJob(JobConf conf){
		JobClient client=new JobClient();
		client.submitJob(conf);
	}
	public void submitJob(JobConf conf){
		
	}
}
