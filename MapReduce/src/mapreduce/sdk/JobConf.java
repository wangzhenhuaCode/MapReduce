package mapreduce.sdk;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;



public class JobConf implements Serializable {
	private Map<String, String> configuration;
	public JobConf(Class exampleClass) throws IOException{
		configuration=new HashMap<String,String>();
		findJarAddress(exampleClass);
		configuration.put("map.input.blockSize", "1024");
	}
	private void findJarAddress(Class exampleClass) throws IOException{
		String path=null;
		try {
			File jarFile = new File(exampleClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			configuration.put("mapreduce.jar", jarFile.getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
			
	}
	public void setJobName(String name){
		configuration.put("mapreduce.job.name", name);
	}
	public void setOutputKeyClass(Class<? extends Serializable> exampleClass){
		configuration.put("mapreduce.output.key.class", exampleClass.getName());
	}
	public void setOutputValueClass(Class<? extends Serializable> exampleClass){
		configuration.put("mapreduce.output.value.class", exampleClass.getName());
	}
	public void setMapperClass(Class<? extends Mapper> exampleClass){
		configuration.put("mapreduce.map.class", exampleClass.getName());
	}
	public void setCombinerClass(Class<? extends Reducer> exampleClass){
		configuration.put("mapreduce.combiner.class", exampleClass.getName());
	}
	public void setReducerClass(Class<? extends Reducer> exampleClass){
		configuration.put("mapreduce.reduce.class", exampleClass.getName());
	}
	public void setInputFormat(Class<? extends InputFormat> exampleClass){
		configuration.put("mapreduce.input.format", exampleClass.getName());
	}
	public void setOutputFormat(Class<? extends OutputFormat> exampleClass){
		configuration.put("mapreduce.output.format", exampleClass.getName());
	}
	protected void setInputFile(String path){
		configuration.put("mapreduce.input.path", path);
	}
	protected void setOutputFile(String path){
		configuration.put("mapreduce.output.path", path);
	}
	public Map<String, String> getConfiguration() {
		return configuration;
	}
	protected void setJobId(String id){
		configuration.put("mapreduce.job.id", id);
	}
	public void setWorkingDirectory(String path){
		configuration.put("mapreduce.workingDirectory", path);
	}
	public void setSplitSize(Long size){
		if(size<128){
			size=128L;
		}
		configuration.put("mapreduce.blockSize", size.toString());
	}
	public void setMaxMapReduceRatio(Integer ratio){
		if(ratio<2){
			ratio=2;
		}
		configuration.put("mapreduce.mapReduceRatio", ratio.toString());
	}
}
