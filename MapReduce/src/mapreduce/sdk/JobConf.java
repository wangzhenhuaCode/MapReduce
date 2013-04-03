package mapreduce.sdk;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JobConf {
	private Map<String, String> configuration;
	public JobConf(Class exampleClass) throws IOException{
		configuration=new HashMap<String,String>();
		findJarAddress(exampleClass);
	}
	private void findJarAddress(Class exampleClass) throws IOException{
		String path=null;
		ClassLoader loader = exampleClass.getClassLoader();
		String className = exampleClass.getName().replaceAll("\\.", "/") + ".class";
		Enumeration<URL> urlItr=loader.getResources(className);
		for(URL url=urlItr.nextElement();urlItr.hasMoreElements();url=urlItr.nextElement()){
			if(url.getProtocol().equals("jar")){
				path=url.getPath();
				if(path.startsWith("file:")){
					path=path.substring(5);
				}
				
			}
		}
		if(path!=null)
			configuration.put("mapreduce.jar", path);
	}
	public void setJobName(String name){
		configuration.put("mapreduce.job.name", name);
	}
	public void setOutputKeyClass(Class<Serializable> exampleClass){
		configuration.put("mapreduce.output.key.class", exampleClass.getName());
	}
	public void setOutputValueClass(Class<Serializable> exampleClass){
		configuration.put("mapreduce.output.value.class", exampleClass.getName());
	}
	public void setMapperClass(Class<Mapper> exampleClass){
		configuration.put("mapreduce.map.class", exampleClass.getName());
	}
	public void setCombinerClass(Class<Reducer> exampleClass){
		configuration.put("mapreduce.combiner.class", exampleClass.getName());
	}
	public void setReducerClass(Class<Reducer> exampleClass){
		configuration.put("mapreduce.reduce.class", exampleClass.getName());
	}
	public void setInputFormat(Class<InputFormat> exampleClass){
		configuration.put("mapreduce.input.format", exampleClass.getName());
	}
	public void setOutputFormat(Class<OutputFormat> exampleClass){
		configuration.put("mapreduce.output.format", exampleClass.getName());
	}
	protected void setInputFile(String path){
		configuration.put("mapreduce.input.path", path);
	}
	
}
